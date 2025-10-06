package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    // Configuración de la seguridad HTTP --> define que request del controlador están protegidos o abiertos
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // desactiva CSRF para pruebas con Postman
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // indica que todas las rutas están abiertas, sin autenticación

        return http.build();
    }

    // Define como se encriptan/verifican las contraseñas
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
