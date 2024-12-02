package main.frame.lobbyservice.client;

import main.frame.shared.dto.PlayerDTO;
import main.frame.shared.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GameServiceClient {

    private final WebClient webClient;

    @Autowired
    public GameServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://game-service").build();
    }

    public PlayerDTO getPlayerById(Long playerId) {
        return webClient.get()
                .uri("/players/{id}", playerId)
                .retrieve()
                .bodyToMono(PlayerDTO.class)
                .block();
    }

    public List<PlayerDTO> getPlayersInLobby(List<Long> playerId) {
        return Flux.fromIterable(playerId)
                .flatMap(this::fetchPlayerById) // Асинхронно получаем данные для каждого ID
                .collectList()
                .block(); // Собираем результат в список
    }

    private Mono<PlayerDTO> fetchPlayerById(Long playerId) {
        return webClient
                .get()
                .uri("/players/{id}", playerId)
                .retrieve()
                .bodyToMono(PlayerDTO.class);
    }

    public void deleteLobbyPlayerCards(Long lobbyId) {
        webClient.delete()
                .uri("http://game-service/lobby-players/" + lobbyId + "/cards")
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    //    private void createPlayerForGame(Long lobbyId, Long userId) {
//        String gameServiceUrl = "http://GAME-SERVICE/players";
//
//        webClientBuilder.build()
//                .post()
//                .uri(gameServiceUrl)
//                .bodyValue(new CreatePlayerRequest(userId, lobbyId))
//                .retrieve()
//                .bodyToMono(Void.class)
//                .block();
//    }
}
