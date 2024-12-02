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

    @Enumerated(EnumType.STRING)
    @Column(name = "phase", nullable = false)
    private GamePhase phase = GamePhase.PREPARATION; // Текущая фаза игры

    @ElementCollection
    @CollectionTable(name = "game_session_deck", joinColumns = @JoinColumn(name = "game_session_id"))
    @Column(name = "card_id")
    private List<Long> deck; // Список ID карт в колоде

    @ElementCollection
    @CollectionTable(name = "game_session_discard_pile", joinColumns = @JoinColumn(name = "game_session_id"))
    @Column(name = "card_id")
    private List<Long> discardPile; // Список ID карт в сбросе

    @Column(name = "active_player_id", nullable = false)
    private Long activePlayerId; // ID текущего игрока

    @Column(name = "turn_number", nullable = false)
    private int turnNumber; // Номер текущего хода
}
