package ua.nekl08.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@EnableWebSocketMessageBroker
public class WebApplication extends AbstractWebSocketMessageBrokerConfigurer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebApplication.class, args);
    }

    public static ConcurrentHashMap<String, Integer> roomsAndUsers = new ConcurrentHashMap<>();

    public static void plusUserInRoom(String room){
        roomsAndUsers.merge(room, 1, (v1, v2) -> v1 + 1);
    }

    public static void minusUserInRoom(String room){
        roomsAndUsers.computeIfPresent(room, (s, integer) -> {
            if((integer - 1) == 0) return null;
            return integer - 1;
        });
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/channel");
//    registry.enableStompBrokerRelay(... насроить внешний брокер, например RabbitMQ
    }
}
