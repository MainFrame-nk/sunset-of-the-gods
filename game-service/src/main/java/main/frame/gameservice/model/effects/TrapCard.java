package main.frame.gameservice.model.effects;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import main.frame.gameservice.model.cardconfig.BaseCard;

@EqualsAndHashCode(callSuper = true)
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name = "trap_cards")
@ToString(callSuper = true) // Для добавления данных базового класса в вывод
public class TrapCard extends BaseCard {
//    @Column(name = "activation_condition")
//    private String activationCondition; // Условие активации ловушки
    @Override
    public String getType() {
        return "Trap";
    }
}
