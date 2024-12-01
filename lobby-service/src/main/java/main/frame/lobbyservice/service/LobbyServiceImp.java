package main.frame.lobbyservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import main.frame.lobbyservice.client.GameServiceClient;
import main.frame.lobbyservice.client.UserServiceClient;
import main.frame.lobbyservice.config.RabbitMQConfig;
import main.frame.lobbyservice.dto.request.JoinLobbyRequest;
import main.frame.lobbyservice.dto.request.LeaveLobbyRequest;
import main.frame.lobbyservice.dto.request.UpdatePlayerStatusRequest;
import main.frame.lobbyservice.dto.response.CreateLobbyDTO;
import main.frame.shared.dto.LobbyDTO;
import main.frame.lobbyservice.dto.response.LobbyPlayerDTO;
import main.frame.lobbyservice.model.Lobby;
import main.frame.lobbyservice.model.LobbyPlayer;
import main.frame.lobbyservice.model.LobbyStatus;
import main.frame.lobbyservice.model.LobbyUserStatus;
import main.frame.shared.dto.PlayerDTO;
import main.frame.shared.dto.UserDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LobbyServiceImp implements LobbyService{

    private final RabbitTemplate rabbitTemplate;
    private final EntityManager entityManager;
    private final UserServiceClient userServiceClient; // WebClient для запросов в UserService
    private final GameServiceClient gameServiceClient; // WebClient для запросов в UserService

//    public void addPlayerToLobby(Long lobbyId, Long playerId) {
//        LobbyPlayer lobbyPlayer = new LobbyPlayer();
//        lobbyPlayer.setLobbyId(lobbyId);
//        lobbyPlayer.setPlayerId(playerId);
//        lobbyPlayer.setStatus(LobbyUserStatus.NOT_READY); // Начальный статус
//
//        entityManager.persist(lobbyPlayer);
//    }


    // @Override
    public void notifyGameService(Long lobbyId) {
        String message = "Лобби " + lobbyId + " готово к старту!";
        // Отправка сообщения
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.LOBBY_EVENTS_ROUTING_KEY,
                message
        );
    }

