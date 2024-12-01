package main.frame.gameservice.model.characters;

import jakarta.persistence.*;
import lombok.*;
import main.frame.gameservice.model.BaseCard;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "monster_cards")
@ToString(callSuper = true) // Для добавления данных базового класса в вывод
public class MonsterCard extends BaseCard {
    @Column(name = "level", nullable = false)
    private int level; // Уровень монстра

    @Column(name = "rewards")
    private int rewards; // Количество наград за победу (сокровища, уровни)

    @Column(name = "penalty")
    private String penalty; // Штраф за поражение (потеря уровней, здоровья и т.д.)

    @Enumerated(EnumType.STRING) // Хранение enum в виде строки в БД
    @Column(name = "monster_type", nullable = false)
    private MonsterType monsterType; // Группа монстра, например, "Киборг"

    @Override
    public String getType() {
        return "Monster";
    }
}
