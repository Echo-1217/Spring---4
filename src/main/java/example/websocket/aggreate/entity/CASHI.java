package example.websocket.aggreate.entity;


import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor

@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@Table(name = "cashi")
//@IdClass(CASHI.CASHIID.class)
@XmlAccessorType(XmlAccessType.FIELD)
//@JsonIgnoreProperties(value= {"id"}) implements Serializable
public class CASHI  {
//    private static final Long serialVersionUID = 1L;


    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "mgni_id",referencedColumnName = "ID")
    private MGNI mgni;
    @Id
    @Column(name = "acc_no")
    private String accNo;
//    @Id
    @Column
    private String ccy;
    @Column
    private BigDecimal amt;

//    private MGNI mgniId;
//
//    @Data
//    public static class CASHIID implements Serializable {
//        private static final Long serialVersionUID = 1L;
//
//        private String mgniId;
//        private String accNo;
//        private String ccy;
//    }

//    public CASHI(String mgniId, String accNo, String ccy, BigDecimal amt) {
//        this.mgniId = mgniId;
//        this.accNo = accNo;
//        this.ccy = ccy.toUpperCase();
//        this.amt = amt;
//    }
}