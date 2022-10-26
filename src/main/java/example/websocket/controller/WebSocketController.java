package example.websocket.controller;


import example.websocket.service.WebSocketService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    @Autowired
    WebSocketService webSocketService;

    //加 @MessageMapping 注解，使其處理 STOMP 消息
    // Spring 的 Web 消息功能基於消息代理 （message broker）建構
    // 需要配置一個消息代理和其他消息目的地
    @MessageMapping("/message/{receiver}")
    public Boolean sendMessage(Principal principal, @Header String authKey, @DestinationVariable String receiver, @RequestBody WebSocketRequestMessage message) {
        log.info("Send message from {} to receiver {}. Auth key {}", principal.getName(), receiver, authKey);
        webSocketService.notify(receiver, message.getMessageContent());
        return Boolean.TRUE;
    }


    @Getter
    @Setter
    public static class WebSocketRequestMessage {
        // 前端傳回的資料由 java 物間包起來 才能做使用
        private String messageContent;

    }

}
