package main.frame.gameservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.frame.gameservice.client.LobbyServiceClient;
import main.frame.gameservice.dto.LobbyPlayerCardsDTO;
import main.frame.gameservice.model.cardconfig.RestrictedCard;
import main.frame.gameservice.model.characters.CharacterCard;
import main.frame.gameservice.model.player.ActionType;
import main.frame.gameservice.model.player.LobbyPlayerCards;
import main.frame.gameservice.model.player.Player;
import main.frame.gameservice.model.player.PlayerAction;
import main.frame.gameservice.model.session.GamePhase;
import main.frame.gameservice.model.session.GameSessionDTO;
import main.frame.gameservice.model.session.GameSession;
import main.frame.shared.dto.LobbyDTO;
import main.frame.shared.dto.PlayerDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImp implements GameService {
    @PersistenceContext
    private EntityManager entityManager;
    private final LobbyServiceClient lobbyServiceClient;
    private final PlayerService playerService;

//    public boolean tryUseCard(Player player, Card card) {
//        if (player.canUseCard(card, cardService)) {
//            // Логика для использования карты
//            return true;
//        } else {
//            // Логика для ошибки, если карта не может быть использована
//            return false;
//        }
//    }

//    public void initializeGameSession(Long lobbyId, List<Long> playerIds) {
//        GameSessionEntity session = new GameSessionEntity();
//        session.setLobbyId(lobbyId);
//
//        // Установка начальной очередности игроков
//        List<Long> shuffledOrder = new ArrayList<>(playerIds);
//        Collections.shuffle(shuffledOrder);
//        session.setTurnOrder(shuffledOrder);
//
//        session.setActivePlayerIndex(0); // Первый игрок начинает
//        session.setTurnNumber(1); // Первый ход
//        session.setPhase(GamePhase.START);
//
//        gameSessionRepository.save(session);
//    }

//    public void nextTurn(Long sessionId) {
//        GameSessionEntity session = gameSessionRepository.findById(sessionId)
//                .orElseThrow(() -> new IllegalArgumentException("Сессия не найдена"));
//
//        // Переход к следующему игроку
//        int currentIndex = session.getActivePlayerIndex();
//        int nextIndex = (currentIndex + 1) % session.getTurnOrder().size();
//
//        session.setActivePlayerIndex(nextIndex);
//        session.setTurnNumber(session.getTurnNumber() + 1);
//
//        // Установка фазы игры
//        session.setPhase(GamePhase.PLAY);
//
//        gameSessionRepository.save(session);
//
//        // Уведомление игроков через WebSocket
//        notifyPlayersAboutTurnChange(session);
//    }

  //  @Transactional
    @Override
    public void nextTurn(Long gameSessionId) {
        GameSession session = entityManager.find(GameSession.class, gameSessionId);
        if (session == null) {
            throw new EntityNotFoundException("Игровая сессия не найдена.");
        }

        // Получаем текущего игрока
        Long currentPlayerId = session.getActivePlayerId();
        List<Long> playerIds = new ArrayList<>(session.getPlayerHands().keySet());

        // Определяем следующего игрока
        int currentIndex = playerIds.indexOf(currentPlayerId);
        int nextIndex = (currentIndex + 1) % playerIds.size();
        session.setActivePlayerId(playerIds.get(nextIndex));

        // Обновляем фазу и номер хода
        session.setTurnNumber(session.getTurnNumber() + 1);
        session.setPhase(GamePhase.PLAY);

        entityManager.merge(session);
    }



    public boolean canPlayerUseCard(LobbyPlayerCards player, RestrictedCard card) {
        Set<String> playerClasses = player.getCharacterClasses()
                .stream()
                .map(CharacterCard::getName)
                .collect(Collectors.toSet());

        // Проверяем "разрешённые" классы
        if (!card.getAllowedClasses().isEmpty() &&
                Collections.disjoint(playerClasses, card.getAllowedClasses())) {
            return false; // Игрок не может использовать карту
        }

        // Проверяем "запрещённые" классы
        if (!card.getRestrictedClasses().isEmpty() &&
                !Collections.disjoint(playerClasses, card.getRestrictedClasses())) {
            return false; // Игрок не может использовать карту
        }

        return true; // Ограничений нет
    }

//    public void applyEffect(Effect effect, Player player) {
//        switch (effect.getEffectType()) {
//            case INCREASE_DAMAGE:
//                player.setDamage(player.getDamage() + effect.getValue());
//                break;
//            case HEAL:
//                player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + effect.getValue()));
//                break;
//            case GAIN_LEVEL:
//                player.setLevel(player.getLevel() + 1);
//                break;
//            case IMMUNITY:
//                player.addStatus("IMMUNE");
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown effect type: " + effect.getEffectType());
//        }
//    }

    @Override
    public GameSessionDTO startGameSession(Long lobbyId) {
        // Получение данных о лобби
        LobbyDTO lobby = lobbyServiceClient.getLobbyById(lobbyId);
        List<PlayerDTO> players = lobbyServiceClient.getPlayersInLobby(lobbyId).stream()
                .map(player -> PlayerDTO.builder()
                        .id(player.getId())
                        .userId(null) // Если `userId` не требуется, можно оставить null
                      //  .level(0) // Уровень не указан в LobbyService
                        .build())
                .toList();

        if (players.isEmpty()) {
            throw new IllegalArgumentException("В лобби нет игроков: " + lobbyId);
        }

        // Создание игровой сессии
        GameSessionDTO session = new GameSessionDTO();
        session.setLobbyId(lobbyId);
        session.setPlayers(players);
        session.initialize(); // Настройка начального состояния игры

        // Сохранение игровой сессии
        saveGameSession(session);

        return session;
    }

   // @Transactional
    @Override
    public void saveGameSession(GameSessionDTO session) {
        // Преобразование GameSession в GameSessionEntity
        GameSession entity = GameSession.builder()
                .lobbyId(session.getLobbyId())
                .playerHands(session.getPlayerHands())
              //  .deck(session.getDeck().stream().map(Card::getId).toList()) // Конвертируем карты в ID
                .deck(session.getDeck())
               // .discardPile(session.getDiscardPile().stream().map(Card::getId).toList()) // Тоже ID
                .discardPile(session.getDiscardPile())
                .phase(session.getPhase())
                .activePlayerId(session.getActivePlayerId())
                .turnNumber(session.getTurnNumber())
                .build();

        // Сохранение через EntityManager
        entityManager.persist(entity);
    }


//    public GameSession startGameSession(Long lobbyId) {
//        LobbyDTO lobby = lobbyServiceClient.getLobbyById(lobbyId)
//                .orElseThrow(() -> new IllegalArgumentException("Лобби не найдено!"));
//
//        GameSession session = new GameSession();
//        session.setLobbyId(lobbyId);
//        session.setPlayers(lobby.getPlayers());
//        session.initialize(); // Настройка начального состояния игры
//
//        saveGameSession(session); // Сохранение сессии
//        return session;
//    }
//
//    public void saveGameSession(GameSession session) {
//        GameSessionEntity entity = new GameSessionEntity();
//        entity.setLobbyId(session.getLobbyId());
//        entity.setPlayers(session.getPlayers());
//        entity.setPhase(session.getPhase());
//        entity.setDeck(session.getDeck());
//        entityManager.persist(entity);
//    }

    public void processPlayerAction(Long sessionId, PlayerAction action) {
        GameSessionDTO session = getGameSessionById(sessionId);

        switch (action.getType()) {
            case PLAY_CARD:
                playCard(session, action.getPlayerId(), action.getCardId());
                break;

            case DISCARD_CARD:
                discardCard(session, action.getPlayerId(), action.getCardId());
                break;

            case END_TURN:
                session.nextTurn();
                break;

            case ATTACK:
                attackTarget(session, action.getPlayerId(), action.getTargetPlayerId());
                break;

            case USE_ABILITY:
                useAbility(session, action.getPlayerId(), action.getCardId());
                break;

            default:
                throw new IllegalArgumentException("Неизвестный тип действия: " + action.getType());
        }

        saveGameSession(session); // Сохранить обновлённое состояние сессии
    }

//    public void processPlayerAction(Long gameSessionId, Long playerId, PlayerAction action) {
//        GameSessionEntity gameSession = getGameSessionEntityById(gameSessionId);
//        validateAction(gameSession, playerId, action);
//
//        // Применение действия
//        switch (action.getType()) {
//            case PLAY_CARD:
//                playCard(gameSession, playerId, action.getCardId());
//                break;
//            case ATTACK:
//                performAttack(gameSession, playerId, action.getTargetId());
//                break;
//            case SKIP_TURN:
//                skipTurn(gameSession, playerId);
//                break;
//            default:
//                throw new IllegalArgumentException("Неизвестный тип действия: " + action.getType());
//        }
//
//        // Сохранение изменений
//        saveGameSession(gameSession);
//
//        // Уведомляем игроков
//        notifyPlayersAboutAction(gameSession, playerId, action);
//    }

    private void validateAction(GameSession session, Long playerId, PlayerAction action) {
        // Проверка, участвует ли игрок в сессии
        boolean isPlayerInSession = session.getPlayers().stream()
                .anyMatch(player -> player.getId().equals(playerId));

        if (!isPlayerInSession) {
            throw new IllegalArgumentException("Игрок не участвует в этой сессии!");
        }

        // Проверка, чей сейчас ход
        if (!session.getActivePlayerId().equals(playerId)) {
            throw new IllegalStateException("Сейчас не ход этого игрока!");
        }

        // Проверка, может ли игрок разыграть карту
        if (action.getType() == ActionType.PLAY_CARD &&
                !session.getDeck().contains(action.getCardId())) {
            throw new IllegalArgumentException("Игрок не может разыграть эту карту!");
        }
    }



    public void playCard(GameSessionDTO session, Long playerId, Long cardId) {
        // Логика разыгрывания карты
        System.out.println("Игрок " + playerId + " разыгрывает карту " + cardId);
        // Убрать карту из руки игрока и выполнить эффект карты
    }

//    private void playCard(GameSessionEntity session, Long playerId, Long cardId) {
//        // Удаляем карту из руки игрока
//        session.getDeck().remove(cardId);
//        // Добавляем карту в сброс
//        session.getDiscardPile().add(cardId);
//
//        // Влияние карты на сессию
//        applyCardEffect(session, playerId, cardId);
//    }


    public void discardCard(GameSessionDTO session, Long playerId, Long cardId) {
        // Логика сброса карты
        System.out.println("Игрок " + playerId + " сбрасывает карту " + cardId);
        // Убрать карту из руки и добавить в сброс
    }

    private void attackTarget(GameSessionDTO session, Long playerId, Long targetPlayerId) {
        // Логика атаки
        System.out.println("Игрок " + playerId + " атакует игрока " + targetPlayerId);
    }

//    private void performAttack(GameSessionEntity session, Long playerId, Long targetId) {
//        // Логика атаки между игроками
//        System.out.printf("Игрок %d атакует игрока %d!%n", playerId, targetId);
//
//        // Пример: снимаем уровень у цели
//        decreasePlayerLevel(session, targetId);
//
//        // Условие победы
//        if (checkWinCondition(session, playerId)) {
//            endGame(session, playerId);
//        }
//    }


    private void useAbility(GameSessionDTO session, Long playerId, Long abilityId) {
        // Логика использования способности
        System.out.println("Игрок " + playerId + " использует способность " + abilityId);
    }

    public GameSessionDTO getGameSessionById(Long sessionId) {
        GameSession entity = entityManager.find(GameSession.class, sessionId);
        return toGameSessionDTO(entity);
    }

    public GameSession getGameSessionEntityById(Long sessionId) {
        return entityManager.find(GameSession.class, sessionId);
    }

//    public void saveGameSession(GameSession session) {
//        GameSessionEntity entity = mapSessionToEntity(session);
//        entityManager.merge(entity);
//    }

    public void nextPhase(Long gameSessionId) {
        GameSession session = getGameSessionEntityById(gameSessionId);

        switch (session.getPhase()) {
            case PREPARATION:
                session.setPhase(GamePhase.PLAY);
                break;
            case PLAY:
                session.setPhase(GamePhase.BATTLE);
                break;
            case BATTLE:
                session.setPhase(GamePhase.LOOT);
                break;
            case LOOT:
                session.setPhase(GamePhase.END_TURN);
                break;
            case END_TURN:
              //  startNextTurn(session);
                session.setPhase(GamePhase.PLAY);
                nextTurn(session.getId());
                break;
            default:
                throw new IllegalStateException("Неизвестная фаза: " + session.getPhase());
        }


        saveGameSessionEntity(session);

      //  notifyPlayersAboutPhaseChange(session);
    }

    public void saveGameSessionEntity(GameSession entity) {
        if (entity.getId() == null) {
            // Новая сущность — сохраняем
            entityManager.persist(entity);
        } else {
            // Существующая сущность — обновляем
            entityManager.merge(entity);
        }
    }


    private boolean checkWinCondition(GameSession session, Long playerId) {
        return playerService.getPlayerDeckById(playerId)
                .map(LobbyPlayerCardsDTO::getLevel) // Получаем уровень игрока
                .filter(level -> level >= 10) // Проверяем, достиг ли уровень 10
                .isPresent(); // Если фильтр прошёл, возвращаем true
    }

//    Если победные условия зависят от других параметров (например, наличие эффектов или определённых карт),
//    их можно учитывать, добавив логику в метод filter или отдельный сервис для проверки условий.

    @Override
    public void endGame(GameSession session, Long winnerId) {
        // Завершаем сессию
        session.setActive(false);

        // Уведомляем игроков и обрабатываем победителя
        if (winnerId != null) {
            handleWinnerRewards(winnerId); // Начисление наград победителю
    //        notifyPlayersAboutGameEnd(session, winnerId); // Уведомление игроков с именем победителя
        } else {
    //        notifyPlayersAboutGameEnd(session, null); // Уведомление без победителя
        }

//        // Преобразуем GameSession в GameSessionEntity
//        GameSessionEntity entity = mapToEntity(session);

        // Сохраняем сессию без winnerId
        saveGameSessionEntity(session);
    }

//    private void notifyPlayersAboutGameEnd(GameSessionEntity session, Long winnerId) {
//        String message;
//        if (winnerId != null) {
//            String winnerName = playerService.getById(winnerId)
//                    .map(PlayerDTO::getName)
//                    .orElse("Неизвестный игрок");
//            message = "Игра завершена! Победитель: " + winnerName;
//        } else {
//            message = "Игра завершена!";
//        }
//
//        // Уведомление игроков через WebSocket или другой механизм
//        notificationService.notifyPlayers(session.getLobbyId(), message);
//    }


    private void handleWinnerRewards(Long winnerId) {
        Optional<PlayerDTO> winner = playerService.getById(winnerId);
        if (winner.isPresent()) {
            PlayerDTO player = winner.get();
          //  int currentLevel = player.getLevel();
         //   player.setLevel(currentLevel + 1); // Пример: повышение уровня победителю
            // Дополнительная логика наград

            // Сохранение обновлённого игрока
        //    playerService.save(player);
        } else {
            throw new IllegalStateException("Победитель с ID " + winnerId + " не найден!");
        }
    }



    private GameSessionDTO toGameSessionDTO(GameSession entity) {
        List<PlayerDTO> players = entity.getPlayers().stream()
                .map(player -> playerService.getById(player.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Игрок с ID " + player.getId() + " не найден")))
                .toList();

        return GameSessionDTO.builder()
                .id(entity.getId())
                .lobbyId(entity.getLobbyId())
                .players(players)
                .deck(entity.getDeck())
                .discardPile(entity.getDiscardPile())
                .activePlayerId(entity.getActivePlayerId())
                .phase(entity.getPhase())
                .turnNumber(entity.getTurnNumber())
                .build();
    }

    private GameSession toGameSession(GameSessionDTO session) {
        List<Player> players = session.getPlayers().stream()
                .map(playerDTO -> playerService.getPlayerEntityById(playerDTO.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Игрок с ID " + playerDTO.getId() + " не найден")))
                .toList();

        return GameSession.builder()
                .id(session.getId())
                .lobbyId(session.getLobbyId())
                .players(players)
                .deck(session.getDeck())
                .discardPile(session.getDiscardPile())
                .activePlayerId(session.getActivePlayerId())
                .phase(session.getPhase())
                .turnNumber(session.getTurnNumber())
                .build();
    }





//    private void notifyPlayersAboutAction(GameSessionEntity session, Long playerId, PlayerAction action) {
//        // Отправляем сообщение через WebSocket
//        String message = String.format("Игрок %d совершил действие: %s", playerId, action.getType());
//        webSocketService.sendMessageToLobby(session.getLobbyId(), message);
//    }
//
//    private void notifyPlayersAboutPhaseChange(GameSessionEntity session) {
//        String message = String.format("Смена фазы на %s", session.getPhase());
//        webSocketService.sendMessageToLobby(session.getLobbyId(), message);
//    }
//
//    private void notifyPlayersAboutGameEnd(GameSessionEntity session) {
//        String message = String.format("Игра завершена! Победитель: игрок %d", session.getWinnerId());
//        webSocketService.sendMessageToLobby(session.getLobbyId(), message);
//    }


    // effect.getEffectType().applyEffect(effect, player);

}
