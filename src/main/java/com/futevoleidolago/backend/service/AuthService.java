package com.futevoleidolago.backend.service;

import com.futevoleidolago.backend.models.JwtUtils;
import com.futevoleidolago.backend.models.User;
import com.futevoleidolago.backend.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtils jwtUtils, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String email, String password) {
        // Validação de entrada
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("Email é obrigatório");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("Senha é obrigatória");
        }

        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return jwtUtils.generateToken(email);
    }

    public User register(User user) {
        System.out.println("Iniciando registro para email: " + user.getEmail());
        
        // Validações de entrada
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("Erro: Email é obrigatório");
            throw new RuntimeException("Email é obrigatório");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            System.out.println("Erro: Nome é obrigatório");
            throw new RuntimeException("Nome é obrigatório");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            System.out.println("Erro: Senha é obrigatória");
            throw new RuntimeException("Senha é obrigatória");
        }
        if (user.getPassword().length() < 6) {
            System.out.println("Erro: Senha deve ter pelo menos 6 caracteres");
            throw new RuntimeException("Senha deve ter pelo menos 6 caracteres");
        }

        // Verificar se email já existe
        System.out.println("Verificando se email já existe: " + user.getEmail().trim());
        if (userRepository.findByEmail(user.getEmail().trim()).isPresent()) {
            System.out.println("Erro: Email já cadastrado");
            throw new RuntimeException("Email já cadastrado");
        }

        try {
            // Limpar dados e criptografar senha
            user.setEmail(user.getEmail().trim().toLowerCase());
            user.setName(user.getName().trim());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            System.out.println("Salvando usuário no banco de dados...");
            User savedUser = userRepository.save(user);
            System.out.println("Usuário salvo com sucesso! ID: " + savedUser.getId());
            
            return savedUser;
        } catch (Exception e) {
            System.out.println("Erro ao salvar usuário: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro interno do servidor ao criar conta");
        }
    }
}