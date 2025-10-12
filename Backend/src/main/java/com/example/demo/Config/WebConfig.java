package com.example.demo.Config;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")          // aplica a todas las rutas
                        .allowedOrigins("*")        // permite cualquier origen
                        .allowedMethods("*")        // permite todos los métodos (GET, POST, etc.)
                        .allowedHeaders("*")        // permite todos los headers
                        .allowCredentials(false);   // no enviar cookies/credenciales con '*'
            }
        };
    }
}
