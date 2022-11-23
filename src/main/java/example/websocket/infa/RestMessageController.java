package example.websocket.infa;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.websocket.infa.dto.request.CreateCommand;
import example.websocket.infa.dto.request.UpdateCommand;
import example.websocket.infa.dto.response.TransferResponse;
import example.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/transfer")
@Slf4j
public class RestMessageController {

    @Autowired
    WebSocketService webSocketService;

    @PostMapping(path = "/test")
    public void sendMessage(@RequestBody String message, @RequestParam String id) {
        log.info("Rest send message {} to user {}", message, id);
        webSocketService.notify(id, message);
    }
//    @PostMapping(path = "/create")
//    public void create(@RequestBody String msg, @RequestParam String id){
//        webSocketService.notify(id ,msg);
//    }
}
