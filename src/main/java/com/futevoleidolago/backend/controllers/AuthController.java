package com.futevoleidolago.backend.controllers;

import com.futevoleidolago.backend.RequestDTO.LoginRequestDTO;
import com.futevoleidolago.backend.RequestDTO.UserDTO;
import com.futevoleidolago.backend.repositories.UserRepository;
import com.futevoleidolago.backend.service.AuthService;
import com.futevoleidolago.backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO credentials) {
        System.out.println("Recebido: email=" + credentials.getEmail() + ", password=" + credentials.getPassword());
        try {
            String token = authService.login(credentials.getEmail(), credentials.getPassword());
            System.out.println("Token gerado: " + token);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            System.out.println("Erro: " + e.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        User registeredUser = authService.register(user);
        UserDTO userDTO = new UserDTO(registeredUser.getId(), registeredUser.getName(), registeredUser.getEmail());
        return ResponseEntity.ok(userDTO);
    }
}