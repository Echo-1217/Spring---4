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

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "cashi")
@IdClass(CASHI.CASHIID.class)
@XmlAccessorType(XmlAccessType.FIELD)
//@JsonIgnoreProperties(value= {"id"})
public class CASHI implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "mgni_id")
    private String mgniId;
    @Id
    @Column(name = "acc_no")
    private String accNo;
    @Id
    @Column
    private String ccy;
    @Column
    private BigDecimal amt;


    @Data
    public static class CASHIID implements Serializable {
        private static final Long serialVersionUID = 1L;

        private String mgniId;
        private String accNo;
        private String ccy;
    }

}