package main.frame.gameservice.service;

import main.frame.gameservice.model.characters.CharacterCard;
import main.frame.gameservice.model.Player;
import main.frame.gameservice.model.RestrictedCard;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardRestrictionService {

    public boolean canPlayerUseCard(Player player, RestrictedCard card) {
        Set<String> playerClasses = player.getCharacterClasses()
                .stream()
                .map(CharacterCard::getName)
                .collect(Collectors.toSet());

        // Проверяем "разрешённые" классы
        if (!card.getAllowedClasses().isEmpty() &&
                Collections.disjoint(playerClasses, card.getAllowedClasses())) {
            return false; // Игрок не может использовать карту
        }

        // Проверяем "запрещённые" классы
        if (!card.getRestrictedClasses().isEmpty() &&
                !Collections.disjoint(playerClasses, card.getRestrictedClasses())) {
            return false; // Игрок не может использовать карту
        }

        return true; // Ограничений нет
    }

//    public boolean canPlayerUseCompanion(Player player, CompanionCard companion) {
//        Set<String> playerClasses = player.getCharacterClasses()
//                .stream()
//                .map(CharacterCard::getName)
//                .collect(Collectors.toSet());
//
//        // Проверяем "разрешённые" классы
//        if (!companion.getAllowedClasses().isEmpty() &&
//                Collections.disjoint(playerClasses, companion.getAllowedClasses())) {
//            return false; // Игрок не может использовать напарника
//        }
//
//        // Проверяем "запрещённые" классы
//        if (!companion.getRestrictedClasses().isEmpty() &&
//                !Collections.disjoint(playerClasses, companion.getRestrictedClasses())) {
//            return false; // Игрок не может использовать напарника
//        }
//
//        return true; // Напарник доступен для использования
//    }

}
