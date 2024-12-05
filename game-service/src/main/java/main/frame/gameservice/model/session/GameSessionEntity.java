package main.frame.gameservice.model.session;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.frame.gameservice.model.player.Player;

import java.util.List;

@Entity
@Table(name = "game_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSessionEntity {
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

    @Column(name = "active_player_index", nullable = false)
    private int activePlayerIndex; // Индекс текущего игрока в очереди

    @Enumerated(EnumType.STRING)
    @Column(name = "phase", nullable = false)
    private GamePhase phase = GamePhase.PREPARATION; // Текущая фаза игры

    @ElementCollection
    @CollectionTable(name = "game_session_deck", joinColumns = @JoinColumn(name = "game_session_id"))
    @Column(name = "card_id")
    private List<Long> deck; // Общая колода карт (ID карт)

    @ElementCollection
    @CollectionTable(name = "game_session_discard_pile", joinColumns = @JoinColumn(name = "game_session_id"))
    @Column(name = "card_id")
    private List<Long> discardPile; // Карты в сбросе (ID карт)

    @Column(name = "turn_number", nullable = false)
    private int turnNumber; // Номер текущего хода

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; // Статус сессии (активна или завершена)
}
