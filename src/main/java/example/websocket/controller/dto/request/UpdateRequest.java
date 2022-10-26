package example.websocket.controller.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class UpdateRequest {

    @NotBlank(message = "id is needed")
    private String id;

    @NotBlank(message = "cmNo is needed")
    @Length(max = 7, message = "length >7")
    private String cmNo; // 會員代號

    @NotBlank(message = "kacType is needed")
    @Length(max = 1, message = "length > 1")
    private String kacType; //存入保管專戶別

    @NotBlank(message = "bankNo is needed")
    @Length(max = 3, message = "length > 3")
    private String bankNo; //

    @NotBlank(message = "ccy is needed")
    @Length(max = 3, message = "length > 3")
    private String ccy; // 存入幣別

    @NotBlank(message = "pvType is needed")
    @Length(max = 1, message = "length > 1")
    private String pvType; //存入方式

    @NotBlank(message = "bicaccNo is needed")
    @Length(max = 21, message = "length > 21")
    private String bicaccNo; // 實體/虛擬帳號

    @NotEmpty(message = "acc is needed")
    private List<Map<String, BigDecimal>> acc;

    @NotNull(message = "iType is needed")
    @Length(max = 1, message = "length > 1")
    private String iType; // 存入類別
}
