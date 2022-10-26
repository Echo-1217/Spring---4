package example.websocket.service;



import example.websocket.controller.dto.request.CreateRequest;
import example.websocket.controller.dto.request.DeleteRequest;
import example.websocket.controller.dto.request.ReadRequest;
import example.websocket.controller.dto.request.UpdateRequest;
import example.websocket.controller.dto.response.ReadResponse;
import example.websocket.controller.dto.response.TransferResponse;
import example.websocket.model.entity.CASHI;
import example.websocket.model.entity.MGNI;
import example.websocket.model.CashiRepository;
import example.websocket.model.MgniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    CashiRepository cashiRepository;
    @Autowired
    MgniRepository mgniRepository;

    public ReadResponse getAllTransfer() {
        ReadResponse response = new ReadResponse();
        response.setMgniList(mgniRepository.findAll());
        response.setMessage("read success");
        return response;
    }

    public ReadResponse readTransfer(ReadRequest request) {

        ReadResponse response = new ReadResponse();

        List<MGNI> mgniList = filterMgni(request);

        if (!mgniList.isEmpty()) {
            response.setMgniList(mgniList);
            response.setMessage("read success");
            return response;
        }
        response.setMessage("查無結果");
        return response;
    }

    private List<MGNI> filterMgni(ReadRequest request) {
        Specification<MGNI> spec = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!request.getId().isEmpty()) {
                predicates.add(builder.equal(root.get("id"), request.getId().toUpperCase()));
            }
            if (!request.getCmNo().isEmpty()) {
                predicates.add(builder.equal(root.get("cmNo"), request.getCmNo().toUpperCase()));
            }
            if (!request.getBicaccNo().isEmpty()) {
                predicates.add(builder.equal(root.get("bicaccNo"), request.getBicaccNo()));
            }
            // order
            query.orderBy(builder.desc(root.get("time")));

            // 由於介面定義  Predicate and(Predicate... var1);  其中 參數接收一個到多個 Predicate 物件
            // 故使用  Predicate[] p 來進行轉換
            Predicate[] p = new Predicate[predicates.size()];
            return builder.and(predicates.toArray(p));
//            return builder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(0, 10);
        Page<MGNI> mgniPage = mgniRepository.findAll(spec, pageable);
        return mgniPage.getContent();
    }

    @Transactional
    public TransferResponse deleteTransfer(DeleteRequest request) {
        TransferResponse response = new TransferResponse();

        if (mgniRepository.findById(request.getId().toUpperCase()).isPresent()) {
            response.setMgni(mgniRepository.findById(request.getId().toUpperCase()).get());
            mgniRepository.deleteById(request.getId().toUpperCase());// cascadeType=All 所以不需要 delete cashi
            response.setMessage("deleted success");
            return response;
        }
        response.setMessage("查無結果");
        return response;
    }

    @Transactional
    public TransferResponse createTransfer(CreateRequest createRequest) {
        TransferResponse response = new TransferResponse();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd" + "HHmmssSSS");

        MGNI mgni = new MGNI();
        Date current = Calendar.getInstance().getTime();
        mgni.setId("MGI" + sdFormat.format(current));
        String message = setMGNI(mgni, createRequest, null);

        if (message.equals("ok")) {
            response.setMgni(mgni);
            response.setMessage("create success");
        } else {
            response.setMessage(message);
        }

        return response;
    }

    @Transactional
    public TransferResponse updateMGNI(UpdateRequest updateRequest) {

        TransferResponse response = new TransferResponse();

        // delete the old cash detail
        cashiRepository.deleteByMGNI_ID(updateRequest.getId().toUpperCase());

        MGNI mgni = new MGNI();

        if (mgniRepository.findById(updateRequest.getId().toUpperCase()).isEmpty()) {
            response.setMessage("查無結果....");
            return response;
        }
        mgni = mgniRepository.findById(updateRequest.getId().toUpperCase()).get();

        mgni.setAmt(BigDecimal.ZERO); // 歸0
        String message = setMGNI(mgni, null, updateRequest);

        if (message.equals("ok")) {
            response.setMgni(mgni);
            response.setMessage("update success");
        } else {
            response.setMessage(message);
        }

        return response;
    }

    private String setMGNI(MGNI mgni, CreateRequest createRequest, UpdateRequest updateRequest) {
        List<CASHI> cashList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (null != updateRequest) {
            if (updateRequest.getKacType().equals("2") && updateRequest.getAcc().size() > 1) {
                return "交割帳戶只能單筆";
            }
            if (updateRequest.getKacType().equals("1") && !updateRequest.getIType().isBlank()) {
                return "選擇交割結算基金時使用 : i_type ";
            }
            mgni.setCmNo(updateRequest.getCmNo().toUpperCase());
            mgni.setKacType(updateRequest.getKacType());
            mgni.setBankNo(updateRequest.getBankNo());
            mgni.setCcy(updateRequest.getCcy().toUpperCase());
            mgni.setPvType(updateRequest.getPvType());
            mgni.setBicaccNo(updateRequest.getBicaccNo());
            mgni.setIType(updateRequest.getIType());

            updateRequest.getAcc().forEach((map -> map.keySet().forEach(acc -> {

                cashList.add(createCASHI(mgni.getId().toUpperCase(), acc, mgni.getCcy(), map.get(acc)));
                mgni.setAmt(null == (mgni.getAmt()) ? map.get(acc) : mgni.getAmt().add((map.get(acc))).setScale(2, RoundingMode.HALF_UP));
            })));
        }
        if (null != createRequest) {
            if (createRequest.getKacType().equals("2") && createRequest.getAcc().size() > 1) {
                return "交割帳戶只能單筆";
            }
            if (createRequest.getKacType().equals("1") && !createRequest.getIType().isBlank()) {
                return "選擇交割結算基金時使用 : i_type";
            }
            mgni.setTime(dateTimeFormatter.format(LocalDateTime.now())); // 建立時間
            mgni.setCmNo(createRequest.getCmNo().toUpperCase());
            mgni.setKacType(createRequest.getKacType());
            mgni.setBankNo(createRequest.getBankNo());
            mgni.setCcy(createRequest.getCcy().toUpperCase());
            mgni.setPvType(createRequest.getPvType());
            mgni.setBicaccNo(createRequest.getBicaccNo());
            mgni.setIType(createRequest.getIType());

            createRequest.getAcc().forEach((map -> map.keySet().forEach(acc -> {
                cashList.add(createCASHI(mgni.getId().toUpperCase(), acc, mgni.getCcy(), map.get(acc)));
                mgni.setAmt(null == (mgni.getAmt()) ? map.get(acc) : mgni.getAmt().add((map.get(acc))).setScale(2, RoundingMode.HALF_UP));
            })));
        }

        mgni.setType("1");
        mgni.setPReason("test");
        mgni.setCtName("Echo");
        mgni.setCtTel("0987654321");
        mgni.setUTime(dateTimeFormatter.format(LocalDateTime.now()));  // 異動時間
        mgni.setStatus("0");
        mgni.setCashiList(cashList);

        mgniRepository.save(mgni);
        return "ok";
    }


    public CASHI createCASHI(String mgniId, String accNo, String ccy, BigDecimal amt) {
        CASHI cashi = new CASHI();

        cashi.setMgniId(mgniId);
        cashi.setCcy(ccy);
        cashi.setAccNo(accNo);
        cashi.setAmt(amt.setScale(2, RoundingMode.HALF_UP));

        // cascadeType=All 所以不需要 save cashi
        return cashi;
    }

}
