package example.websocket.infa.dto.response;

import example.websocket.aggreate.entity.CASHI;
import example.websocket.aggreate.entity.MGNI;
import lombok.*;

import javax.persistence.Access;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
//@XmlRootElement(name = "Transfer")
//@XmlAccessorType(XmlAccessType.FIELD)
public class ReadResponse {
//    private List<MGNI> mgniList;
    private List<DeatilReadResponse> deatilReadResponseList;
    private String message;
}
