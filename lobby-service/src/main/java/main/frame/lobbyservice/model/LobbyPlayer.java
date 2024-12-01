package main.frame.lobbyservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.frame.lobbyservice.dto.response.LobbyPlayerDTO;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "lobby_players")
public class LobbyPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный идентификатор записи

    @Column(name = "lobby_id", nullable = false)
    private Long lobbyId; // Идентификатор лобби

    @Column(name = "player_id", nullable = false)
    private Long playerId; // Идентификатор игрока

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LobbyUserStatus status; // Статус игрока в лобби (например, READY, NOT_READY, IN_GAME)

    @Column(name = "turn_order", nullable = false)
    private Integer turnOrder = 0; // Порядок хода игрока в лобби

    // Преобразование сущности User в UserDTO
    public LobbyPlayerDTO toLobbyPlayerDTO() {
        return new LobbyPlayerDTO(
                this.id,
                this.lobbyId,
                this.playerId,
                this.status,
                this.turnOrder
        );
    }
}
