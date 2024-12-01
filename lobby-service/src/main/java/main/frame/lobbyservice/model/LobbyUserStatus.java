package main.frame.lobbyservice.model;

public enum LobbyUserStatus {
    CONNECTED, // Подключен к лобби
    NOT_READY,     // Присоединяется к игре, не готов к началу
    READY,     // Готов к началу игры
    PLAYING,    // В игре
    IN_TURN     // Ход игрока
}
