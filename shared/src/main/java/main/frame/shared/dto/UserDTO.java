package main.frame.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String password;
    private Set<RoleDTO> roles;
//    private Long id;
//    private String email;
//
//    private String username;
//    private String phoneNumber;
//    private boolean active;
//    private Set<RoleDTO> roles = new HashSet<>();
//    private LocalDateTime dateOfCreated;

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserDTO(String username) {
        this.username = username;
    }
}
