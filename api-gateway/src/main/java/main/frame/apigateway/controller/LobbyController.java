package main.frame.apigateway.controller;


import lombok.AllArgsConstructor;
import main.frame.apigateway.client.AuthServiceClient;
import main.frame.apigateway.client.LobbyServiceClient;
import main.frame.apigateway.dto.request.CreateLobbyRequest;
import main.frame.apigateway.dto.request.JoinLobbyRequest;
import main.frame.apigateway.dto.request.LoginRequest;
import main.frame.apigateway.dto.request.RegisterRequest;
import main.frame.shared.dto.UserDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/lobby")
@AllArgsConstructor
public class LobbyController {
    private final LobbyServiceClient lobbyServiceClient;

    @PostMapping("/create")
    public Mono<ResponseEntity<String>> createLobby(@RequestBody CreateLobbyRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("Отправляем запрос в LobbyService для создания лобби.");

        return lobbyServiceClient.createLobby(request, token)
                .then(Mono.just(ResponseEntity.created(URI.create("/lobby/create")).body("Лобби успешно создано."))) // Возвращаем успешный ответ
                .doOnSuccess(response -> System.out.println("Ответ успешно обработан: " + response.getBody()))
                .doOnError(e -> System.err.println("Ошибка при создании лобби: " + e.getMessage()))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при создании лобби."))); // Обработка ошибок
    }

    @PostMapping("/{lobbyId}/join")
    public Mono<ResponseEntity<String>> joinLobby(
            @PathVariable Long lobbyId,
            @RequestBody JoinLobbyRequest joinLobbyRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        System.out.println("Отправляем запрос в LobbyService для входа в лобби с ID: " + lobbyId);
        joinLobbyRequest.setLobbyId(lobbyId); // Устанавливаем ID лобби из URL в запрос

        return lobbyServiceClient.joinLobby(joinLobbyRequest, token)
                .then(Mono.just(ResponseEntity.ok("Вы успешно вошли в лобби."))) // Успешный ответ
                .doOnSuccess(response -> System.out.println("Ответ успешно обработан: " + response.getBody()))
                .doOnError(e -> System.err.println("Ошибка при входе в лобби: " + e.getMessage()))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при входе в лобби."))); // Обработка ошибок
    }
}
