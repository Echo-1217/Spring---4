package example.websocket.model;

import example.websocket.model.entity.MGNI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MgniRepository extends JpaRepository<MGNI, String>, JpaSpecificationExecutor<MGNI> {
}
