package com.futevoleidolago.backend.controllers;

import com.futevoleidolago.backend.models.Aluno;
import com.futevoleidolago.backend.repositories.AlunoRepository;
import com.futevoleidolago.backend.service.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final MessagingService messagingService;
    private final AlunoRepository alunoRepository;

    public NotificationController(MessagingService messagingService, AlunoRepository alunoRepository) {
        this.messagingService = messagingService;
        this.alunoRepository = alunoRepository;
    }

    @PostMapping("/{idAluno}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> sendManualNotification(
            @PathVariable Long idAluno,
            @RequestBody Map<String, String> body
    ) {
        String mensagem = body.get("mensagem");
        if (mensagem == null || mensagem.trim().isEmpty()) {
            logger.warn("Mensagem vazia para aluno ID {}", idAluno);
            return ResponseEntity.badRequest().body("A mensagem não pode ser vazia");
        }

        logger.info("Enviando notificação manual para aluno ID {}: {}", idAluno, mensagem);
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> {
                    logger.error("Aluno com ID {} não encontrado", idAluno);
                    return new RuntimeException("Aluno não encontrado");
                });

        boolean emailSent = false;
        boolean whatsappSent = false;
        StringBuilder responseMessage = new StringBuilder();

        // Tentar enviar email
        if (aluno.getEmail() != null && !aluno.getEmail().isEmpty()) {
            try {
                messagingService.sendEmail(aluno.getEmail(), "Notificação Manual", mensagem);
                logger.info("Email enviado com sucesso para {}", aluno.getEmail());
                emailSent = true;
                responseMessage.append("Email enviado para ").append(aluno.getNome());
            } catch (Exception e) {
                logger.error("Erro ao enviar email para {}: {}", aluno.getEmail(), e.getMessage());
                responseMessage.append("Falha ao enviar email: ").append(e.getMessage());
            }
        } else {
            logger.warn("Aluno ID {} não tem email configurado", idAluno);
            responseMessage.append("Nenhum email configurado para ").append(aluno.getNome());
        }

        // Tentar enviar WhatsApp
        if (aluno.getTelefone() != null && !aluno.getTelefone().isEmpty()) {
            try {
                messagingService.sendWhatsApp(aluno.getTelefone(), mensagem);
                logger.info("Mensagem WhatsApp enviada com sucesso para {}", aluno.getTelefone());
                whatsappSent = true;
                if (responseMessage.length() > 0) responseMessage.append("; ");
                responseMessage.append("WhatsApp enviado para ").append(aluno.getNome());
            } catch (Exception e) {
                logger.error("Erro ao enviar WhatsApp para {}: {}", aluno.getTelefone(), e.getMessage());
                if (responseMessage.length() > 0) responseMessage.append("; ");
                responseMessage.append("Falha ao enviar WhatsApp: ").append(e.getMessage());
            }
        } else {
            logger.warn("Aluno ID {} não tem telefone configurado", idAluno);
            if (responseMessage.length() > 0) responseMessage.append("; ");
            responseMessage.append("Nenhum telefone configurado para ").append(aluno.getNome());
        }

        if (!emailSent && !whatsappSent) {
            return ResponseEntity.status(500).body(responseMessage.toString());
        }

        return ResponseEntity.ok(responseMessage.toString());
    }
}