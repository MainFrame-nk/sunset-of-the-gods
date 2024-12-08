package main.frame.gameservice.model.cardconfig;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.frame.gameservice.model.effects.Effect;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@MappedSuperclass
@Entity
//чтобы BaseCard сохранялась как отдельная таблица, нужно удалить аннотацию @MappedSuperclass и оставить только @Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "card_type", discriminatorType = DiscriminatorType.STRING)
public abstract class BaseCard implements RestrictedCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
   // @Column(name = "type", insertable = false, updatable = false)
    private String type;
    // private String type = "Armor"; // Тип карты

    @ElementCollection
    @CollectionTable(name = "card_allowed_classes", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "class_name")
    private Set<String> allowedClasses = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "card_restricted_classes", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "class_name")
    private Set<String> restrictedClasses = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "card_effects",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "effect_id")
    )
    private Set<Effect> effects = new HashSet<>(); // Список эффектов

    @Column(name = "gold_value")
    private int goldValue; // Цена карты (может быть 0 для бесплатных, -1 без цены)
}
