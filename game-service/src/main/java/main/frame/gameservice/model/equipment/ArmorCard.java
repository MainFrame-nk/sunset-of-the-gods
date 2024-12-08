package main.frame.gameservice.model.equipment;

import jakarta.persistence.*;
import lombok.*;
import main.frame.gameservice.model.cardconfig.BaseCard;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true) // Для добавления данных базового класса в вывод
//@DiscriminatorColumn(name = "type")
public abstract class ArmorCard extends BaseCard {
    private int armorValue; // Защита от брони (бонус)
  //  private int goldValue; // Цена карты (может быть 0 для бесплатных)
}
