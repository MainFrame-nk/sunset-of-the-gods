package main.frame.shared.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LobbyDTO {
    private Long id; // Уникальный идентификатор лобби
    private String name; // Имя лобби
  //  private int maxPlayers;
  //  private LobbyStatus status; // Статус лобби (например, "WAITING", "IN_GAME")
  //  private String password;
  //  private Long hostId;
}
