package com.futevoleidolago.backend.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public class MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

    private final JavaMailSender mailSender;

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.number}")
    private String twilioNumber;

    private boolean twilioInitialized = false;

    public MessagingService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void initializeTwilio() {
        if (!twilioInitialized) {
            try {
                Twilio.init(accountSid, authToken);
                twilioInitialized = true;
                logger.info("Twilio inicializado com sucesso.");
            } catch (Exception e) {
                logger.error("Erro ao inicializar Twilio: {}", e.getMessage());
                throw new RuntimeException("Falha na inicialização do Twilio: " + e.getMessage());
            }
        }
    }

    public void sendEmail(String to, String subject, String text) {
        logger.info("Tentando enviar email para {} com assunto: {}", to, subject);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("tadeucassis@gmail.com");
            mailSender.send(message);
            logger.info("Email enviado com sucesso para {}", to);
        } catch (Exception e) {
            if (e.getCause() instanceof UnknownHostException) {
                logger.error("Falha ao resolver o host smtp.gmail.com: {}", e.getMessage());
                throw new RuntimeException("Não foi possível conectar ao servidor de email devido a problemas de rede ou DNS.");
            }
            logger.error("Erro ao enviar email para {}: {}", to, e.getMessage());
            throw new RuntimeException("Falha ao enviar email: " + e.getMessage());
        }
    }

    public void sendWhatsApp(String to, String text) {
        logger.info("Tentando enviar mensagem WhatsApp para {}", to);
        initializeTwilio();
        try {
            Message.creator(
                    new PhoneNumber("whatsapp:" + to),
                    new PhoneNumber(twilioNumber),
                    text
            ).create();
            logger.info("Mensagem WhatsApp enviada com sucesso para {}", to);
        } catch (Exception e) {
            if (e.getCause() instanceof UnknownHostException) {
                logger.error("Falha ao resolver o host do Twilio: {}", e.getMessage());
                throw new RuntimeException("Não foi possível conectar ao Twilio devido a problemas de rede ou DNS.");
            }
            logger.error("Erro ao enviar mensagem WhatsApp para {}: {}", to, e.getMessage());
            throw new RuntimeException("Falha ao enviar mensagem WhatsApp: " + e.getMessage());
        }
    }
}