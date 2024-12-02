package main.frame.gameservice.service;

import main.frame.gameservice.model.equipment.ArmorCard;
import main.frame.gameservice.model.equipment.BodyCard;
import main.frame.gameservice.model.equipment.HeadCard;
import main.frame.gameservice.model.equipment.LegCard;
import main.frame.gameservice.model.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipmentService {
    @Transactional
    public void equipArmor(Player player, ArmorCard armorCard) {
//        if (!canEquip(player, armorCard)) {
//            throw new IllegalArgumentException("Игрок не может использовать эту броню!");
//        }

        switch (armorCard.getType()) {
            case "Head":
                player.setHeadSlot((HeadCard) armorCard);
                break;
            case "Body":
                player.setBodySlot((BodyCard) armorCard);
                break;
            case "Leg":
                player.setLegSlot((LegCard) armorCard);
                break;
            default:
                throw new IllegalArgumentException("Неверный тип брони!");
        }
    }

//    public boolean canEquip(Player player, ArmorCard armorCard) {
//        for (String playerClass : player.getClasses()) {
//            if (armorCard.getAllowedClasses().contains(playerClass)) {
//                return true; // Игрок может использовать карту
//            }
//        }
//        return false; // Класс игрока не подходит
//    }

}