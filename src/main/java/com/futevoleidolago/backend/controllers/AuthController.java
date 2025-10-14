package com.futevoleidolago.backend.controllers;

import com.futevoleidolago.backend.RequestDTO.LoginRequestDTO;
import com.futevoleidolago.backend.RequestDTO.UserDTO;
import com.futevoleidolago.backend.RequestDTO.UserRegisterDTO;
import com.futevoleidolago.backend.repositories.UserRepository;
import com.futevoleidolago.backend.service.AuthService;
import com.futevoleidolago.backend.models.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequestDTO credentials) {
        System.out.println("Recebido: email=" + credentials.getEmail() + ", password=[PROTECTED]");
        try {
            String token = authService.login(credentials.getEmail(), credentials.getPassword());
            System.out.println("Login realizado com sucesso para: " + credentials.getEmail());
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login realizado com sucesso",
                "token", token
            ));
        } catch (RuntimeException e) {
            System.out.println("Erro no login: " + e.getMessage());
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            // Converter DTO para entidade
            User user = new User();
            user.setName(userRegisterDTO.getName());
            user.setEmail(userRegisterDTO.getEmail());
            user.setPassword(userRegisterDTO.getPassword());

            User registeredUser = authService.register(user);
            UserDTO userDTO = new UserDTO(registeredUser.getId(), registeredUser.getName(), registeredUser.getEmail());
            
            System.out.println("Usuário cadastrado com sucesso: " + registeredUser.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Usuário cadastrado com sucesso! Você pode fazer login agora.",
                "user", userDTO
            ));
        } catch (RuntimeException e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
}