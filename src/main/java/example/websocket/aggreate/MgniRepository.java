package example.websocket.aggreate;

import example.websocket.aggreate.entity.CASHI;
import example.websocket.aggreate.entity.MGNI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MgniRepository extends JpaRepository<MGNI, String>, JpaSpecificationExecutor<MGNI> {

}
