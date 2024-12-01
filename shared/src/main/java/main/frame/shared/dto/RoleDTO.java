package main.frame.shared.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Component
public class RoleDTO {
    private Integer id;
    private String name;
}
