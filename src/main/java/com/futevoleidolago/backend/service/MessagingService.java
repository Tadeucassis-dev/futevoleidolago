package com.futevoleidolago.backend.service;

import com.futevoleidolago.backend.repositories.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class MessagingService {
    private final JavaMailSender javaMailSender;
    private final AlunoRepository alunoRepository;

    // Substitua pelo seu API key da CallMeBot
    private static final String CALLMEBOT_API_KEY = "your_callmebot_api_key";
    private static final String CALLMEBOT_URL = "https://api.callmebot.com/whatsapp.php";

    @Autowired
    public MessagingService(JavaMailSender javaMailSender, AlunoRepository alunoRepository) {
        this.javaMailSender = javaMailSender;
        this.alunoRepository = alunoRepository;
    }

    public void sendWhatsApp(String to, String message) throws Exception {
        // Normalizar o número para o formato E.164 (ex.: +5511999999999)
        String normalizedNumber = normalizePhoneNumber(to);

        // Montar a URL da requisição para CallMeBot
        String urlString = CALLMEBOT_URL + "?phone=" + normalizedNumber +
                "&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8) +
                "&apikey=" + CALLMEBOT_API_KEY;

        // Fazer a requisição HTTP
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);

        // Verificar o código de resposta
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Erro ao enviar mensagem WhatsApp: HTTP " + responseCode);
        }

        conn.disconnect();
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        javaMailSender.send(msg);
    }

    private String normalizePhoneNumber(String phone) {
        // Remover caracteres não numéricos
        String cleaned = phone.replaceAll("[^0-9+]", "");
        // Garantir formato E.164 (ex.: +5511999999999)
        if (!cleaned.startsWith("+")) {
            // Assumir código do Brasil (+55) se não especificado
            cleaned = "+55" + cleaned;
        }
        return cleaned;
    }
}