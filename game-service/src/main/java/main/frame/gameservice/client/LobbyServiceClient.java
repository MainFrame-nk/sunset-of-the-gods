package main.frame.gameservice.client;

import main.frame.gameservice.dto.SimplePlayerDTO;
import main.frame.shared.dto.LobbyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class LobbyServiceClient {

    private final WebClient webClient;

    @Autowired
    public LobbyServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://lobby-service").build();
    }

    public LobbyDTO getLobbyById(Long lobbyId) {
        return webClient.get()
                .uri("/lobby/{id}", lobbyId)
                .retrieve()
                .bodyToMono(LobbyDTO.class)
                .block();
    }

//    public List<LobbyPlayerDTO> getPlayersInLobby(Long lobbyId) {
//        return webClient.get()
//                .uri("/lobby/{id}/players", lobbyId)
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<List<LobbyPlayerDTO>>() {})
//                .block();
//    }

    public List<SimplePlayerDTO> getPlayersInLobby(Long lobbyId) {
        // Запрос в LobbyService для получения игроков
        List<Map<String, Object>> lobbyPlayers = webClient.get()
                .uri("/lobby/{id}/players", lobbyId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();

        if (lobbyPlayers == null || lobbyPlayers.isEmpty()) {
            throw new IllegalArgumentException("В лобби нет игроков: " + lobbyId);
        }

        // Преобразование JSON в SimplePlayerDTO
        return lobbyPlayers.stream()
                .map(player -> SimplePlayerDTO.builder()
                        .id(((Number) player.get("playerId")).longValue())
                    //    .turnOrder((Integer) player.get("turnOrder"))
                 //       .status((String) player.get("status"))
                        .build())
                .toList();
    }


}
