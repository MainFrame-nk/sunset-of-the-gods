package main.frame.gameservice.model;

import jakarta.persistence.*;
import lombok.*;
import main.frame.shared.dto.PlayerDTO;
import main.frame.gameservice.model.characters.CharacterCard;
import main.frame.gameservice.model.characters.CompanionCard;
import main.frame.gameservice.model.effects.Ability;
import main.frame.gameservice.model.effects.Effect;
import main.frame.gameservice.model.equipment.BodyCard;
import main.frame.gameservice.model.equipment.HeadCard;
import main.frame.gameservice.model.equipment.LegCard;
import main.frame.gameservice.model.equipment.WeaponCard;
import main.frame.shared.dto.UserDTO;

import java.util.*;

@Data
@Entity
@Table(name = "players")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // ID пользователя из UserService

    @Column(name = "level")
    private int level = 1;


    public PlayerDTO toPlayerDTO() {
        return new PlayerDTO(
                this.id,
                this.userId
              //  this.level
        );
    }
}
