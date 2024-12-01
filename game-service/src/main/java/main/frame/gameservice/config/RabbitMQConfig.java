package main.frame.gameservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Очередь, на которую будет подписываться GameService
    public static final String LOBBY_EVENTS_QUEUE = "lobbyEventsQueue"; // Общая очередь для событий лобби

    // Ключи маршрутизации
    public static final String LOBBY_EVENTS_ROUTING_KEY = "lobby.events";

    @Bean
    public Queue lobbyEventsQueue() {
        return new Queue(LOBBY_EVENTS_QUEUE);
    }

    // Обменник, на который подписывается GameService
    public static final String EXCHANGE = "lobby.exchange";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    // Связь очереди с обменником
    @Bean
    public Binding lobbyEventsBinding(Queue lobbyEventsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(lobbyEventsQueue).to(exchange).with(LOBBY_EVENTS_ROUTING_KEY);
    }
}
