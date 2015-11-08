package tr.com.abay.repository;

import tr.com.abay.domain.Prim;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prim entity.
 */
public interface PrimRepository extends JpaRepository<Prim,Long> {

}
