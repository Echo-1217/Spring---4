package example.websocket.controller.dto.response;

import example.websocket.model.entity.MGNI;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@XmlRootElement(name = "Detail")
//@XmlAccessorType(XmlAccessType.FIELD)
public class TransferResponse {
    private MGNI mgni;
    private String message;
}