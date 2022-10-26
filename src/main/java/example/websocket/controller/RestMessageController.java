//package example.websocket.controller;
//
//import example.websocket.service.WebSocketService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(path = "api/v1/transfer")
//@Slf4j
//public class RestMessageController {
//
//    @Autowired
//    WebSocketService webSocketService;
//
//    @PostMapping(path = "/test")
//    public void sendMessage(@RequestBody String message, @RequestParam String id) {
//        log.info("Rest send message {} to user {}", message, id);
//        webSocketService.notify(id, message);
//    }
//
//}
