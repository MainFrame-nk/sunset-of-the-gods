package main.frame.gameservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.frame.gameservice.client.LobbyServiceClient;
import main.frame.gameservice.model.cardconfig.RestrictedCard;
import main.frame.gameservice.model.characters.CharacterCard;
import main.frame.gameservice.model.player.Player;
import main.frame.gameservice.model.player.PlayerAction;
import main.frame.gameservice.model.session.GamePhase;
import main.frame.gameservice.model.session.GameSession;
import main.frame.gameservice.model.session.GameSessionEntity;
import main.frame.shared.dto.LobbyDTO;
import main.frame.shared.dto.PlayerDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImp {
    @PersistenceContext
    private EntityManager entityManager;
    private final LobbyServiceClient lobbyServiceClient;
    private final CardService cardService;

//    public boolean tryUseCard(Player player, Card card) {
//        if (player.canUseCard(card, cardService)) {
//            // Логика для использования карты
//            return true;
//        } else {
//            // Логика для ошибки, если карта не может быть использована
//            return false;
//        }
//    }

    public boolean canPlayerUseCard(Player player, RestrictedCard card) {
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

    public GameSession startGameSession(Long lobbyId) {
        LobbyDTO lobby = lobbyServiceClient.getLobbyById(lobbyId)
                .orElseThrow(() -> new IllegalArgumentException("Лобби не найдено!"));

        GameSession session = new GameSession();
        session.setLobbyId(lobbyId);
        session.setPlayers(lobby.getPlayers());
        session.initialize(); // Настройка начального состояния игры

        saveGameSession(session); // Сохранение сессии
        return session;
    }

    private void saveGameSession(GameSession session) {
        GameSessionEntity entity = new GameSessionEntity();
        entity.setLobbyId(session.getLobbyId());
        entity.setPlayers(session.getPlayers());
        entity.setPhase(session.getPhase());
        entity.setDeck(session.getDeck());
        entityManager.persist(entity);
    }

    public void processPlayerAction(Long sessionId, PlayerAction action) {
        GameSession session = getGameSessionById(sessionId);

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

    private void validateAction(GameSessionEntity session, Long playerId, PlayerAction action) {
        if (!session.getPlayerIds().contains(playerId)) {
            throw new IllegalArgumentException("Игрок не участвует в этой сессии!");
        }

        if (!session.getActivePlayerId().equals(playerId)) {
            throw new IllegalStateException("Сейчас не ход этого игрока!");
        }

        if (action.getType() == PlayerActionType.PLAY_CARD && !session.getDeck().contains(action.getCardId())) {
            throw new IllegalArgumentException("Игрок не может разыграть эту карту!");
        }
    }


    private void playCard(GameSession session, Long playerId, Long cardId) {
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


    private void discardCard(GameSession session, Long playerId, Long cardId) {
        // Логика сброса карты
        System.out.println("Игрок " + playerId + " сбрасывает карту " + cardId);
        // Убрать карту из руки и добавить в сброс
    }

    private void attackTarget(GameSession session, Long playerId, Long targetPlayerId) {
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


    private void useAbility(GameSession session, Long playerId, Long abilityId) {
        // Логика использования способности
        System.out.println("Игрок " + playerId + " использует способность " + abilityId);
    }

    public GameSession getGameSessionById(Long sessionId) {
        GameSessionEntity entity = entityManager.find(GameSessionEntity.class, sessionId);
        return mapEntityToSession(entity);
    }

    private void saveGameSession(GameSession session) {
        GameSessionEntity entity = mapSessionToEntity(session);
        entityManager.merge(entity);
    }

    public void nextPhase(Long gameSessionId) {
        GameSessionEntity session = getGameSessionEntityById(gameSessionId);

        switch (session.getPhase()) {
            case PREPARATION:
                session.setPhase(GamePhase.ACTION);
                break;
            case ACTION:
                session.setPhase(GamePhase.RESOLUTION);
                break;
            case RESOLUTION:
                session.setPhase(GamePhase.END_TURN);
                break;
            case END_TURN:
                startNextTurn(session);
                break;
        }


        saveGameSession(session);

        notifyPlayersAboutPhaseChange(session);
    }

    private boolean checkWinCondition(GameSessionEntity session, Long playerId) {
        Player player = getPlayerById(playerId); // Предполагаем, что есть метод получения игрока
        return player.getLevel() >= 10; // Победа, если игрок достиг уровня 10
    }

    private void endGame(GameSessionEntity session, Long winnerId) {
        session.setActive(false); // Завершаем сессию
        session.setWinnerId(winnerId);

        saveGameSession(session);

        notifyPlayersAboutGameEnd(session);
    }

    private GameSession mapEntityToSession(GameSessionEntity entity) {
        List<PlayerDTO> players = entity.getPlayerIds().stream()
                .map(playerId -> playerService.getPlayerById(playerId)) // Метод из PlayerService
                .collect(Collectors.toList());

        return GameSession.builder()
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

    private GameSessionEntity mapSessionToEntity(GameSession session) {
        List<Long> playerIds = session.getPlayers().stream()
                .map(PlayerDTO::getId) // Получаем ID из PlayerDTO
                .collect(Collectors.toList());

        return GameSessionEntity.builder()
                .id(session.getId())
                .lobbyId(session.getLobbyId())
                .playerIds(playerIds)
                .deck(session.getDeck())
                .discardPile(session.getDiscardPile())
                .activePlayerId(session.getActivePlayerId())
                .phase(session.getPhase())
                .turnNumber(session.getTurnNumber())
                .build();
    }


    private void notifyPlayersAboutAction(GameSessionEntity session, Long playerId, PlayerAction action) {
        // Отправляем сообщение через WebSocket
        String message = String.format("Игрок %d совершил действие: %s", playerId, action.getType());
        webSocketService.sendMessageToLobby(session.getLobbyId(), message);
    }

    private void notifyPlayersAboutPhaseChange(GameSessionEntity session) {
        String message = String.format("Смена фазы на %s", session.getPhase());
        webSocketService.sendMessageToLobby(session.getLobbyId(), message);
    }

    private void notifyPlayersAboutGameEnd(GameSessionEntity session) {
        String message = String.format("Игра завершена! Победитель: игрок %d", session.getWinnerId());
        webSocketService.sendMessageToLobby(session.getLobbyId(), message);
    }


    // effect.getEffectType().applyEffect(effect, player);

}
