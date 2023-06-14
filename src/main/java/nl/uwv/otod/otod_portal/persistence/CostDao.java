package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

import nl.uwv.otod.otod_portal.model.Cost;
import nl.uwv.otod.otod_portal.model.CostType;

@Repository
public interface CostDao extends JpaRepository<Cost, Long>{

	List<Cost> findByType(CostType type);
	@Query("SELECT c FROM Cost c WHERE c.type=:type AND c.creationTime=(SELECT MAX(c0.creationTime) FROM Cost c0)")
	Optional<Cost> findLatestByType(@Param("type") CostType type);
}
