package main.frame.gameservice.model.effects;

import jakarta.persistence.*;
import lombok.*;
import main.frame.gameservice.model.cardconfig.BaseCard;

@EqualsAndHashCode(callSuper = true)
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name = "curse_cards")
@ToString(callSuper = true) // Для добавления данных базового класса в вывод
public class CurseCard extends BaseCard {
//    // Проклятие сразу применяется или активируется по событию
//    @Column(name = "is_immediate", nullable = false)
//    private boolean isImmediate;
    @Override
    public String getType() {
        return "Curse";
    }
}
