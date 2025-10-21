package com.futevoleidolago.backend.service;

import com.futevoleidolago.backend.repositories.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class MessagingService {
    private final AlunoRepository alunoRepository;

    // Substitua pelo seu API key da CallMeBot
    private static final String CALLMEBOT_API_KEY = "your_callmebot_api_key";
    private static final String CALLMEBOT_URL = "https://api.callmebot.com/whatsapp.php";

    @Autowired
    public MessagingService(AlunoRepository alunoRepository) {
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
        // Email temporariamente desabilitado - JavaMailSender não configurado
        System.out.println("📧 EMAIL SIMULADO:");
        System.out.println("   Para: " + to);
        System.out.println("   Assunto: " + subject);
        System.out.println("   Mensagem: " + text);
        System.out.println("   ⚠️  Configure JavaMailSender para enviar emails reais");
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