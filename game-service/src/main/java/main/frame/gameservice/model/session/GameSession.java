package main.frame.gameservice.model.session;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.frame.gameservice.config.JsonConverter;
import main.frame.gameservice.model.cardconfig.Card;
import main.frame.gameservice.model.player.Player;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "game_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lobby_id", nullable = false)
    private Long lobbyId; // Связь с лобби

    @ManyToMany
    @JoinTable(
            name = "game_session_players",
            joinColumns = @JoinColumn(name = "game_session_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> players; // Список игроков в сессии, использовать ли Player???

    @ElementCollection
    @CollectionTable(name = "game_session_turn_order", joinColumns = @JoinColumn(name = "game_session_id"))
    @Column(name = "player_id")
    private List<Long> turnOrder; // Очередность ходов

    @Column(name = "active_player_id", nullable = false)
    private Long activePlayerId; // Текущий игрок

    @Enumerated(EnumType.STRING)
    @Column(name = "phase", nullable = false)
    private GamePhase phase = GamePhase.PREPARATION; // Текущая фаза игры

    @Convert(converter = JsonConverter.class)
    @Column(name = "player_hands", columnDefinition = "TEXT")
    private Map<Long, List<Card>> playerHands; // Карты игроков (ID карт)

//    @ElementCollection
//    @CollectionTable(name = "game_session_deck", joinColumns = @JoinColumn(name = "game_session_id"))
//    @Column(name = "card_id")
//    private List<Long> deck; // Общая колода карт (ID карт)

    @Convert(converter = JsonConverter.class)
    @Column(name = "deck", columnDefinition = "TEXT")
    private List<Card> deck; // ID карт в колоде


    @Convert(converter = JsonConverter.class)
    @Column(name = "discard_pile", columnDefinition = "TEXT")
    private List<Card> discardPile; // Карты в сбросе


//    @ElementCollection
//    @CollectionTable(name = "game_session_discard_pile", joinColumns = @JoinColumn(name = "game_session_id"))
//    @Column(name = "card_id")
//    private List<Long> discardPile; // Карты в сбросе (ID карт)

    @Column(name = "turn_number", nullable = false)
    private int turnNumber; // Номер текущего хода

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; // Статус сессии (активна или завершена)
}
