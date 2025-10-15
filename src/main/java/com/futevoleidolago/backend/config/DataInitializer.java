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
        // Criar usuário admin padrão
        createUserIfNotExists("tadeucassis@gmail.com", "85792426", "Administrador");
        
        // 🔧 ADICIONE SEUS USUÁRIOS AQUI:
        // Descomente e modifique as linhas abaixo para criar novos usuários
        
        // createUserIfNotExists("seu.email@exemplo.com", "suaSenha123", "Seu Nome");
        // createUserIfNotExists("outro@exemplo.com", "outraSenha456", "Outro Nome");
        
        // Exemplo de usuários que você pode criar:
        // createUserIfNotExists("tadeu@futevoleidolago.com", "tadeu123", "Tadeu Cesar");
        // createUserIfNotExists("gerente@futevoleidolago.com", "gerente123", "Gerente");
    }
    
    private void createUserIfNotExists(String email, String password, String name) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            
            userRepository.save(user);
            System.out.println("✅ Usuário criado:");
            System.out.println("   Nome: " + name);
            System.out.println("   Email: " + email);
            System.out.println("   Senha: " + password);
            System.out.println("   ⚠️  IMPORTANTE: Mude esta senha após o primeiro login!");
        } else {
            System.out.println("ℹ️  Usuário " + email + " já existe no sistema");
        }
    }
}