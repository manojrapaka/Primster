package tr.com.abay.repository;

import tr.com.abay.domain.Satis;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Satis entity.
 */
public interface SatisRepository extends JpaRepository<Satis,Long> {

}
