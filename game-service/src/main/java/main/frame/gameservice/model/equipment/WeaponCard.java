package main.frame.gameservice.model.equipment;

import jakarta.persistence.*;
import lombok.*;
import main.frame.gameservice.model.cardconfig.BaseCard;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "weapon_cards")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true) // Для добавления данных базового класса в вывод
public class WeaponCard extends BaseCard {
//    @Column(name = "rarity")
//    private String rarity; // Редкость карты, например "Common", "Rare", "Epic"

    @Column(name = "damage")
    private String damage; // Урон от оружия

    @Column(name = "hands")
    private byte hands; // Для одной или двух рук

    @Column(name = "gold_value")
    private int goldValue; // Цена карты (может быть 0 для бесплатных)

    @Enumerated(EnumType.STRING) // Хранение enum в виде строки в БД
    @Column(name = "weapon_type", nullable = false)
    private WeaponType weaponType; // Тип оружия (например, лазерное)

    @Override
    public String getType() {
        return "Weapon";
    }

//    @Override
//    public Set<String> getAllowedClasses() {
//        return this.allowedClasses;
//    }

//    @Override
//    public Set<String> getAllowedClasses() {
//        return Set.of("Warrior", "Paladin");
//    }
}