//    // Отправка события о статусе лобби
//    public void notifyLobbyEvent(Long lobbyId, String status) {
//        String message = "Lobby ID: " + lobbyId + ", Status: " + status;
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.EXCHANGE,
//                RabbitMQConfig.LOBBY_EVENTS_ROUTING_KEY,
//                message
//        );
//    }
//
//    // Отправка действия игрока
//    public void notifyPlayerAction(Long lobbyId, Long playerId, String action) {
//        String message = "Player ID: " + playerId + ", Action: " + action + " in Lobby ID: " + lobbyId;
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.EXCHANGE,
//                RabbitMQConfig.PLAYER_ACTIONS_ROUTING_KEY,
//                message
//        );
//    }

    @Override
    public List<LobbyPlayerDTO> getPlayersInLobby(Long lobbyId) {
        return entityManager.createQuery(
                        "SELECT lp FROM LobbyPlayer lp WHERE lp.lobbyId = :lobbyId", LobbyPlayerDTO.class)
                .setParameter("lobbyId", lobbyId)
                .getResultList();
    }


    @Transactional
    @Override
    public void updatePlayerStatus(Long lobbyId, Long playerId, LobbyUserStatus status) {
        LobbyPlayer lobbyPlayer = entityManager.createQuery(
                        "SELECT lp FROM LobbyPlayer lp WHERE lp.lobbyId = :lobbyId AND lp.playerId = :playerId", LobbyPlayer.class)
                .setParameter("lobbyId", lobbyId)
                .setParameter("playerId", playerId)
                .getSingleResult();

        if (lobbyPlayer == null) {
            throw new IllegalArgumentException("Игрок не найден в указанном лобби.");
        }

        lobbyPlayer.setStatus(status);
        entityManager.merge(lobbyPlayer);
    }

    @Transactional
    @Override
    public LobbyPlayerDTO joinToLobby(JoinLobbyRequest request) {
        Long lobbyId = request.getLobbyId();
        Long playerId = request.getPlayerId();

        Lobby lobby = entityManager.find(Lobby.class, lobbyId);
        if (lobby == null) {
            throw new IllegalArgumentException("Лобби не найдено.");
        }

        // Проверяем статус игрока через GameServiceClient
        PlayerDTO playerDTO = gameServiceClient.getPlayerById(playerId);
        if (playerDTO == null) {
            throw new IllegalArgumentException("Игрок не найден.");
        }

        // Проверяем, что статус лобби позволяет добавление
        if (!lobby.getStatus().equals(LobbyStatus.WAITING)) {
            throw new IllegalStateException("Игроки могут быть добавлены только в ожидании начала игры.");
        }

        // Проверяем, что в лобби ещё есть место
        long currentPlayers = countPlayersInLobby(lobbyId);
        if (currentPlayers >= lobby.getMaxPlayers()) {
            throw new IllegalStateException("Лобби заполнено.");
        }

        // Проверяем, что игрок ещё не в лобби
        if (isPlayerInLobby(lobbyId, playerId)) {
            throw new IllegalStateException("Игрок уже находится в лобби.");
        }

        // Добавляем игрока
        LobbyPlayer lobbyPlayer = new LobbyPlayer();
        lobbyPlayer.setLobbyId(lobbyId);
        lobbyPlayer.setPlayerId(playerId);
        lobbyPlayer.setStatus(LobbyUserStatus.CONNECTED);
      //  lobbyPlayer.setTurnOrder(0); // Возможно переделать очередность ходов!
        entityManager.persist(lobbyPlayer);


//        Map<String, Object> message = new HashMap<>();
//        message.put("lobbyId", lobbyId);
//        message.put("email", email); // Добавляем данные пользователя
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.EXCHANGE,
//                RabbitMQConfig.LOBBY_EVENTS_ROUTING_KEY,
//                message
//        );


        // Отправляем сообщение в RabbitMQ
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.LOBBY_EVENTS_ROUTING_KEY,
                new LobbyPlayerDTO(
                        lobbyPlayer.getId(),
                        lobbyId,
                        playerId,
                        LobbyUserStatus.CONNECTED,
                        null // Очередность можно задать позже
                )
        );

        return lobbyPlayer.toLobbyPlayerDTO();
    }

    // Присоединение к лобби
    public void joinLobby(JoinLobbyRequest request) {
        // Логика добавления игрока в лобби
        System.out.println("Игрок с ID " + request.getPlayerId() + " присоединился к лобби " + request.getLobbyId());

        // Отправка сообщения через RabbitMQ
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.LOBBY_EVENTS_ROUTING_KEY,
                request
        );
    }

    // Выход из лобби
    public void leaveLobby(LeaveLobbyRequest request) {
        System.out.println("Игрок с ID " + request.getPlayerId() + " покинул лобби " + request.getLobbyId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.LOBBY_EVENTS_ROUTING_KEY,
                request
        );
    }

    // Обновление статуса игрока
    public void updatePlayerStatus(UpdatePlayerStatusRequest request) {
        System.out.println("Игрок с ID " + request.getPlayerId() + " изменил статус в лобби " + request.getLobbyId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.LOBBY_EVENTS_ROUTING_KEY,
                request
        );
    }

    public long countPlayersInLobby(Long lobbyId) {
        String query = "SELECT COUNT(lp) FROM LobbyPlayer lp WHERE lp.lobbyId = :lobbyId";
        return entityManager.createQuery(query, Long.class)
                .setParameter("lobbyId", lobbyId)
                .getSingleResult();
    }

    public boolean isPlayerInLobby(Long lobbyId, Long playerId) {
        String query = "SELECT COUNT(lp) FROM LobbyPlayer lp WHERE lp.lobbyId = :lobbyId AND lp.playerId = :playerId";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("lobbyId", lobbyId)
                .setParameter("playerId", playerId)
                .getSingleResult();
        return count > 0;
    }

    @Transactional
    @Override
    public void removePlayerFromLobby(Long lobbyId, Long playerId, Long requestorId) {
        Lobby lobby = entityManager.find(Lobby.class, lobbyId);
        //Lobby lobby = getLobbyById(lobbyId);
        if (lobby == null) {
            throw new IllegalArgumentException("Лобби не найдено.");
        }

        // Проверяем, что игрок есть в лобби
        if (!isPlayerInLobby(lobbyId, playerId)) {
            throw new IllegalArgumentException("Игрок не находится в данном лобби.");
        }

        // Если удаляет хост, он может удалить любого игрока
        if (!lobby.getHostId().equals(requestorId)) {
            if (!playerId.equals(requestorId)) {
                throw new SecurityException("Только хост может удалять других игроков.");
            }
        }

        // Удаляем игрока
        String deleteQuery = "DELETE FROM LobbyPlayer lp WHERE lp.lobbyId = :lobbyId AND lp.playerId = :playerId";
        entityManager.createQuery(deleteQuery)
                .setParameter("lobbyId", lobbyId)
                .setParameter("playerId", playerId)
                .executeUpdate();

        // Если удалённый игрок — хост, передаём права другому игроку
        if (playerId.equals(lobby.getHostId())) {
            transferHost(lobbyId);
        }

       // notifyPlayerKicked(playerId, lobbyId); // Уведомление игрока
    }

    private void transferHost(Long lobbyId) {
        String query = "SELECT lp.playerId FROM LobbyPlayer lp WHERE lp.lobbyId = :lobbyId";
        List<Long> playerIds = entityManager.createQuery(query, Long.class)
                .setParameter("lobbyId", lobbyId)
                .getResultList();

        if (playerIds.isEmpty()) {
            // Если игроков больше нет, удаляем лобби
            entityManager.remove(entityManager.find(Lobby.class, lobbyId));
        } else {
            // Назначаем нового хоста
            Lobby lobby = entityManager.find(Lobby.class, lobbyId);
            lobby.setHostId(playerIds.get(0));
            entityManager.merge(lobby);
        }
    }

    @Transactional
    @Override
    public void createLobby(CreateLobbyDTO createLobbyDTO) {
        if (createLobbyDTO.getHostId() == null) {
            throw new IllegalArgumentException("Host ID обязателен для создания лобби.");
        }

        if (createLobbyDTO.getMaxPlayers() <= 0) {
            createLobbyDTO.setMaxPlayers(6); // Устанавливаем значение по умолчанию
        }

        Lobby lobby = new Lobby();
        lobby.setName(createLobbyDTO.getName());
        lobby.setPassword(createLobbyDTO.getPassword());
        lobby.setMaxPlayers(createLobbyDTO.getMaxPlayers());
//        lobby.setGameMode(gameMode);
//        lobby.setMinRank(minRank);
//        lobby.setMaxRank(maxRank);
        lobby.setStatus(LobbyStatus.WAITING);
        lobby.setHostId(createLobbyDTO.getHostId());

        entityManager.persist(lobby);
     //   log.info("Лобби успешно создано: {}", lobby.getName());
    }

    @Transactional
    @Override
    public Lobby connectToPrivateLobby(JoinLobbyRequest request, String password) {
        Lobby lobby = entityManager.find(Lobby.class, request.getLobbyId());
        if (lobby == null) {
            throw new IllegalArgumentException("Лобби не найдено.");
        }

        if (!lobby.getPassword().equals(password)) {
            throw new SecurityException("Неверный пароль.");
        }

        joinToLobby(request);

        return lobby;
    }



   // @Transactional
    @Override
    public boolean deleteLobby(Long lobbyId) {
        // Удаляем всех игроков, связанных с лобби
        entityManager.createQuery("DELETE FROM LobbyPlayer lp WHERE lp.lobbyId = :lobbyId")
                .setParameter("lobbyId", lobbyId)
                .executeUpdate();

        // Удаляем само лобби
        Lobby lobby = entityManager.find(Lobby.class, lobbyId);
        if (lobby != null) {
            entityManager.remove(lobby);
            return true;
        } else {
          //  log.error("Ошибка! Лобби не найдено!");
            return false;
        }
    }

    @Transactional
    @Override
    public LobbyDTO updateLobby(Long lobbyId, Long hostId, String name, String password, int maxPlayers) {
        Lobby lobby = entityManager.find(Lobby.class, lobbyId);
        if (lobby == null) {
            throw new IllegalArgumentException("Лобби не найдено.");
        }

        if (!isPlayerHost(lobbyId, hostId)) {
            throw new SecurityException("Только хост может изменять параметры лобби.");
        }

        if (name != null) lobby.setName(name);
        if (password != null) lobby.setPassword(password);
        if (maxPlayers >= 2 && maxPlayers <= 6) lobby.setMaxPlayers(maxPlayers);
//        if (gameMode != null) lobby.setGameMode(gameMode);
//        if (minRank != null) lobby.setMinRank(minRank);
//        if (maxRank != null) lobby.setMaxRank(maxRank);

        entityManager.merge(lobby);
        return lobby.toLobbyDTO();
    }

