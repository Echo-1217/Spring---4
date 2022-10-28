package example.websocket.config;

import example.websocket.service.WebSocketChannelInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// 設置消息連接請求的各種規範信息。
@Configuration
// 註解開啟Websocket的支持 並實作 WebSocketMessageBrokerConfigurer
// @EnableWebSocketMessageBroker 註解表示開啟使用STOMP協議來傳輸基於代理的消息，Broker就是代理。
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    WebSocketChannelInterceptor channelInterceptor;

    // @Value 對應到 application.yml 中的宣告值
    @Value("${broker.host}")
    private String brokerHost;

    @Value("${broker.port}")
    private int brokerPort;

    @Value("${broker.username}")
    private String brokerUser;

    @Value("${broker.password}")
    private String brokerPass;

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        // 表示添加了一個/ws-register端點，客戶端就可以通過這個端點來進行連接。
        registry.addEndpoint("/ws-register")
                .setAllowedOriginPatterns("*") // 允許任何來源
                .withSockJS();
    }

    // 透過 active MQ 讓兩個獨立的 application run 可以連接彼此
    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/queue", "/topic");

        registry.enableStompBrokerRelay("/queue", "/topic")
                .setRelayHost(brokerHost)
                .setRelayPort(brokerPort)
                .setClientLogin(brokerUser)
                .setClientPasscode(brokerPass)
                .setSystemLogin(brokerUser)
                .setSystemPasscode(brokerPass)
                .setUserDestinationBroadcast("/topic/unresolved-user")
                .setUserRegistryBroadcast("/topic/log-user-registry");

        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);// 配置客戶端入站通道攔截器
    }


}
