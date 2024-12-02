package main.frame.gameservice.model;

import lombok.Data;
import main.frame.shared.dto.PlayerDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GameSession {
    private Long lobbyId;
    private List<PlayerDTO> players;
    private Map<Long, List<Card>> playerHands; // Карты игроков
    private List<Card> deck; // Общая колода карт
    private List<Card> discardPile; // Карты в сбросе
    private Long activePlayerId; // Игрок, который сейчас ходит
    private GamePhase phase; // Текущая фаза игры
    private int turnNumber; // Номер текущего хода

    public void initialize() {
        // Начальная настройка игры
        this.playerHands = new HashMap<>();
        for (PlayerDTO player : players) {
            List<Card> initialCards = dealCards(5); // Раздача 5 карт каждому игроку
            playerHands.put(player.getId(), initialCards);
        }
        this.deck = createDeck(); // Создать колоду
        this.discardPile = new ArrayList<>();
        this.activePlayerId = players.get(0).getId(); // Первый игрок начинает
        this.phase = GamePhase.START;
        this.turnNumber = 1;
    }

    public void nextTurn() {
        // Переход хода к следующему игроку
        int currentIndex = players.indexOf(getActivePlayer());
        int nextIndex = (currentIndex + 1) % players.size();
        this.activePlayerId = players.get(nextIndex).getId();
        this.turnNumber++;
        this.phase = GamePhase.PLAY;
    }

    private PlayerDTO getActivePlayer() {
        return players.stream()
                .filter(player -> player.getId().equals(activePlayerId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Активный игрок не найден!"));
    }

    private List<Card> dealCards(int count) {
        // Логика раздачи карт
        List<Card> dealtCards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (!deck.isEmpty()) {
                dealtCards.add(deck.remove(0));
            }
        }
        return dealtCards;
    }

    private List<Card> createDeck() {
        // Генерация колоды карт
        return new ArrayList<>(); // Здесь можно добавить карты в колоду
    }
}

