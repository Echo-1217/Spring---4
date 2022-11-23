package example.websocket.aggreate.entity;

import example.websocket.infa.dto.request.CreateCommand;
import example.websocket.infa.dto.request.UpdateCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
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

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "mgni", cascade = CascadeType.ALL)
    private List<CASHI> cashiList;//  FetchType.EAGER表示一併載入所有屬性所對應的資料
//  mappedBy CASHI 合併的變數名稱

    public MGNI(CreateCommand command) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd" + "HHmmssSSS");
        Date current = Calendar.getInstance().getTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        checkCommand(command);
        this.id = "MGI" + sdFormat.format(current);
        this.time = dateTimeFormatter.format(LocalDateTime.now());
        this.type = "1";
        this.cmNo = command.getCmNo().toUpperCase();
        this.kacType = command.getKacType();
        this.bankNo = command.getBankNo();
        this.ccy = command.getCcy().toUpperCase();
        this.pvType = command.getPvType();
        this.bicaccNo = command.getBicaccNo();
        this.iType = command.getIType();
        this.pReason = "Root Aggregate";
        this.ctName = "Echo";
        this.ctTel = "0987654321";
        this.status = "0";
        this.uTime = dateTimeFormatter.format(LocalDateTime.now());
        this.cashiList = new ArrayList<>();

        command.getAcc().forEach((map -> map.keySet().forEach(acc -> {
            this.cashiList.add(CASHI.builder()
//                    .mgniId(this.getId().toUpperCase())
                    .accNo(acc)
                    .ccy(this.getCcy())
                    .amt(map.get(acc))
                    .build());
            this.amt = (null == (this.getAmt()) ? map.get(acc) : this.getAmt().add((map.get(acc))).setScale(2, RoundingMode.HALF_UP));
        })));

    }

    public void update(UpdateCommand command) {
//        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd" + "HHmmssSSS");
//        Date current = Calendar.getInstance().getTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        checkCommand(command);
        this.cmNo = command.getCmNo().toUpperCase();
        this.kacType = command.getKacType();
        this.bankNo = command.getBankNo();
        this.ccy = command.getCcy();
        this.pvType = command.getPvType();
        this.bicaccNo = command.getBicaccNo();
        this.iType = command.getIType();
        this.uTime = dateTimeFormatter.format(LocalDateTime.now());
        this.cashiList = new ArrayList<>();

        command.getAcc().forEach((map -> map.keySet().forEach(acc -> {
            this.cashiList.add(CASHI.builder()
//                    .mgniId(this.getId().toUpperCase())
                    .accNo(acc)
                    .ccy(this.getCcy())
                    .amt(map.get(acc))
                    .build());
            this.amt = (null == (this.getAmt()) ? map.get(acc) : this.getAmt().add((map.get(acc))).setScale(2, RoundingMode.HALF_UP));
        })));

    }


    public void checkCommand(CreateCommand command) {
        if (null != command) {
            if (command.getKacType().equals("2") && command.getAcc().size() > 1) {
                throw new RuntimeException("交割帳戶只能單筆");
            }
            if (command.getKacType().equals("1") && !command.getIType().isBlank()) {
                throw new RuntimeException("選擇交割結算基金時使用 : i_type");
            }
        } else {
            throw new RuntimeException("null command");
        }
    }

    public void checkCommand(UpdateCommand command) {
        if (null != command) {
            if (command.getKacType().equals("2") && command.getAcc().size() > 1) {
                throw new RuntimeException("交割帳戶只能單筆");
            }
            if (command.getKacType().equals("1") && !command.getIType().isBlank()) {
                throw new RuntimeException("選擇交割結算基金時使用 : i_type");
            }
        } else {
            throw new RuntimeException("null command");
        }
    }

    public void resetCashAmt(BigDecimal amt) {
        this.amt = amt;
        this.cashiList = null;
    }
}