//    @Override
//    public List<LobbyDTO> searchLobbies(String name) {
//        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT u FROM Lobby u WHERE 1=1");
//
//        // Добавляем условия только для ненулевых параметров
//        if (name != null && !name.isEmpty()) {
//            queryBuilder.append(" AND LOWER(u.name) LIKE :name");
//        }
//
//        TypedQuery<Lobby> query = entityManager.createQuery(queryBuilder.toString(), Lobby.class);
//
//        // Устанавливаем параметры только для ненулевых значений
//        if (name != null && !name.isEmpty()) {
//            query.setParameter("name", "%" + name.toLowerCase() + "%");
//        }
//
//        return query.getResultList().stream().map(Lobby::toLobbyDTO).toList();
//    }

//    public void inviteUserToLobby(Long lobbyId, Long userId) {
////        if (!isUserHost(lobbyId, hostId)) {
////            throw new SecurityException("Только хост может приглашать игроков в лобби.");
////        }
//
//        // Отправка приглашения игроку через RabbitMQ или REST-запрос
//        Invitation invitation = new Invitation(lobbyId, playerId);
//        rabbitTemplate.convertAndSend("lobby-invitations", invitation);
//    }

    public boolean isPlayerHost(Long lobbyId, Long userId) {
        Lobby lobby = entityManager.find(Lobby.class, lobbyId);
        if (lobby == null) {
            throw new IllegalArgumentException("Лобби не найдено.");
        }

        return lobby.getHostId().equals(userId);
    }

