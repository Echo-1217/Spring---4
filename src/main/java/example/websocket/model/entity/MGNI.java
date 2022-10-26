package example.websocket.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "mgni")
@XmlAccessorType(XmlAccessType.FIELD)
public class MGNI implements Serializable {
    @Id
    private String id;
    @Column
    private String time;
    @Column
    private String type;
    @Column(name = "cm_no")
    private String cmNo; // 會員代號
    @Column(name = "kac_type")
    private String kacType; //存入保管專戶別
    @Column(name = "bank_no")
    private String bankNo;
    @Column
    private String ccy; // 存入幣別
    @Column(name = "pv_type")
    private String pvType; //存入方式
    @Column(name = "bicacc_no")
    private String bicaccNo; // 實體/虛擬帳號
    @Column(name = "i_type")
    private String iType; // 存入類別
    @Column(name = "p_reason")
    private String pReason;
    @Column
    private BigDecimal amt;
    @Column(name = "ct_name")
    private String ctName;
    @Column(name = "ct_tel")
    private String ctTel;
    @Column
    private String status;
    @Column(name = "u_time")
    private String uTime;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mgniId", cascade = CascadeType.ALL)
    //  FetchType.EAGER表示一併載入所有屬性所對應的資料
    //  mappedBy CASHI 合併的變數名稱
    //  CascadeType.ALL 無論儲存、合併、 更新或移除，一併對被參考物件作出對應動作。
    private List<CASHI> cashiList;
}
