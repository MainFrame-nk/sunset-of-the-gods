package main.frame.lobbyservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaveLobbyRequest {
    private Long lobbyId;
    private Long playerId;

}