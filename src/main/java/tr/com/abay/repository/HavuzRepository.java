package tr.com.abay.repository;

import tr.com.abay.domain.Havuz;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Havuz entity.
 */
public interface HavuzRepository extends JpaRepository<Havuz,Long> {

}
