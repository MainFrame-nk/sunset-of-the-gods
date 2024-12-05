package main.frame.gameservice.service;

import main.frame.gameservice.model.session.GameSession;
import main.frame.gameservice.model.session.GameSessionEntity;

public interface GameService {
    GameSession startGameSession(Long lobbyId);
    void playCard(GameSession session, Long playerId, Long cardId);
    void discardCard(GameSession session, Long playerId, Long cardId);
    void saveGameSession(GameSession session);
    void endGame(GameSessionEntity session, Long winnerId);
}
