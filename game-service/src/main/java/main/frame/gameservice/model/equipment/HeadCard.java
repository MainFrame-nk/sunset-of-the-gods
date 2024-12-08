package main.frame.gameservice.model.equipment;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
//@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "head_cards")
@ToString(callSuper = true) // Для добавления данных базового класса в вывод
public class HeadCard extends ArmorCard {
    @Override
    public String getType() {
        return "Head";
    }

//    @Override
//    public boolean isUsableBy(Player player) {
//        // Пример для TreasureCard: проверяем, есть ли у игрока подходящий класс
//        return allowedClasses.contains(player.getCharacterClass());
//    }
}
