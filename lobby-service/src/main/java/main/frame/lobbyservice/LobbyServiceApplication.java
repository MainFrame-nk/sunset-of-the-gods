package main.frame.lobbyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"main.frame.shared", "main.frame"})  // Обязательно добавь sharedmodule
public class LobbyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LobbyServiceApplication.class, args);
    }

}
