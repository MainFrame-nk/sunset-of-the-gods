package main.frame.apigateway.filter;

//import main.frame.apigat.client.AuthServiceClient;
import main.frame.apigateway.utils.JwtUtil;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
//import main.frame.shared.JwtUtil;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;


@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Пропускаем маршруты для логина и регистрации
        if (exchange.getRequest().getURI().getPath().startsWith("/auth/login") ||
                exchange.getRequest().getURI().getPath().startsWith("/auth/register")) {
            return chain.filter(exchange);
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // Если заголовка нет или он неверный, возвращаем 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); // Завершаем обработку
        }

        String token = authorizationHeader.substring(7); // Извлекаем токен
        System.out.println("Authorization Header передан: " + authorizationHeader);
        // Если токен валиден, извлекаем имя пользователя и устанавливаем аутентификацию
        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            System.out.println("Аутентификация установлена!");
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
        }

        // Если токен не валиден, возвращаем 401
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
