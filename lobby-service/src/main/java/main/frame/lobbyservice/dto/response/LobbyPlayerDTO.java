package main.frame.lobbyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.frame.lobbyservice.model.LobbyUserStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LobbyPlayerDTO {
    private Long id;
    private Long lobbyId; // ID лобби
    private Long playerId; // ID игрока
    private LobbyUserStatus status; // Тип действия (например, "JOIN", "START_GAME")
    private Integer turnOrder;
   // private String payload; // Дополнительные данные
}
