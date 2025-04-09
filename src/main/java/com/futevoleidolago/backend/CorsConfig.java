package com.futevoleidolago.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica CORS a todos os endpoints
                .allowedOrigins("http://localhost:3000") // Origem do frontend
                .allowedMethods("GET", "POST", "DELETE", "PUT") // Métodos permitidos
                .allowedHeaders("*") // Permite todos os cabeçalhos
                .allowCredentials(true); // Permite credenciais, se necessário
    }
}