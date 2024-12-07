package main.frame.gameservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GamePlayerDTO {
    private Long playerId;
 //   private Integer turnOrder; // Порядок хода, если требуется
}