package com.futevoleidolago.backend.notifications;

import com.futevoleidolago.backend.models.Aluno;
import com.futevoleidolago.backend.repositories.AlunoRepository;
import com.futevoleidolago.backend.service.MessagingService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
public class NotificationsScheduler {

    private final AlunoRepository alunoRepository;
    private final MessagingService messagingService;

    public NotificationsScheduler(AlunoRepository alunoRepository, MessagingService messagingService) {
        this.alunoRepository = alunoRepository;
        this.messagingService = messagingService;
    }

    // Executa todos os dias às 8h
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkBirthdaysAndDueDates() {
        LocalDate today = LocalDate.now();
        List<Aluno> alunos = alunoRepository.findAllByAtivoTrue();

        for (Aluno aluno : alunos) {
            // Notificação de aniversário
            if (aluno.getDataNascimento() != null &&
                    aluno.getDataNascimento().getDayOfMonth() == today.getDayOfMonth() &&
                    aluno.getDataNascimento().getMonth() == today.getMonth()) {
                String birthdayMessage = "Parabéns, " + aluno.getNome() + "! Desejamos um feliz aniversário!";
                if (aluno.getEmail() != null) {
                    messagingService.sendEmail(aluno.getEmail(), "Feliz Aniversário!", birthdayMessage);
                }
                if (aluno.getTelefone() != null) {
                    messagingService.sendWhatsApp(aluno.getTelefone(), birthdayMessage);
                }
            }

            // Notificação de vencimento da mensalidade (3 dias antes)
            if (aluno.getDiaVencimentoMensalidade() != null) {
                int diaVencimento = aluno.getDiaVencimentoMensalidade();
                LocalDate vencimento = LocalDate.of(today.getYear(), today.getMonth(), Math.min(diaVencimento, today.lengthOfMonth()));
                long diasParaVencimento = today.until(vencimento).getDays();

                // Se o vencimento já passou no mês atual, considera o próximo mês
                if (diasParaVencimento < 0) {
                    vencimento = LocalDate.of(today.getYear(), today.getMonth().plus(1), Math.min(diaVencimento, today.plusMonths(1).lengthOfMonth()));
                    diasParaVencimento = today.until(vencimento).getDays();
                }

                if (diasParaVencimento == 3) {
                    String dueMessage = "Olá, " + aluno.getNome() + "! Sua mensalidade vence em 3 dias (dia " + diaVencimento + "). Por favor, regularize o pagamento.";
                    if (aluno.getEmail() != null) {
                        messagingService.sendEmail(aluno.getEmail(), "Lembrete de Mensalidade", dueMessage);
                    }
                    if (aluno.getTelefone() != null) {
                        messagingService.sendWhatsApp(aluno.getTelefone(), dueMessage);
                    }
                }
            }
        }
    }
}