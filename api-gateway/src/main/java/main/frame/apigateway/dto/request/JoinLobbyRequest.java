package main.frame.apigateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinLobbyRequest {
    private Long lobbyId;
    private Long playerId;
    private String password;
}