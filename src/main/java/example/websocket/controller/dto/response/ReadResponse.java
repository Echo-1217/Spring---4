package example.websocket.controller.dto.response;

import example.websocket.model.entity.MGNI;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
//@XmlRootElement(name = "Transfer")
//@XmlAccessorType(XmlAccessType.FIELD)
public class ReadResponse {
    private List<MGNI> mgniList;
    String message;
}
