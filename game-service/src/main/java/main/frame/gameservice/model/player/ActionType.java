package main.frame.gameservice.model.player;

public enum ActionType {
    PLAY_CARD,  // Разыграть карту
    DISCARD_CARD, // Сбросить карту
    END_TURN,   // Завершить ход
    ATTACK,     // Атаковать монстра или игрока
    USE_ABILITY // Использовать способность
}