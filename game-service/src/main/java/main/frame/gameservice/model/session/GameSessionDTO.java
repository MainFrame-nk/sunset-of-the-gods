package main.frame.gameservice.model.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.frame.gameservice.model.cardconfig.Card;
import main.frame.shared.dto.PlayerDTO;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GameSessionDTO {
    private Long id;
    private Long lobbyId;
    private List<PlayerDTO> players;
    private Map<Long, List<Card>> playerHands; // Карты игроков
    private List<Card> deck; // Общая колода карт
    private List<Card> discardPile; // Карты в сбросе
    private List<Long> turnOrder; // Очередность ходов (ID игроков)
    private Long activePlayerId; // Игрок, который сейчас ходит
    private GamePhase phase; // Текущая фаза игры
    private int turnNumber; // Номер текущего хода

    // Начальная настройка игры
    public void initialize() {
        shufflePlayers(); // Случайное определение очередности ходов
        this.playerHands = new HashMap<>();
        for (PlayerDTO player : players) {
            List<Card> initialCards = dealCards(5); // Раздача 5 карт каждому игроку
            playerHands.put(player.getId(), initialCards);
        }
        this.deck = createDeck(); // Создать колоду
        this.discardPile = new ArrayList<>();
        this.activePlayerId = turnOrder.get(0); // Первый игрок начинает
        this.phase = GamePhase.START;
        this.turnNumber = 1;
    }

    // Случайное определение очередности игроков
    private void shufflePlayers() {
        Collections.shuffle(players);
        this.turnOrder = players.stream()
                .map(PlayerDTO::getId)
                .collect(Collectors.toList());
    }

    // Переход хода к следующему игроку
    public void nextTurn() {
        int currentIndex = turnOrder.indexOf(activePlayerId);
        int nextIndex = (currentIndex + 1) % turnOrder.size();
        this.activePlayerId = turnOrder.get(nextIndex);
        this.turnNumber++;
        this.phase = GamePhase.PLAY;
    }

    // Логика раздачи карт
    private List<Card> dealCards(int count) {
        List<Card> dealtCards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (!deck.isEmpty()) {
                dealtCards.add(deck.remove(0));
            }
        }
        return dealtCards;
    }

    // Создание начальной колоды
    private List<Card> createDeck() {
        return new ArrayList<>(); // Логика генерации карт
    }

    // Перемещение карты в сброс
    public void discardCard(Long playerId, Card card) {
        if (playerHands.get(playerId).remove(card)) {
            discardPile.add(card);
        } else {
            throw new IllegalStateException("Карта не найдена у игрока!");
        }
    }

    // Проверка завершения игры
    public boolean isGameOver() {
        // Логика определения завершения игры
        return false; // Пример: возвращайте true, если есть победитель
    }
}

