package main.frame.gameservice.model.session;

public enum GamePhase {
    PREPARATION,  // Подготовка (раздача карт)
    START,        // Начало игры, раздача карт
    PLAY,         // Ход игрока: разыгрывание карт
    BATTLE,       // Сражение с монстром
    LOOT,         // Получение добычи
    END_TURN,     // Завершение хода
    GAME_OVER     // Конец игры
}
