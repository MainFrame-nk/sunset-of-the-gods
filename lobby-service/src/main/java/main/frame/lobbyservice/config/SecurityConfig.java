package main.frame.lobbyservice.config;

import lombok.AllArgsConstructor;
import main.frame.lobbyservice.filter.JwtRequestFilter;
import main.frame.lobbyservice.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
   // private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Отключаем CSRF для REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws/**").permitAll() // Игнорируем WebSocket-эндпоинты
                        .requestMatchers("/lobby/**").hasAuthority("ROLE_USER") // Пример: только пользователи могут создавать лобби
                       // .requestMatchers("/lobby/create").hasAuthority("ROLE_USER") // Пример: только пользователи могут создавать лобби
                        .requestMatchers("/static/**").permitAll()
                      //  .requestMatchers("/lobby/admin").hasAuthority("ROLE_ADMIN") // Пример: доступ только админам
                        .anyRequest().authenticated()
                   //     .requestMatchers("/lobby/**").permitAll()  // Регистрация доступна без токена
                       // .anyRequest().hasAuthority("ROLE_ADMIN")
                       // .anyRequest().authenticated()  // Все остальные запросы требуют токена
                )
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil);  // Передаем JwtUtil для проверки токенов
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("*"));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//        configuration.setExposedHeaders(List.of("Authorization"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

}