package main.frame.gameservice.model.characters;

import jakarta.persistence.*;
import lombok.*;
import main.frame.gameservice.model.BaseCard;
import main.frame.gameservice.model.effects.Effect;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companion_cards")
@ToString(callSuper = true) // Для добавления данных базового класса в вывод
public class CompanionCard extends BaseCard {
//    @Column(name = "level")
//    private int level; // Уровень напарника

    @Column(name = "bonus")
    private int bonus; // Бонус, который напарник даёт игроку (например, к урону)

    @ManyToMany
    @JoinTable(
            name = "companion_card_effects",
            joinColumns = @JoinColumn(name = "companion_card_id"),
            inverseJoinColumns = @JoinColumn(name = "effect_id")
    )
    private Set<Effect> effects = new HashSet<>(); // Список эффектов для напарника

//    @Column(name = "health_points")
//    private int healthPoints; // Очки здоровья

    @ElementCollection
    @CollectionTable(name = "companion_allowed_classes", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "class_name")
    private Set<String> allowedClasses; // Классы, которые могут использовать этого напарника

    @ElementCollection
    @CollectionTable(name = "companion_restricted_classes", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "class_name")
    private Set<String> restrictedClasses; // Классы, которые не могут использовать этого напарника

//    @Column(name = "is_unique")
//    private boolean isUnique; // Уникален ли напарник (может ли быть только один на поле)

//    @Override
//    public Set<String> getRestrictedClasses() {
//        return Set.of("Mage");
//    }
}
