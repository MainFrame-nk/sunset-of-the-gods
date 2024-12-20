package main.frame.gameservice.service;

import main.frame.gameservice.dto.LobbyPlayerCardsDTO;
import main.frame.shared.dto.PlayerDTO;
import main.frame.gameservice.model.player.Player;
import main.frame.shared.dto.UserDTO;

import java.util.Optional;

public interface PlayerService {
  Optional<PlayerDTO> getById(Long id);
  Optional<LobbyPlayerCardsDTO> getPlayerDeckById(Long id);
  Player createPlayer(UserDTO userDTO);
  Optional<Player> getPlayerEntityById(Long id);
  //  Optional<UserDTO> getById(Long id);
 //   boolean deleteUser(Long id);
 //   void createUser(RegisterRequest registerRequest);
  //  List<UserDTO> getAllUsers();
 // Optional<PlayerDTO> updatePlayer(Long id, PlayerDTO playerDTO);
 //   Optional<UserDTO> updateUserRoles(Long userId, List<String> roleNames);
 //   Optional<User> findByEmail(String email);
 //   void userBan(Long id);
 //   Optional<User> getUserByPrincipal(Principal principal);
}
