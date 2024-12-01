package main.frame.gameservice.model.equipment;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "body_cards")
@ToString(callSuper = true) // Для добавления данных базового класса в вывод
public class BodyCard extends ArmorCard {
    @Override
    public String getType() {
        return "Body"; // Специфический тип для тела
    }
}
