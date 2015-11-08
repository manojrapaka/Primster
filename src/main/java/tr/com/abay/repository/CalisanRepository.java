package tr.com.abay.repository;

import tr.com.abay.domain.Calisan;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Calisan entity.
 */
public interface CalisanRepository extends JpaRepository<Calisan,Long> {

}
