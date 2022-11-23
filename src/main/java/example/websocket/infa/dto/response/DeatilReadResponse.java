package example.websocket.infa.dto.response;

import example.websocket.aggreate.entity.CASHI;
import lombok.Builder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Builder
public class DeatilReadResponse {
    private String id;
    private String time;
    private String type;
    private String cm_no; // 會員代號
    private String kac_type; //存入保管專戶別
    private String bank_no;
    private String ccy; // 存入幣別
    private String pv_type; //存入方式
    private String bicacc_no; // 實體/虛擬帳號
    private String i_type; // 存入類別
    private String p_reason;
    private BigDecimal amt;
    private String ct_name;
    private String ct_tel;
    private String status;
    private String u_time;
    private List<CASHI> cashiList;
}
