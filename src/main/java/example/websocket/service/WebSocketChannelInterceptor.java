package example.websocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

@Service
@Slf4j
public class WebSocketChannelInterceptor implements ChannelInterceptor {
    static final String ApiKeyHeader = "authKey";
    static final String SessionKeyHeader = "simpSessionId";
    static final String WsIdHeader = "ws-id";

    //     Processes a message before sending it
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = readHeaderAccessor(message);

        if (accessor.getCommand() == StompCommand.CONNECT) {

            String wsId = readWebSocketIdHeader(accessor);
            String apiKey = readAuthKeyHeader(accessor);
            String sessionId = readSessionId(accessor);

            // authenticate the user and if that's successful add their user information to the headers.
            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(wsId, null);
            accessor.setUser(user);
            accessor.setHeader("connection-time", LocalDateTime.now().toString());
            log.info("User with authKey '{}', ws-id {} session {} make a WebSocket connection and generated the user {}", apiKey, wsId, sessionId, user);

        }

        return message;

    }

     // Instantiate an object for retrieving the STOMP headers
    private StompHeaderAccessor readHeaderAccessor(Message<?> message) {
        final StompHeaderAccessor accessor = getAccessor(message);
        if (accessor == null) {
            throw new AuthenticationCredentialsNotFoundException("Fail to read headers.");
        }
        return accessor;
    }
    // 驗證 id
    private String readSessionId(StompHeaderAccessor accessor) {
        return ofNullable(accessor.getMessageHeaders().get(SessionKeyHeader))  // 判定是否為 null true :　return .empty() ; false return .of(Value) ;
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Session header not found")).toString(); // else throws new exception
    }
    // 驗證 AuthKey
    private String readAuthKeyHeader(StompHeaderAccessor accessor) {
        final String authKey = accessor.getFirstNativeHeader(ApiKeyHeader);
        if (authKey == null || authKey.trim().isEmpty()) // 有可能 user 前後手殘按到 space 故使用 String.trim()
            throw new AuthenticationCredentialsNotFoundException("Auth Key Not Found");
        return authKey;
    }
    // 驗證 ws-id
    private String readWebSocketIdHeader(StompHeaderAccessor accessor) {
        final String wsId = accessor.getFirstNativeHeader(WsIdHeader);
        if (wsId == null || wsId.trim().isEmpty())
            throw new AuthenticationCredentialsNotFoundException("Web Socket ID Header not found");
        return wsId;
    }

    StompHeaderAccessor getAccessor(Message<?> message) {
        return MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    }


}
