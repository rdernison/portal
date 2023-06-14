package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.CostCenter;
// PKUN18 - UN0931 - DIENST
@Repository
public interface CostCenterDao extends JpaRepository<CostCenter, Long> {

	@Query("SELECT c FROM CostCenter c WHERE LOWER(c.code) LIKE LOWER(CONCAT('%', :value, '%')) OR LOWER(description) LIKE LOWER(CONCAT('%', :value, '%'))")
	List<CostCenter> findByName(@Param("value") String value);
	
	@Query("SELECT c FROM CostCenter c WHERE LOWER(c.code) LIKE 'pkun%' OR LOWER(c.code) LIKE 's%'")
	List<CostCenter> findByPkunOrS();
}
