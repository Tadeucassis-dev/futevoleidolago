package com.futevoleidolago.backend.controllers;

import com.futevoleidolago.backend.models.Aluno;
import com.futevoleidolago.backend.repositories.AlunoRepository;
import com.futevoleidolago.backend.service.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
        try {
            Aluno aluno = alunoRepository.findById(idAluno)
                    .orElseThrow(() -> {
                        logger.error("Aluno com ID {} não encontrado", idAluno);
                        return new RuntimeException("Aluno não encontrado");
                    });

            StringBuilder responseMessage = new StringBuilder();

            // Tentar enviar email
            if (aluno.getEmail() != null && !aluno.getEmail().isEmpty()) {
                try {
                    messagingService.sendEmail(aluno.getEmail(), "Futevôlei do Lago", mensagem);
                    logger.info("Email enviado com sucesso para {}", aluno.getEmail());
                    responseMessage.append("Email enviado para ").append(aluno.getNome());
                } catch (Exception e) {
                    logger.error("Erro ao enviar email para {}: {}", aluno.getEmail(), e.getMessage());
                    responseMessage.append("Falha ao enviar email: ").append(e.getMessage());
                }
            } else {
                logger.warn("Aluno ID {} não tem email configurado", idAluno);
                responseMessage.append("Nenhum email configurado para ").append(aluno.getNome());
            }

            return ResponseEntity.ok(responseMessage.toString());
        } catch (RuntimeException e) {
            logger.error("Erro ao processar notificação para aluno ID {}: {}", idAluno, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado ao processar notificação para aluno ID {}: {}", idAluno, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao enviar notificação");
        }
    }

    @PostMapping("/todos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sendNotificationToAll(
            @RequestBody Map<String, String> body
    ) {
        String mensagem = body.get("mensagem");
        if (mensagem == null || mensagem.trim().isEmpty()) {
            logger.warn("Mensagem vazia para envio em massa");
            return ResponseEntity.badRequest().body("A mensagem não pode ser vazia");
        }

        logger.info("Enviando notificação em massa: {}", mensagem);
        try {
            Iterable<Aluno> alunos = alunoRepository.findAll();
            StringBuilder responseMessage = new StringBuilder();
            int emailsEnviados = 0;
            int falhas = 0;

            for (Aluno aluno : alunos) {
                if (aluno.getEmail() != null && !aluno.getEmail().isEmpty()) {
                    try {
                        messagingService.sendEmail(aluno.getEmail(), "Futevôlei do Lago", mensagem);
                        logger.info("Email enviado com sucesso para {}", aluno.getEmail());
                        emailsEnviados++;
                        responseMessage.append("Email enviado para ").append(aluno.getNome()).append("; ");
                    } catch (Exception e) {
                        logger.error("Erro ao enviar email para {}: {}", aluno.getEmail(), e.getMessage());
                        falhas++;
                        responseMessage.append("Falha ao enviar email para ").append(aluno.getNome()).append(": ").append(e.getMessage()).append("; ");
                    }
                } else {
                    logger.warn("Aluno {} não tem email configurado", aluno.getNome());
                    responseMessage.append("Nenhum email configurado para ").append(aluno.getNome()).append("; ");
                }
            }

            String finalMessage = String.format("Envio em massa concluído: %d emails enviados, %d falhas. Detalhes: %s",
                    emailsEnviados, falhas, responseMessage.toString());
            return ResponseEntity.ok(finalMessage);
        } catch (Exception e) {
            logger.error("Erro inesperado ao enviar notificação em massa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao enviar notificação em massa: " + e.getMessage());
        }
    }
}