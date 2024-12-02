package main.frame.lobbyservice.service;

import main.frame.lobbyservice.dto.request.JoinLobbyRequest;
import main.frame.lobbyservice.dto.request.LeaveLobbyRequest;
import main.frame.lobbyservice.dto.response.CreateLobbyDTO;
import main.frame.shared.dto.LobbyDTO;
import main.frame.lobbyservice.dto.response.LobbyPlayerDTO;
import main.frame.lobbyservice.model.Lobby;
import main.frame.lobbyservice.model.LobbyStatus;
import main.frame.lobbyservice.model.LobbyUserStatus;

import java.util.List;
import java.util.Optional;

public interface LobbyService {
    Optional<LobbyDTO> getLobbyById(Long lobbyId);
    List<LobbyDTO> getAllLobbies(LobbyStatus status);
    void removePlayerFromLobby(Long lobbyId, Long playerId, Long requestorId);
    //private void transferHostToNextPlayer(Long lobbyId)
   // public void cleanupInactiveLobbies(); // Удалить неактивные лобби
    void createLobby(CreateLobbyDTO createLobbyDTO);
    boolean deleteLobby(Long lobbyId);
   // public void closeLobby(Long lobbyId);
    LobbyDTO updateLobby(Long lobbyId, Long hostId, String name, String password, int maxPlayers);
    LobbyPlayerDTO joinToLobby(JoinLobbyRequest request);
    //  public List<LobbyDTO> getAvailableLobbies();
    void disconnectPlayerFromLobby(LeaveLobbyRequest request);
    List<LobbyPlayerDTO> getPlayersInLobby(Long lobbyId); // Получить игроков в лобби
    void startGame(Long lobbyId, Long hostId);
    void endTurn(Long lobbyId, Long userId);
   // LobbyDTO setMaxPlayers(Long lobbyId, int maxPlayers);
    void updatePlayerStatus(Long lobbyId, Long userId, LobbyUserStatus status);
    List<LobbyDTO> filterLobbies(Optional<Integer> minPlayers, Optional<Integer> maxPlayers, Optional<String> gameMode);
  //  public void inviteUserToLobby(Long lobbyId, Long userId);
  //  public Lobby acceptInvitation(Long lobbyId, Long userId);
  //  public Lobby createRankedLobby(Long ownerId, int minRank, int maxRank); Рейтинговые игры
    // public List<LobbyDTO> filterLobbies(Optional<Integer> minPlayers, Optional<Integer> maxPlayers, Optional<String> gameMode, Optional<Integer> minRank, Optional<Integer> maxRank);

}
