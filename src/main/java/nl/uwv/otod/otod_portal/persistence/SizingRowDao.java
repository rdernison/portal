package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.SizingRow;

@Repository
public interface SizingRowDao extends CrudRepository<SizingRow, Long> {
	
	@Query("SELECT s FROM SizingRow s WHERE lower(s.status) like lower(concat('%', :status, '%')) "
		+ "AND lower(s.applicationName) like lower(concat('%', :applicationName, '%')) "
		+ "AND lower(s.division) like lower(concat('%', :division, '%')) "
		+ "AND lower(s.os) LIKE lower(concat('%', :os, '%'))")
	List<SizingRow> findByStatusApplicationNameDivisionOs(@Param("status") String status, @Param("applicationName") String applicationName, @Param("division") String division, @Param("os") String os);
}
