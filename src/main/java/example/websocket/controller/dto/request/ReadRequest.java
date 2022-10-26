package example.websocket.controller.dto.request;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class ReadRequest {

    @NotNull(message = "欄位不能為null")
    private String id;

    @NotNull(message = "欄位不能為null")
    @Length(max = 7, message = "length > 7")
    private String cmNo; // 會員代號

    @NotNull(message = "欄位不能為null")
    @Length(max = 21, message = "length > 21")
    private String bicaccNo; // 實體/虛擬帳號
}
