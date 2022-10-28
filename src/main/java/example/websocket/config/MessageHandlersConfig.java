package example.websocket.config;

import example.websocket.handler.ConnectHandler;
import example.websocket.handler.DisconnectHandler;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

// 埋 Bean 就像埋 queue/topic 同理
@Configuration
public class MessageHandlersConfig<S extends Session> {

    @Bean
    public ConnectHandler<S> connectHandler(SimpMessageSendingOperations messagingTemplate) {
        return new ConnectHandler<>(messagingTemplate);
    }

    @Bean
    public DisconnectHandler<S> disconnectHandler(SimpMessageSendingOperations messagingTemplate) {
        return new DisconnectHandler<>(messagingTemplate);
    }

}
