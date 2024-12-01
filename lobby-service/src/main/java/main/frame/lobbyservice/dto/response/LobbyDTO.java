package main.frame.lobbyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.frame.lobbyservice.model.LobbyStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LobbyDTO {
    private Long id; // Уникальный идентификатор лобби
  //  @NotBlank
    private String name; // Имя лобби
 //   @Min(value = 2, message = "Максимальное количество игроков должно быть хотя бы 2.")
 //   @Max(value = 6, message = "Максимальное количество игроков должно быть не больше 6.")
    private int maxPlayers;
    private LobbyStatus status; // Статус лобби (например, "WAITING", "IN_GAME")
   // private String password;
    private Long hostId;
}
