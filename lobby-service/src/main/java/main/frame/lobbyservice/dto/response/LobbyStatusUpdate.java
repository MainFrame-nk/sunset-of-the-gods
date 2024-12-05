package main.frame.lobbyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LobbyStatusUpdate {
    private Long lobbyId;
    private String status;
}
