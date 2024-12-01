package main.frame.lobbyservice.controller;

import main.frame.lobbyservice.dto.request.JoinLobbyRequest;
import main.frame.lobbyservice.dto.response.CreateLobbyDTO;
import main.frame.lobbyservice.dto.response.LobbyDTO;
import main.frame.lobbyservice.model.Lobby;
import main.frame.lobbyservice.service.LobbyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

//    @PostMapping("/create")
//    public ResponseEntity<Lobby> createLobby(@RequestParam String name,
//                                             @RequestParam String password,
//                                             @RequestParam Integer maxPlayers,
//                                             @RequestParam Long hostId) {
//        Lobby lobby = lobbyService.createLobby(name, password, maxPlayers, hostId);
//        return ResponseEntity.ok(lobby);
//    }

    @PostMapping("/create")
    public ResponseEntity<Lobby> createLobby(@RequestBody @Valid CreateLobbyDTO createLobbyDTO) {
        Lobby lobby = lobbyService.createLobby(createLobbyDTO);
        return ResponseEntity.ok(lobby);
    }

//    @PostMapping("/register")
//    public ResponseEntity<String> createUser(@RequestBody RegisterRequest registerRequest) {
//        userService.createUser(registerRequest);
//        return ResponseEntity.ok("User registered successfully");
//    }

    //    @GetMapping("/user")
//    public ResponseEntity<UserDTO> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
//        System.out.println("UserDetails: " + userDetails); // Логируем полученные данные
//        Optional<User> optionalUser = userService.findByEmail(userDetails.getUsername());
//        if (optionalUser.isPresent()) {
//            UserDTO userDTO = optionalUser.get().toUserDTO();
//            return ResponseEntity.ok(userDTO);
//        } else {
//            System.out.println("User not found: " + userDetails.getUsername());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

    @PostMapping("/{lobbyId}/join")
    public ResponseEntity<Void> joinLobby(@RequestBody JoinLobbyRequest request) {
        lobbyService.joinToLobby(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lobbyId}/start")
    public ResponseEntity<Void> startGame(@PathVariable Long lobbyId, @RequestParam Long hostId) {
        lobbyService.startGame(lobbyId, hostId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{lobbyId}")
    public ResponseEntity<LobbyDTO> getLobby(@PathVariable Long lobbyId) {
        LobbyDTO lobby = lobbyService.getLobbyById(lobbyId);
        return lobby != null ? ResponseEntity.ok(lobby) : ResponseEntity.notFound().build();
    }

    // Удаление пользователя по ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
//        boolean isDeleted = userService.deleteUser(id);
//        if (isDeleted) {
//            return ResponseEntity.ok("User deleted successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//    }
//
//    // Обновление данных пользователя
//    @PutMapping("/{id}")
//    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
//        Optional<UserDTO> updatedUser = userService.updateUser(id, userDTO);
//        return updatedUser.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
//    }
}
