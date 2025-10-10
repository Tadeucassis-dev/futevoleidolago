package com.futevoleidolago.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica CORS a todos os endpoints
                .allowedOrigins(
                    "http://localhost:3000",  // React (Create React App)
                    "http://localhost:5173",  // Vite
                    "http://localhost:5174",  // Vite alternativo
                    "http://127.0.0.1:5173",  // Vite com 127.0.0.1
                    "http://127.0.0.1:3000"   // React com 127.0.0.1
                ) 
                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS") // Adicionei OPTIONS
                .allowedHeaders("*") // Permite todos os cabeçalhos
                .allowCredentials(true); // Permite credenciais, se necessário
    }
}