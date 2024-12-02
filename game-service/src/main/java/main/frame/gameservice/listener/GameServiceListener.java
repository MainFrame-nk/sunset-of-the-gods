package main.frame.gameservice.listener;

import main.frame.gameservice.config.RabbitMQConfig;
import main.frame.gameservice.dto.PlayerDTO;
import main.frame.gameservice.model.session.GameSession;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameServiceListener {

    @RabbitListener(queues = RabbitMQConfig.LOBBY_EVENTS_QUEUE)
    public void handleLobbyEvent(String message) {
        System.out.println("GameService получил событие лобби: " + message);
        // Логика обработки события лобби

        // Разбираем сообщение (например, получаем ID лобби)
        Long lobbyId = extractLobbyIdFromMessage(message);

        if (message.contains("готово")) {
            startGameSession(lobbyId);
        }
    }

    private Long extractLobbyIdFromMessage(String message) {
        // Пример извлечения ID лобби из сообщения
        return Long.parseLong(message.split(" ")[1]);
    }

    private void startGameSession(Long lobbyId) {
        System.out.println("Старт игровой сессии: " + lobbyId);

        // Получаем игроков из лобби через базу данных или другой сервис
        List<PlayerDTO> players = getPlayersFromLobby(lobbyId);

        // Генерация начального состояния игры (раздача карт, начальные параметры)
        GameSession gameSession = new GameSession();
        gameSession.setLobbyId(lobbyId);
        gameSession.setPlayers(players);
        gameSession.initialize(); // Раздача карт и настройка параметров

        // Сохраняем сессию в базе данных
        saveGameSession(gameSession);

        // Уведомляем игроков через WebSocket или другой механизм
        notifyPlayersAboutGameStart(players, gameSession);
    }

    private List<PlayerDTO> getPlayersFromLobby(Long lobbyId) {
        // Пример: запрос через WebClient или базу данных
        return List.of(new PlayerDTO(1L), new PlayerDTO(2L)); // Заглушка
    }

    private void saveGameSession(GameSession gameSession) {
        // Сохранение в базу данных через EntityManager или репозиторий
        System.out.println("Game session saved: " + gameSession);
    }

    private void notifyPlayersAboutGameStart(List<PlayerDTO> players, GameSession gameSession) {
        for (PlayerDTO player : players) {
            System.out.println("Notifying player: " + player.getId());
            // Логика уведомления игрока
        }
    }
}
