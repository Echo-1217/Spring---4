package example.websocket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.websocket.controller.dto.request.CreateRequest;
import example.websocket.controller.dto.request.DeleteRequest;
import example.websocket.controller.dto.request.ReadRequest;
import example.websocket.controller.dto.request.UpdateRequest;
import example.websocket.controller.dto.response.ReadResponse;
import example.websocket.controller.dto.response.TransferResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TransferService transferService;

    public void notify(final String id, final String message) {
        this.send(id, message);
    }

    @SneakyThrows
    private void send(String id, String message) {
        try {
            JsonNode jsonNodes = objectMapper.readTree(message);
            String crud = jsonNodes.get("crud").asText();
            JsonNode request = jsonNodes.get("request");
            String json = request.toPrettyString();

            switch (crud) {
                case "read": {
                    ReadRequest readRequest = objectMapper.readValue(json, ReadRequest.class);
                    ReadResponse readResponse = transferService.readTransfer(readRequest);
                    log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(readResponse));
                    messagingTemplate.convertAndSendToUser(id, "/topic/messages", readResponse);
                    break;
                }
                case "create": {
                    CreateRequest createRequest = objectMapper.readValue(json, CreateRequest.class);
                    TransferResponse transferResponse = transferService.createTransfer(createRequest);
                    log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(transferResponse));
                    messagingTemplate.convertAndSendToUser(id, "/topic/messages", transferResponse);
                    break;
                }
                case "update": {
                    UpdateRequest updateRequest = objectMapper.readValue(json, UpdateRequest.class);
                    TransferResponse transferResponse = transferService.updateMGNI(updateRequest);
                    log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(transferResponse));
                    messagingTemplate.convertAndSendToUser(id, "/topic/messages", transferResponse);
                    break;
                }
                case "delete": {
                    DeleteRequest deleteRequest = objectMapper.readValue(json, DeleteRequest.class);
                    TransferResponse transferResponse = transferService.deleteTransfer(deleteRequest);
                    log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(transferResponse));
                    messagingTemplate.convertAndSendToUser(id, "/topic/messages", transferResponse);
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
