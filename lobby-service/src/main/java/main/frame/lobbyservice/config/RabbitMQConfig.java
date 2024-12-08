package main.frame.lobbyservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // Очереди
    public static final String LOBBY_QUEUE = "lobby.queue";
    public static final String LOBBY_EVENTS_QUEUE = "lobby.events.queue"; // Для событий в лобби
//    public static final String PLAYER_ACTIONS_QUEUE = "playerActionsQueue"; // Для действий игроков

    // Общий обменник
    public static final String EXCHANGE = "lobby.exchange";

    // Ключи маршрутизации
    public static final String LOBBY_EVENTS_ROUTING_KEY = "lobby.events";
//    public static final String LOBBY_EVENTS_ROUTING_KEY = "lobby.events";
//    public static final String PLAYER_ACTIONS_ROUTING_KEY = "player.actions";


    // Настройка очереди
    @Bean
    public Queue queue() {
        return new Queue(LOBBY_QUEUE);
    }

    @Bean
    public Queue lobbyEventsQueue() {
        return new Queue(LOBBY_EVENTS_QUEUE, true); // true для персистентности
    }
//
//    @Bean
//    public Queue playerActionsQueue() {
//        return new Queue(PLAYER_ACTIONS_QUEUE);
//    }

    // Настройка обменника
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    // Связывание очереди и обменника с использованием ключа маршрутизации
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(LOBBY_EVENTS_ROUTING_KEY);
    }

//    @Bean
//    public Binding lobbyEventsBinding(Queue lobbyEventsQueue, TopicExchange exchange) {
//        return BindingBuilder.bind(lobbyEventsQueue).to(exchange).with(LOBBY_EVENTS_ROUTING_KEY);
//    }
//
//    @Bean
//    public Binding playerActionsBinding(Queue playerActionsQueue, TopicExchange exchange) {
//        return BindingBuilder.bind(playerActionsQueue).to(exchange).with(PLAYER_ACTIONS_ROUTING_KEY);
//    }
}
