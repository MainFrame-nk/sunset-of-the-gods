package main.frame.lobbyservice.service;

import main.frame.lobbyservice.dto.request.JoinLobbyRequest;
import main.frame.lobbyservice.dto.response.CreateLobbyDTO;
import main.frame.shared.dto.LobbyDTO;
import main.frame.lobbyservice.dto.response.LobbyPlayerDTO;
import main.frame.lobbyservice.model.Lobby;
import main.frame.lobbyservice.model.LobbyStatus;
import main.frame.lobbyservice.model.LobbyUserStatus;

import java.util.List;
import java.util.Optional;

public interface LobbyService {
    public Optional<LobbyDTO> getLobbyById(Long lobbyId);
    List<LobbyDTO> getAllLobbies(LobbyStatus status);
    public void removePlayerFromLobby(Long lobbyId, Long playerId, Long requestorId);
    //private void transferHostToNextPlayer(Long lobbyId)
   // public void cleanupInactiveLobbies(); // Удалить неактивные лобби
    public void createLobby(CreateLobbyDTO createLobbyDTO);
    public boolean deleteLobby(Long lobbyId);
   // public void closeLobby(Long lobbyId);
    public LobbyDTO updateLobby(Long lobbyId, Long hostId, String name, String password, int maxPlayers);
    public LobbyPlayerDTO joinToLobby(JoinLobbyRequest request);
  //  public List<LobbyDTO> getAvailableLobbies();
    public void disconnectPlayerFromLobby(Long lobbyId, Long userId);
    public List<LobbyPlayerDTO> getPlayersInLobby(Long lobbyId); // Получить игроков в лобби
    public void startGame(Long lobbyId, Long hostId);
    void endTurn(Long lobbyId, Long userId);
    public LobbyDTO setMaxPlayers(Long lobbyId, int maxPlayers);
    public void updatePlayerStatus(Long lobbyId, Long userId, LobbyUserStatus status);
    public List<LobbyDTO> filterLobbies(Optional<Integer> minPlayers, Optional<Integer> maxPlayers, Optional<String> gameMode);
    public Lobby connectToPrivateLobby(JoinLobbyRequest request, String password);
  //  public void inviteUserToLobby(Long lobbyId, Long userId);
  //  public Lobby acceptInvitation(Long lobbyId, Long userId);
  //  public Lobby createRankedLobby(Long ownerId, int minRank, int maxRank); Рейтинговые игры
    // public List<LobbyDTO> filterLobbies(Optional<Integer> minPlayers, Optional<Integer> maxPlayers, Optional<String> gameMode, Optional<Integer> minRank, Optional<Integer> maxRank);

}
