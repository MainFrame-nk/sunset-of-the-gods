package main.frame.gameservice.model;

import java.util.Set;

public interface RestrictedCard extends Card {
    // Ограничения по классам
    default Set<String> getAllowedClasses() {
        return Set.of(); // По умолчанию карта доступна всем
    }

    default Set<String> getRestrictedClasses() {
        return Set.of(); // По умолчанию нет ограничений
    }
//    boolean isUsableBy(Player player);
}
