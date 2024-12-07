package main.frame.gameservice.service;

import main.frame.gameservice.model.session.GameSessionDTO;
import main.frame.gameservice.model.session.GameSession;

public interface GameService {
    //  @Transactional
    void nextTurn(Long gameSessionId);
    GameSessionDTO startGameSession(Long lobbyId);
    void playCard(GameSessionDTO session, Long playerId, Long cardId);
    void discardCard(GameSessionDTO session, Long playerId, Long cardId);
    void saveGameSession(GameSessionDTO session);
    void endGame(GameSession session, Long winnerId);
}
