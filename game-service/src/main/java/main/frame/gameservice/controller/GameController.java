package main.frame.gameservice.controller;

import lombok.extern.slf4j.Slf4j;
import main.frame.shared.dto.PlayerDTO;
import main.frame.gameservice.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/game")
public class GameController {
    private final PlayerService playerService;

    public GameController(PlayerService playerService) {
        this.playerService = playerService;
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
//
//    @GetMapping("/email/{email}")
//    public ResponseEntity<User> findByEmail(@PathVariable String email) {
//        Optional<User> userOptional = userService.findByEmail(email);
//        return userOptional.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> findById(@PathVariable Long id) {
        Optional<PlayerDTO> optionalPlayer = playerService.getById(id);
        if (optionalPlayer.isPresent()) {
            return ResponseEntity.ok(optionalPlayer.get());
        } else {
            System.out.println("Игрок не найден, id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
//
//
//    // Получение всех пользователей
//    @GetMapping("/")
//    public ResponseEntity<List<UserDTO>> getAllUsers() {
//        List<UserDTO> users = userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }

//    @GetMapping("/test-user")
//    public ResponseEntity<String> testEndpoint(@RequestHeader Map<String, String> headers) {
//        headers.forEach((key, value) -> System.out.println(key + " -> " + value));
//        return ResponseEntity.ok("Headers logged");
//    }
//
//    @GetMapping("/debug-headers")
//    public ResponseEntity<?> debugHeaders(HttpServletRequest request) {
//        Enumeration<String> headers = request.getHeaderNames();
//        while (headers.hasMoreElements()) {
//            String header = headers.nextElement();
//            System.out.println(header + ": " + request.getHeader(header));
//        }
//        return ResponseEntity.ok().build();
//    }
}