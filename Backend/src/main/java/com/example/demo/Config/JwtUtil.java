package com.example.demo.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clave secreta segura (puedes moverla a application.properties)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tiempo de expiración: 1 hora
    private final long expiration = 3600000;

    // Generar token con claims
    public String generateToken(String subject, String role) {
        return Jwts.builder()
                .setSubject(subject) // normalmente email o username
                .claim("role", role) // rol de usuario
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // Obtener claims del token
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    // Obtener subject (ej: email del usuario)
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Validar token
    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
