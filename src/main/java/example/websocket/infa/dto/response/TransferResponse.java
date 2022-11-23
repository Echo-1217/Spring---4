package example.websocket.infa.dto.response;

import example.websocket.aggreate.entity.MGNI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@XmlRootElement(name = "Detail")
//@XmlAccessorType(XmlAccessType.FIELD)
public class TransferResponse {
    private MGNI mgni;
    private String message;
}