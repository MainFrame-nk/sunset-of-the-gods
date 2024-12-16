package main.frame.lobbyservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import main.frame.lobbyservice.dto.request.JoinLobbyRequest;
import main.frame.lobbyservice.dto.response.CreateLobbyDTO;
import main.frame.lobbyservice.dto.response.LobbyPlayerDTO;
import main.frame.lobbyservice.model.LobbyStatus;
import main.frame.shared.dto.LobbyDTO;
import main.frame.lobbyservice.model.Lobby;
import main.frame.lobbyservice.service.LobbyService;
import main.frame.shared.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping("/")
    public ResponseEntity<List<LobbyDTO>> getAllLobbies(@RequestBody LobbyStatus status) {
        try {
            List<LobbyDTO> lobbies = lobbyService.getAllLobbies(status);
            if (lobbies.isEmpty()) {
                return ResponseEntity.noContent().build(); // Возвращаем 204, если список пуст
            }
            return ResponseEntity.ok(lobbies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<String> createLobby(@RequestBody @Valid CreateLobbyDTO createLobbyDTO) {
        try {
            Lobby lobby = lobbyService.createLobby(createLobbyDTO);
            JoinLobbyRequest request = new JoinLobbyRequest(lobby.getId(), createLobbyDTO.getHostId(), createLobbyDTO.getPassword());
            lobbyService.joinToLobby(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Лобби успешно создано!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при создании лобби!");
        }
    } // Проверить

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

    @GetMapping("/{id}")
    public ResponseEntity<LobbyDTO> findById(@PathVariable Long id) {
        try {
            Optional<LobbyDTO> optionalLobby = lobbyService.getLobbyById(id);
            return optionalLobby
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLobby(@PathVariable Long id) {
        try {
            boolean isDeleted = lobbyService.deleteLobby(id);
            if (isDeleted) {
                return ResponseEntity.ok("Пользователь успешно удален.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Пользователь с указанным ID не найден.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении пользователя.");
        }
    }

    @GetMapping("/{id}/players")
    public List<LobbyPlayerDTO> getPlayersInLobby(@PathVariable Long id) {
        return lobbyService.getPlayersInLobby(id);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<LobbyDTO> updateLobby(@PathVariable Long id, @RequestBody LobbyDTO lobbyDTO) {
//        try {
//            Optional<LobbyDTO> updatedLobby = lobbyService.updateLobby(id, lobbyDTO);
//            return updatedLobby
//                    .map(ResponseEntity::ok)
//                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
//                            .body(null)); // Возвращаем пустое тело в случае ошибки
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(null);
//        }
//    }

        @GetMapping("/test")
    public ResponseEntity<?> someEndpoint(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + request);
        // Логика обработки
        return ResponseEntity.ok().build();
    }
}
