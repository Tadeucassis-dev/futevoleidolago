package com.futevoleidolago.backend.config;

import com.futevoleidolago.backend.models.User;
import com.futevoleidolago.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@futevoleidolago.com";
        String adminPassword = "admin123";
        
        // Verificar se já existe um admin
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            
            userRepository.save(admin);
            System.out.println("✅ Usuário admin criado:");
            System.out.println("   Email: " + adminEmail);
            System.out.println("   Senha: " + adminPassword);
            System.out.println("   ⚠️  IMPORTANTE: Mude esta senha após o primeiro login!");
        } else {
            System.out.println("ℹ️  Usuário admin já existe no sistema");
        }
    }
}