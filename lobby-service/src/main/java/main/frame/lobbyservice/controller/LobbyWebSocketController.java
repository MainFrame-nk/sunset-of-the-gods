package main.frame.lobbyservice.controller;

import main.frame.lobbyservice.dto.request.JoinLobbyRequest;
import main.frame.lobbyservice.dto.response.LobbyPlayerDTO;
import main.frame.lobbyservice.dto.response.LobbyStatusUpdate;
import main.frame.lobbyservice.service.LobbyService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MessageMapping("/lobby") // Общий префикс для обработки сообщений
public class LobbyWebSocketController {

    private final LobbyService lobbyService;
    private final SimpMessagingTemplate messagingTemplate;

    public LobbyWebSocketController(LobbyService lobbyService, SimpMessagingTemplate messagingTemplate) {
        this.lobbyService = lobbyService;
        this.messagingTemplate = messagingTemplate;
    }

    // Клиент отправляет действие через WebSocket
//    @MessageMapping("/join")
//    public void handleJoinLobby(@Payload JoinLobbyRequest request,
//                                @Header("simpSessionId") String sessionId) {
//        System.out.println("Игрок присоединился к лобби: " + request);
//
//        // Логика обработки (например, добавление игрока в лобби)
//        lobbyService.joinToLobby(request.getLobbyId(), request.getPlayerId());
//
//        // Если нужно, можно сразу ответить клиенту
//        messagingTemplate.convertAndSend(
//                "/topic/lobby/" + request.getLobbyId(),
//                "Игрок с ID " + request.getPlayerId() + " присоединился"
//        );
//    }
    @MessageMapping("/join")
    public void handleJoinLobby(@Payload JoinLobbyRequest request,
                                @Header("simpSessionId") String sessionId) {
        try {
            // Добавляем игрока в лобби через сервис
            LobbyPlayerDTO addedPlayer = lobbyService.joinToLobby(request);

            // Отправляем обновление всем игрокам в лобби через WebSocket
            messagingTemplate.convertAndSend(
                    "/topic/lobby/" + request.getLobbyId(),
                    "Игрок с ID " + addedPlayer.getPlayerId() + " присоединился"
            );

        } catch (IllegalArgumentException | IllegalStateException ex) {
            // Отправляем клиенту ошибку, если что-то пошло не так
            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/errors",
                    ex.getMessage()
            );
        }
    }

    @MessageMapping("/lobby/status")
    @SendTo("/topic/lobby")
    public LobbyStatusUpdate updateLobbyStatus(LobbyStatusUpdate statusUpdate) {
        return statusUpdate; // Рассылаем обновления статуса лобби
    }
}
