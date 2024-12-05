package main.frame.gameservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class GameTurnUpdate {
    private Long gameId;
    private Long playerId;
    private String action;
}
