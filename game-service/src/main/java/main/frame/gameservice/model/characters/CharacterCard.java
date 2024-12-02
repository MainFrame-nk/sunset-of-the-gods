package main.frame.gameservice.model.characters;

import jakarta.persistence.*;
import lombok.*;
import main.frame.gameservice.model.cardconfig.Card;

@Data
@Entity
@Table(name = "character_cards")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CharacterCard implements Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type = "Character"; // Тип карты

//    @Column(name = "levelRequirement")
//    private String levelRequirement; // Требования к уровню для использования

    // Реализация методов интерфейса Card
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
