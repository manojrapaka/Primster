package tr.com.abay.repository;

import tr.com.abay.domain.Ulke;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ulke entity.
 */
public interface UlkeRepository extends JpaRepository<Ulke,Long> {

}
