package main.frame.gameservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.frame.gameservice.utils.JwtUtil;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

//@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

//@Override
//public void doFilterInternal(@Nullable HttpServletRequest request,
//                             @Nullable HttpServletResponse response,
//                             @Nullable FilterChain filterChain)
//        throws ServletException, IOException {
//    // Логируем все заголовки
//// Проверка на null перед выполнением
//    if (request == null || response == null || filterChain == null) {
//        System.out.println("Некорректный запрос: request, response или filterChain == null");
//        return;
//    }
//
//    System.out.println("Фильтр вызван для URI: " + request.getRequestURI());
//
//    Enumeration<String> headerNames = request.getHeaderNames();
//    if (headerNames != null) {
//        while (headerNames.hasMoreElements()) {
//            String header = headerNames.nextElement();
//            System.out.println("Header: " + header + " -> " + request.getHeader(header));
//        }
//    }
//
//    final String authorizationHeader = request.getHeader("Authorization");
//
//    String email = null;
//    String jwt = null;
//
//    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//        jwt = authorizationHeader.substring(7);
//        email = jwtUtil.extractEmail(jwt);
//        System.out.println("JWT: " + jwt);  // Добавь логирование
//        System.out.println("Email из токена: " + email);
//    } else {
//        System.out.println("Authorization header не найден или неправильного формата");
//    }
//
//    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//      //  UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
//        List<String> roles = jwtUtil.extractRoles(jwt);
//        System.out.println("UserDetails загружен для: " + email);
//        if (jwtUtil.validateToken(jwt)) {
//            System.out.println("Токен валиден для пользователя: " + email);
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                    new UsernamePasswordAuthenticationToken(email, null, roles);
//            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//        } else {
//            System.out.println("Токен недействителен для пользователя: " + email);
//        }
//    }
//    filterChain.doFilter(request, response);
//}

    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request,
                                    @Nullable HttpServletResponse response,
                                    @Nullable FilterChain filterChain)
            throws ServletException, IOException {
        // Проверка на null перед выполнением
        if (request == null || response == null || filterChain == null) {
            System.out.println("Некорректный запрос: request, response или filterChain == null");
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            email = jwtUtil.extractEmail(jwt);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt)) {
                List<String> roles = jwtUtil.extractRoles(jwt);

                // Преобразование ролей в SimpleGrantedAuthority
                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}