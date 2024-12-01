package main.frame.gameservice.model.effects;

import jakarta.persistence.*;
import lombok.*;
import main.frame.gameservice.model.BaseCard;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "abilities")
public class Ability extends BaseCard {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ability_effects",
            joinColumns = @JoinColumn(name = "ability_id"),
            inverseJoinColumns = @JoinColumn(name = "effect_id")
    )
    private Set<Effect> effects = new HashSet<>(); // Список эффектов, связанных с этим навыком

    @Override
    public String getType() {
        return "Ability"; // Специфический тип для тела
    }
}
