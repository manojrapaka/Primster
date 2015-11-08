package tr.com.abay.repository;

import tr.com.abay.domain.Hakedis;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Hakedis entity.
 */
public interface HakedisRepository extends JpaRepository<Hakedis,Long> {

}
