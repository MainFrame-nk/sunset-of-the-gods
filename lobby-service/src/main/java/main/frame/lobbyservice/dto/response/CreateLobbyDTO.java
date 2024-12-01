package main.frame.lobbyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CreateLobbyDTO {

    @NotBlank(message = "Имя лобби не может быть пустым.")
    private String name;

    private String password;  // Пароль может быть пустым

    @Min(value = 2, message = "Максимальное количество игроков должно быть хотя бы 2.")
    @Max(value = 6, message = "Максимальное количество игроков должно быть не больше 6.")
    private Integer maxPlayers;

    private Long hostId;  // ID хозяина лобби

}
