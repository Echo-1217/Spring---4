package example.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.websocket.controller.dto.request.CreateRequest;
import example.websocket.controller.dto.request.DeleteRequest;
import example.websocket.controller.dto.request.ReadRequest;
import example.websocket.controller.dto.request.UpdateRequest;
import lombok.*;
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
            Object obj = processingRequestMessage(crud, json);
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
            messagingTemplate.convertAndSendToUser(id, "/topic/messages", obj);
        } catch (Exception exception) {
            log.error(exception.getLocalizedMessage());
            messagingTemplate.convertAndSendToUser(id, "/topic/messages",new ErrTempResponse(exception.getMessage(),message));
        }
    }

    private Object processingRequestMessage(String crud, String json) throws JsonProcessingException {
        switch (crud) {
            case "read": {
                ReadRequest readRequest = objectMapper.readValue(json, ReadRequest.class);
                return transferService.readTransfer(readRequest);
            }
            case "create": {
                CreateRequest createRequest = objectMapper.readValue(json, CreateRequest.class);
                return transferService.createTransfer(createRequest);
            }
            case "update": {
                UpdateRequest updateRequest = objectMapper.readValue(json, UpdateRequest.class);
                return transferService.updateMGNI(updateRequest);
            }
            case "delete": {
                DeleteRequest deleteRequest = objectMapper.readValue(json, DeleteRequest.class);
                return transferService.deleteTransfer(deleteRequest);
            }
            default:
                return transferService.getAllTransfer();
        }
    }

    @Data
    @AllArgsConstructor
    private static class ErrTempResponse {
        String errorMessage;
        String request;
    }
}
