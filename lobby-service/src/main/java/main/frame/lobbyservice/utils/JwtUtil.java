package main.frame.lobbyservice.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private SecretKey secretKey;

    @Value("${jwt.secret}")
    private String secretKeyString;

    @PostConstruct
    public void init() {
        // Генерация ключа из строки
        this.secretKey = new SecretKeySpec(secretKeyString.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    // Извлечение email из токена
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Проверка токена на валидность
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

//    public List<String> extractRoles(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        // Предполагаем, что роли хранятся как список строк
//        return claims.get("roles", List.class);
//    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Извлекаем роли как List<Map<String, Object>> с использованием Gson для безопасного приведения типов
        Gson gson = new Gson();
        String rolesJson = gson.toJson(claims.get("roles"));

        // Преобразуем JSON строку в List<Map<String, Object>>
        List<Map<String, Object>> roles = gson.fromJson(rolesJson, new TypeToken<List<Map<String, Object>>>() {}.getType());

        // Извлекаем имена ролей из Map
        return roles.stream()
                .map(role -> (String) role.get("name")) // Извлекаем имя роли из RoleDTO
                .collect(Collectors.toList());

//        // Извлекаем Set<RoleDTO> из токена
//        List<Map<String, Object>> roles = (List<Map<String, Object>>) claims.get("roles", List.class);
//
//        // Преобразуем в список строк (имена ролей)
//        return roles.stream()
//                .map(role -> (String) role.get("name")) // Извлекаем имя роли из RoleDTO
//                .collect(Collectors.toList());
        // Предполагаем, что роли хранятся как список строк
        //  return claims.get("roles", List.class);
    }

}