//    private void notifyPlayerKicked(Long userId, Long lobbyId) {
//        // Реализация уведомления игрока (например, через RabbitMQ или WebSocket)
//    }


    @Override
    public Lobby setMaxPlayers(Long lobbyId, int maxPlayers) {
        if (maxPlayers < 2 || maxPlayers > 6) {
            throw new IllegalArgumentException("Игроков может быть не меньше 2 и не больше 6.");
        }

        Lobby lobby = getLobbyById(lobbyId)
                .orElseThrow(() -> new EntityNotFoundException("Лобби с ID " + lobbyId + " не найдено."));

        lobby.setMaxPlayers(maxPlayers);
        //updateLobby(lobbyId);

        entityManager.merge(lobby);
        return lobby;
    }

//    public void recordGameStatistics(Long lobbyId, GameResult result) {
//        Lobby lobby = getLobbyById(lobbyId);
//
//        for (LobbyPlayer player : lobby.getPlayers()) {
//            GameStatistics stats = new GameStatistics(player.getUserId(), result);
//            entityManager.persist(stats);
//        }
//
//        closeLobby(lobbyId); // Закрытие лобби после завершения
//    }
//
//    public void sendMessageToLobby(Long lobbyId, ChatMessage message) {
//        Lobby lobby = getLobbyById(lobbyId);
//        message.setLobbyId(lobbyId);
//        entityManager.persist(message);
//
//        // Рассылка сообщения (RabbitMQ/WebSocket)
//        notifyLobbyUsers(lobby.getPlayers(), message);
//    }

//    private void notifyLobbyUsers(List<LobbyPlayer> players, ChatMessage message) {
//        for (LobbyPlayer player : players) {
//            // Отправка сообщения пользователю
//        }
//    }

