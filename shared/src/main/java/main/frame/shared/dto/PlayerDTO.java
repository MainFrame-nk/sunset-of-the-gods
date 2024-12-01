package main.frame.shared.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlayerDTO {
    private Long id;
    private Long userId;
//    private int level;
}
