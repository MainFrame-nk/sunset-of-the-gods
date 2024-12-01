package main.frame.lobbyservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import main.frame.lobbyservice.dto.response.LobbyDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lobbies")
@ToString
public class Lobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный идентификатор лобби

    @Column(name = "name", nullable = false)
    private String name; // Имя лобби

    @Column(name = "max_players", nullable = false)
    private int maxPlayers = 6; // Максимальное количество игроков в лобби

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "lobby_players", joinColumns = @JoinColumn(name = "lobby_id"))
//    @Column(name = "player_id")
//    private Set<Long> playerIds = new HashSet<>(); // Список ID игроков, подключённых к лобби


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LobbyStatus status; // Статус лобби (например, "WAITING", "IN_GAME")

    @Column(name = "password")
    private String password; // Пароль для приватного лобби (может быть null для публичных).

    @Column(name = "host_id", nullable = false)
    private Long hostId; // ID пользователя-хоста (создателя лобби)
//    @Column(name = "min_rank")
//    private Integer minRank;
//
//    @Column(name = "max_rank")
//    private Integer maxRank;

    public LobbyDTO toLobbyDTO() {
        return new LobbyDTO(
                this.id,
                this.name,
                this.maxPlayers,
                this.status,
         //       this.password,
                this.hostId
        );
    }

}
