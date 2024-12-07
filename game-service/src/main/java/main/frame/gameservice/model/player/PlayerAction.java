package main.frame.gameservice.model.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerAction {
    private Long playerId; // ID игрока, выполняющего действие
    private ActionType type; // Тип действия
    private Long cardId; // ID карты (если действие связано с картой)
    private Long targetPlayerId; // ID цели (если действие направлено на другого игрока)
}