//    // Сохранение текущего состояния лобби
//    public void saveLobbyState(Long lobbyId) {
//        Lobby lobby = getLobbyById(lobbyId);
//        LobbySnapshot snapshot = new LobbySnapshot(lobby);
//        entityManager.persist(snapshot);
//    }
//
//    public Lobby restoreLobbyState(Long snapshotId) {
//        LobbySnapshot snapshot = entityManager.find(LobbySnapshot.class, snapshotId);
//        return snapshot.toLobby();
//    }


    @Transactional
    @Override
    public void startGame(Long lobbyId, Long hostId) {
        Lobby lobby = entityManager.find(Lobby.class, lobbyId);
       // Lobby lobby = getLobbyById(lobbyId);
        if (lobby == null) {
            throw new IllegalArgumentException("Лобби не найдено.");
        }

        // Проверяем, что хост запускает игру
        if (isPlayerHost(lobbyId, hostId)) {
            throw new SecurityException("Только хост может запускать игру.");
        }

        // Проверяем, что достаточно игроков
        long playerCount = countPlayersInLobby(lobbyId);
        if (playerCount < 2) {
            throw new IllegalStateException("Для запуска игры требуется минимум 2 игрока.");
        }

        // Проверяем, что все игроки готовы
        if (!areAllPlayersReady(lobbyId)) {
            throw new IllegalStateException("Все игроки должны быть готовы для запуска игры.");
        }

        List<LobbyPlayerDTO> players = getPlayersInLobby(lobbyId);

        // Устанавливаем порядок ходов
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setTurnOrder(i);
            updateLobbyPlayer(players.get(i)); // Сохраняем изменения в БД
        }

        // Обновляем статус лобби
        if (lobby.getStatus() == LobbyStatus.WAITING) {
            lobby.setStatus(LobbyStatus.IN_GAME);

            // Отправка события в RabbitMQ
            notifyGameStarted(lobbyId);
        } else {
            throw new IllegalStateException("Cannot start the game: either not found or already started");
        }

        entityManager.merge(lobby);
    }

   // @Transactional
    @Override
    public void endTurn(Long lobbyId, Long userId) {
        List<LobbyPlayerDTO> players = getPlayersInLobby(lobbyId);

        LobbyPlayerDTO currentPlayer = players.stream()
                .filter(player -> player.getPlayerId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Игрок не найден в лобби."));

        // Проверяем, что это ход текущего игрока
        if (!currentPlayer.getTurnOrder().equals(getCurrentTurnIndex(players))) {
            throw new IllegalStateException("Сейчас не ход данного игрока.");
        }

        // Определяем следующего игрока
        int nextTurnOrder = (currentPlayer.getTurnOrder() + 1) % players.size();
        setCurrentTurnIndex(players, nextTurnOrder);

     //   notifyNextPlayerTurn(players.get(nextTurnOrder).getPlayerId(), lobbyId);
    }

    private int getCurrentTurnIndex(List<LobbyPlayerDTO> players) {
        return players.stream()
                .filter(player -> player.getStatus() == LobbyUserStatus.PLAYING)
                .mapToInt(LobbyPlayerDTO::getTurnOrder)
                .min()
                .orElseThrow(() -> new IllegalStateException("Нет активного игрока."));
    }

    private void setCurrentTurnIndex(List<LobbyPlayerDTO> players, int turnOrder) {
        players.forEach(player -> {
            if (player.getTurnOrder() == turnOrder) {
                player.setStatus(LobbyUserStatus.IN_TURN);
            } else {
                player.setStatus(LobbyUserStatus.PLAYING);
            }
            updateLobbyPlayer(player);
        });
    }

    @Override
    public void disconnectPlayerFromLobby(Long lobbyId, Long userId) {
        List<LobbyPlayerDTO> players = getPlayersInLobby(lobbyId);

        LobbyPlayerDTO disconnectedPlayer = players.stream()
                .filter(player -> player.getPlayerId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Игрок не найден в лобби."));

        players.remove(disconnectedPlayer);

        if (disconnectedPlayer.getStatus() == LobbyUserStatus.IN_TURN) {
            endTurn(lobbyId, userId); // Завершаем ход отключенного игрока
        }

        if (players.size() < 2) {
            //closeLobby(lobbyId); // Закрываем лобби, если игроков меньше двух
            deleteLobby(lobbyId);
        }
    }



    //@Transactional
    public void updateLobbyPlayer(LobbyPlayerDTO lobbyPlayer) {
        entityManager.merge(lobbyPlayer);
    }




//    private boolean areAllPlayersReady(Long lobbyId) {
//        String query = "SELECT COUNT(lp) FROM LobbyPlayer lp WHERE lp.lobbyId = :lobbyId AND lp.status != :status";
//        Long count = entityManager.createQuery(query, Long.class)
//                .setParameter("lobbyId", lobbyId)
//                .setParameter("status", LobbyUserStatus.READY)
//                .getSingleResult();
//        return count == 0;
//    }

    // Метод, который проверяет, что все игроки в лобби готовы, например, перед началом игры.
    public boolean areAllPlayersReady(Long lobbyId) {
        List<LobbyPlayerDTO> playersInLobby = getPlayersInLobby(lobbyId);
        if (playersInLobby.isEmpty()) {
            return false; // Лобби пустое, никто не готов
        }

        return playersInLobby.stream()
                .allMatch(player -> player.getStatus() == LobbyUserStatus.READY);
    }

//    private void notifyGameServiceAboutStart(Long lobbyId) {
//        // Логика отправки сообщения в GameService
//        gameServiceClient.startGame(lobbyId);
//    }

//    @Transactional
//    public void cleanupInactiveLobbies() {
//        String query = "DELETE FROM Lobby l WHERE l.status = :status AND l.updatedAt < :threshold";
//        LocalDateTime threshold = LocalDateTime.now().minusHours(1); // Лобби неактивны более часа
//
//        entityManager.createQuery(query)
//                .setParameter("status", LobbyStatus.WAITING)
//                .setParameter("threshold", threshold)
//                .executeUpdate();
//    }

//    @Transactional
//    public void inviteUserToLobby(Long lobbyId, Long userId) {
//        Lobby lobby = entityManager.find(Lobby.class, lobbyId);
//        if (lobby == null) {
//            throw new IllegalArgumentException("Лобби не найдено.");
//        }
//
//        if (lobby.getInvitedUsers().contains(userId)) {
//            throw new IllegalArgumentException("Пользователь уже приглашён.");
//        }
//
//        lobby.getInvitedUsers().add(userId);
//        entityManager.merge(lobby);
//    }
//
//    @Transactional
//    public Lobby acceptInvitation(Long lobbyId, Long userId) {
//        Lobby lobby = entityManager.find(Lobby.class, lobbyId);
//        if (lobby == null) {
//            throw new IllegalArgumentException("Лобби не найдено.");
//        }
//
//        if (!lobby.getInvitedUsers().contains(userId)) {
//            throw new SecurityException("Пользователь не приглашён в это лобби.");
//        }
//
//        addPlayerToLobby(lobbyId, userId);
//
//        // Удаляем из списка приглашённых
//        lobby.getInvitedUsers().remove(userId);
//        entityManager.merge(lobby);
//
//        return lobby;
//    }

    @Override
    public List<LobbyDTO> getAllLobbies(LobbyStatus status) {
        if (status != null) {
            return entityManager.createQuery("SELECT l FROM Lobby l WHERE l.status = :status", LobbyDTO.class)
                    .setParameter("status", status)
                    .getResultList();
        } else {
            return entityManager.createQuery("SELECT l FROM Lobby l", LobbyDTO.class)
                    .getResultList();
        }
//        return entityManager.createQuery("SELECT u FROM User u", User.class)
//                .getResultList().stream()
//                .map(User::toUserDTO)
//                .sorted(Comparator.comparing(UserDTO::getId))
//                .collect(Collectors.toList());
    }


    @Override
    public Optional<LobbyDTO> getLobbyById(Long lobbyId) {
        Lobby lobby = entityManager.find(Lobby.class, lobbyId);
        if (lobby == null) {
            throw new IllegalArgumentException("Лобби не найдено.");
        }

        return Optional.of(lobby.toLobbyDTO());
    }

    public List<LobbyDTO> filterLobbies(Optional<Integer> minPlayers, Optional<Integer> maxPlayers, Optional<String> gameMode) {
        StringBuilder queryBuilder = new StringBuilder("SELECT l FROM Lobby l WHERE l.status = :status");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", LobbyStatus.WAITING);

        // Добавляем фильтры
        if (minPlayers.isPresent()) {
            queryBuilder.append(" AND (SELECT COUNT(lp) FROM LobbyPlayer lp WHERE lp.lobbyId = l.id) >= :minPlayers");
            parameters.put("minPlayers", minPlayers.get());
        }
        if (maxPlayers.isPresent()) {
            queryBuilder.append(" AND (SELECT COUNT(lp) FROM LobbyPlayer lp WHERE lp.lobbyId = l.id) <= :maxPlayers");
            parameters.put("maxPlayers", maxPlayers.get());
        }
        if (gameMode.isPresent()) {
            queryBuilder.append(" AND l.gameMode = :gameMode");
            parameters.put("gameMode", gameMode.get());
        }

        TypedQuery<Lobby> query = entityManager.createQuery(queryBuilder.toString(), Lobby.class);
        parameters.forEach(query::setParameter);

        return query.getResultList()
                .stream()
                .map(Lobby::toLobbyDTO)
                .collect(Collectors.toList());
    }

    private void notifyGameStarted(Long lobbyId) {
        rabbitTemplate.convertAndSend("lobby.exchange", "lobby.started", "Game started in lobby " + lobbyId);
    }
}


