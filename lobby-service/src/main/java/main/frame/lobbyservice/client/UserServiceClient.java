package main.frame.lobbyservice.client;

import main.frame.shared.dto.UserDTO;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserServiceClient {

    private final WebClient webClient;

    public UserServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service").build();
    }

    public User getUserById(Long userId) {
        return webClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }

//    public List<UserDTO> getUsersInLobby(List<Long> userIds) {
//        return Flux.fromIterable(userIds)
//                .flatMap(this::fetchUserById) // Асинхронно получаем данные для каждого ID
//                .collectList()
//                .block(); // Собираем результат в список
//    }
//
//    private Mono<UserDTO> fetchUserById(Long userId) {
//        return webClient.get()
//                .uri("/users/{id}", userId)
//                .retrieve()
//                .bodyToMono(UserDTO.class);
//    }

    //    private Boolean checkUserExists(Long userId) {
//        String userServiceUrl = "http://USER-SERVICE/users/" + userId + "/exists";
//
//        return webClientBuilder.build()
//                .get()
//                .uri(userServiceUrl)
//                .retrieve()
//                .bodyToMono(Boolean.class)
//                .block(); // Блокируемый вызов для синхронной проверки (можно заменить на async при необходимости)
//    }

}
