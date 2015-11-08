package tr.com.abay.repository;

import tr.com.abay.domain.History;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the History entity.
 */
public interface HistoryRepository extends JpaRepository<History,Long> {

}
