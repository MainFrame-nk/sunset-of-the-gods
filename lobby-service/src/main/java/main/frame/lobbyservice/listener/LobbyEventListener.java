package main.frame.lobbyservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.frame.lobbyservice.config.RabbitMQConfig;
import main.frame.lobbyservice.dto.response.LobbyPlayerDTO;
import main.frame.lobbyservice.model.Lobby;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class LobbyEventListener {
    private final SimpMessagingTemplate messagingTemplate;

    public LobbyEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

//    @RabbitListener(queues = RabbitMQConfig.LOBBY_QUEUE)
//    public void handleLobbyEvent(String message) throws JsonProcessingException {
//        System.out.println("Получено сообщение из RabbitMQ: " + message);
//
////        // Пример: парсим сообщение, чтобы отправить данные конкретному лобби
////        LobbyUpdate lobbyUpdate = parseMessage(message);
////
////        // Уведомляем игроков в лобби через WebSocket
////        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyUpdate.getLobbyId(), lobbyUpdate);
//
//        // Например, преобразуем сообщение в объект Lobby
//        Lobby lobby = new ObjectMapper().readValue(message, Lobby.class);
//
//        // Уведомляем всех подписчиков
//        messagingTemplate.convertAndSend("/topic/lobby/" + lobby.getId(), lobby);
//    }

    @RabbitListener(queues = RabbitMQConfig.LOBBY_EVENTS_QUEUE)
    public void handleLobbyEvent(String message) {
        try {
            // Десериализация сообщения
            LobbyPlayerDTO lobbyPlayer = new ObjectMapper().readValue(message, LobbyPlayerDTO.class);

            // Уведомляем игроков в лобби о событии
            messagingTemplate.convertAndSend(
                    "/topic/lobby/" + lobbyPlayer.getLobbyId(),
                    "Игрок с ID " + lobbyPlayer.getPlayerId() + " изменил статус на " + lobbyPlayer.getStatus()
            );

        } catch (JsonProcessingException e) {
            System.err.println("Ошибка обработки сообщения RabbitMQ: " + e.getMessage());
        }
    }

//    private LobbyUpdate parseMessage(String message) throws JsonProcessingException {
//        // Логика парсинга сообщения из RabbitMQ
//        // Например, преобразование JSON в объект LobbyUpdate
//        return new ObjectMapper().readValue(message, LobbyUpdate.class);
//    }

    @RabbitListener(queues = RabbitMQConfig.LOBBY_QUEUE)
    public void handleMessage(String message) {
        System.out.println("Сообщение получено: " + message);

        // Пример: извлечение данных из сообщения
        if (message.contains("player_ready")) {
            Long playerId = extractPlayerId(message);
            markPlayerAsReady(playerId);
        } else if (message.contains("request_lobby_state")) {
            Long lobbyId = extractLobbyId(message);
            sendLobbyState(lobbyId);
        }
    }

    private Long extractPlayerId(String message) {
        // Извлечение ID игрока из сообщения
        return Long.parseLong(message.split(":")[1]);
    }

    private Long extractLobbyId(String message) {
        // Извлечение ID лобби из сообщения
        return Long.parseLong(message.split(":")[1]);
    }

    private void markPlayerAsReady(Long playerId) {
        System.out.println("Marking player as ready: " + playerId);
        // Логика пометки игрока как готового в базе данных
    }

    private void sendLobbyState(Long lobbyId) {
        System.out.println("Sending lobby state for lobby: " + lobbyId);
        // Логика отправки состояния лобби
    }

//    // Обработка событий лобби
//    @RabbitListener(queues = RabbitMQConfig.LOBBY_EVENTS_QUEUE)
//    public void handleLobbyEvents(String message) {
//        System.out.println("Lobby Event Received: " + message);
//        // Логика обработки событий лобби
//    }

//    @RabbitListener(queues = RabbitMQConfig.LOBBY_EVENTS_QUEUE)
//    public void handleLobbyEvent(String message) {
//        System.out.println("Сообщение из RabbitMQ: " + message);
//
//        // TODO: Здесь можно добавить логику (например, обновить состояние в базе данных)
//    }
//@RabbitListener(queues = RabbitMQConfig.LOBBY_EVENTS_QUEUE)
//public void handleLobbyEvent(String message) {
//    try {
//        JoinLobbyRequest request = objectMapper.readValue(message, JoinLobbyRequest.class);
//        System.out.println("Обработано сообщение: " + request);
//    } catch (JsonProcessingException e) {
//        System.out.println("Ошибка при десериализации сообщения: " + e.getMessage());
//    }
//}

//
//    // Обработка действий игроков
//    @RabbitListener(queues = RabbitMQConfig.PLAYER_ACTIONS_QUEUE)
//    public void handlePlayerActions(String message) {
//        System.out.println("Player Action Received: " + message);
//        // Логика обработки действий игроков
//    }
}
