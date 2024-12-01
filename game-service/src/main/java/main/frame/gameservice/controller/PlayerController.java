package main.frame.gameservice.controller;

import main.frame.gameservice.model.Player;
import main.frame.gameservice.service.PlayerService;
import main.frame.shared.dto.PlayerDTO;
import main.frame.shared.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<Void> createPlayer(@RequestBody UserDTO userDTO) {
     //   playerService.createPlayer(request.getUserId(), request.getLobbyId());
        playerService.createPlayer(userDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        try {
            Optional<PlayerDTO> optionalPlayer = playerService.getById(id);
            return optionalPlayer
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}