package main.frame.gameservice.client;

import main.frame.shared.dto.LobbyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
}
