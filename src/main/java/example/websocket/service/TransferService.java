package example.websocket.service;


import example.websocket.infa.dto.request.CreateCommand;
import example.websocket.infa.dto.request.DeleteRequest;
import example.websocket.infa.dto.request.ReadRequest;
import example.websocket.infa.dto.request.UpdateCommand;
import example.websocket.infa.dto.response.DeatilReadResponse;
import example.websocket.infa.dto.response.ReadResponse;
import example.websocket.infa.dto.response.TransferResponse;
//import example.websocket.aggreate.CashiRepository;
import example.websocket.aggreate.MgniRepository;
import example.websocket.aggreate.entity.MGNI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TransferService {

    @Autowired
    MgniRepository mgniRepository;

    public ReadResponse getAllTransfer() {
        ReadResponse response = new ReadResponse();
        List<MGNI> mgniList = mgniRepository.findAll();
        if (mgniList.isEmpty()) {
            return response.toBuilder().message("尚未有任何資料....").build();
        }
//        response.setMgniList(mgniList);
        mgniList.forEach(mgni -> mgni.getCashiList().forEach(cashi -> response.toBuilder().deatilReadResponseList(List.of(DeatilReadResponse.builder()
                        .id(cashi.getMgni().getId())
                        .time(cashi.getMgni().getTime())
                        .cm_no(cashi.getMgni().getCmNo())
                        .kac_type(cashi.getMgni().getKacType())
                        .bank_no(cashi.getMgni().getBankNo())
                        .ccy(cashi.getCcy())
                        .pv_type(cashi.getMgni().getPvType())
                        .bicacc_no(cashi.getMgni().getBicaccNo())
                        .i_type(cashi.getMgni().getIType())
                        .p_reason(cashi.getMgni().getPReason())
                        .amt(cashi.getMgni().getAmt())
                        .ct_name(cashi.getMgni().getCtName())
                        .ct_tel(cashi.getMgni().getCtTel())
                        .status(cashi.getMgni().getStatus())
                        .u_time(cashi.getMgni().getUTime())
//                        .cashiList()

                .build()))));
//        response.setMessage("read success");

        return response;
    }

    public ReadResponse readTransfer(ReadRequest request) {

        ReadResponse response = new ReadResponse();

        List<MGNI> mgniList = filterMgni(request);

        if (!mgniList.isEmpty()) {
//            response.setMgniList(mgniList);
            mgniList.forEach(mgni -> response.getDeatilReadResponseList().add(DeatilReadResponse.builder().cashiList(mgni.getCashiList()).build()));
//            mgniList.forEach(mgni -> response.setCashiList(mgni.getCashiList()));
//            response.setMessage("read success");
            return response;
        }
//        response.setMessage("查無結果");
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
        Optional<MGNI> optionalMGNI =mgniRepository.findById(request.getId().toUpperCase());
        if (optionalMGNI.isPresent()) {
            response.setMgni(optionalMGNI.get());
            mgniRepository.deleteById(request.getId().toUpperCase());// cascadeType=All 所以不需要 delete cashi
            response.setMessage("deleted success");
            return response;
        }
        response.setMessage("查無結果");
        return response;
    }

    @Transactional
    public TransferResponse createTransfer(CreateCommand createCommand) {
//        TransferResponse response = new TransferResponse();

        MGNI mgni = new MGNI(createCommand);


//        if (message.equals("ok")) {
//            response.setMgni(mgni);
//            response.setMessage("create success");
//        } else {
//            response.setMessage(message);
//        }
        mgniRepository.save(mgni);

        return TransferResponse.builder()
                .mgni(mgni)
                .message("create success")
                .build();
    }

    @Transactional
    public TransferResponse updateMGNI(UpdateCommand command) {


        // 撈資料庫
        Optional<MGNI> optionalMGNI = mgniRepository.findById(command.getId().toUpperCase());
        if (optionalMGNI.isEmpty()) {
            return TransferResponse.builder().message("查無結果....").build();
        }

         // 歸0
        optionalMGNI.get().resetCashAmt(BigDecimal.ZERO);
        // delete the old cash detail
        // ============ update ===========
        optionalMGNI.get().update(command);

        mgniRepository.save(optionalMGNI.get());
        return TransferResponse.builder()
                .mgni(optionalMGNI.get())
                .message("update success").build();
    }

}
