package tr.com.abay.repository;

import tr.com.abay.domain.Gorev;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Gorev entity.
 */
public interface GorevRepository extends JpaRepository<Gorev,Long> {

}
