package main.frame.lobbyservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.frame.lobbyservice.model.LobbyUserStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlayerStatusRequest {
    private Long lobbyId;             // ID лобби
    private Long playerId;            // ID игрока
    private LobbyUserStatus status;   // Новый статус (например, READY, NOT_READY)
}

