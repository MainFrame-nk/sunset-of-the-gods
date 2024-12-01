package main.frame.gameservice.service;

import main.frame.gameservice.model.*;
import main.frame.gameservice.model.characters.CharacterCard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service
//public class CharacterClassService {
//    @Transactional
//    public void addClassToPlayer(Player player, CharacterCard characterCard) {
//        if (!player.addCharacterClass(characterCard)) {
//            throw new IllegalArgumentException("Игрок уже достиг лимита классов!");
//        }
//    }
//
//    @Transactional
//    public void removeClassFromPlayer(Player player, CharacterCard characterCard) {
//        if (!player.removeCharacterClass(characterCard)) {
//            throw new IllegalArgumentException("Этот класс не принадлежит игроку!");
//        }
//    }
//
////    public boolean canUseCard(Player player, Card card) {
////        if (card instanceof ArmorCard || card instanceof WeaponCard) {
////            for (CharacterCard characterCard : player.getCharacterClasses()) {
////                if (characterCard.getAllowedEquipmentTypes().contains(card.getType())) {
////                    return true; // Карта совместима хотя бы с одним классом
////                }
////            }
////            return false; // Никакие классы не поддерживают эту карту
////        }
////        return true; // Если карта не броня или оружие, проверка не требуется
////    }
//
//    @Transactional
//    public void applyClassLimitIncreaseCard(Player player, ClassLimitIncreaseCard card) {
//        player.setMaxClasses(player.getMaxClasses() + card.getIncreaseAmount());
//    }
//
//}
