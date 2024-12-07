package main.frame.gameservice.dto;

import lombok.*;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PlayerDTO {
    private Long id;
    private Long userId;
    private int level;
}
