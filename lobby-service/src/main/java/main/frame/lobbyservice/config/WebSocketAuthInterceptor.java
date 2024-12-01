package main.frame.lobbyservice.config;

import main.frame.lobbyservice.utils.JwtUtil;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    public WebSocketAuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(@Nullable Message<?> message,
                              @Nullable MessageChannel channel) {
        if (message == null || channel == null) {
            return null; // Если параметры `null`, просто возвращаем `null` или обрабатываем иначе
        }
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Убираем "Bearer "

                // Проверяем токен через Spring Security
                Authentication authentication = authenticateToken(token);

                if (authentication == null) {
                    throw new IllegalArgumentException("Токен недействителен!");
                }

                // Сохраняем пользователя в контекст WebSocket
                accessor.setUser(authentication);
            } else {
                throw new IllegalArgumentException("Токен отсутствует!");
            }
        }

        return message;
    }

//    private Authentication authenticateToken(String token) {
//        // Используй свой сервис для проверки токена
//        // Например, JwtTokenProvider.validateToken() и JwtTokenProvider.getAuthentication()
//        return SecurityContextHolder.getContext().getAuthentication();
//    }

    private Authentication authenticateToken(String token) {
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);
            List<String> roles = jwtUtil.extractRoles(token);

            // Преобразование ролей в SimpleGrantedAuthority
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        }
        return null; // Токен недействителен
    }


//    const socket = new SockJS('/ws');
//const stompClient = Stomp.over(socket);
//
//stompClient.connect(
//    { Authorization: "Bearer YOUR_JWT_TOKEN" }, // Передаем токен
//    onConnected,
//    onError
//);
// фронт
}