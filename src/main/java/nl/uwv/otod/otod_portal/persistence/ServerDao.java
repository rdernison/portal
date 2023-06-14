package nl.uwv.otod.otod_portal.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;

@Repository
public interface ServerDao extends CrudRepository<Server, Long> {

	
	@Query("SELECT s FROM Server s ORDER BY s.project.name, s.name")
	List<Server> findAllOrdered();

	@Query("SELECT s FROM Server s WHERE s.project=:project ORDER BY s.project.name, s.name")
	List<Server> findByProject(@Param("project") Project project);

	@Query("SELECT s FROM Server s WHERE s.status='Operational' ORDER BY s.project.name, s.name")
	List<Server> findOperational();

	@Query("SELECT s FROM Server s WHERE lower(s.status)=lower(:status) ORDER BY s.project.name, s.name")
	List<Server> findByStatus(@Param("status") String status);
	
	@Query("SELECT s FROM Server s WHERE s.name=:name")
	List<Server> findByName(@Param("name") String name);

	@Query("SELECT s FROM Server s WHERE lower(s.name) like lower(concat('%', :name, '%')) and lower(s.os.name) like lower(:os)")
	List<Server> findByNameAndOs(@Param("name") String name, @Param("os") String os);
	
	/* skipping
	@Query("SELECT s FROM Server s WHERE lower(s.name) like lower(:name) and lower(s.os) like lower(:os) and lower(s.status) like lower(:status)")
	List<Server> findByNameOsStatus(String name, String os, String status);

	@Query("SELECT s FROM Server s WHERE lower(s.name) like lower('%:name%') and (:os = null or lower(s.os) like lower('%:os%')) and lower(s.status) like lower('%:status%')") 
	List<Server> findByNameOsStatusProductionDate(String name, String os, String status, LocalDate productionDate);
	*/

	@Query("SELECT s FROM Server s WHERE lower(s.status) like lower(concat('%',:status,'%')) and (:productionDate is null or s.productionDate = :productionDate)")
	List<Server> findByStatusProductionDate(@Param("status") String status, @Param("productionDate") LocalDate productionDate);
	
	@Query("SELECT s FROM Server s WHERE (:os = null or lower(s.os) like lower('%:os%')) and lower(s.status) like lower('%:status%') and (:productionDate is null or s.productionDate = :productionDate)")
	List<Server> findByOsStatusProductionDate(@Param("status") String status, @Param("productionDate") LocalDate productionDate);
	
	@Query("SELECT s FROM Server s WHERE  (:productionDate is null or s.productionDate = :productionDate)")
	List<Server> findByProductionDate(@Param("productionDate") LocalDate productionDate);
	
/* TODO production date between x and y
	@Query("SELECT s FROM Server s WHERE lower(s.name) like lower(:name) and lower(s.os) like lower(:os) and s.productionDate = :productionDate and s. = :relation")
	List<Server> findByNameOsStatusProductionDates(String name, String os, String status, LocalDate productionDateBegin, LocalDate productionDateEnd, String relation);
*/
	
	@Query("SELECT s "
			+ "FROM Server s "
			+ "WHERE lower(s.name) LIKE lower(CONCAT('%', :name, '%')) "
			+ "AND (s.os is null or lower(s.os.name) LIKE lower(CONCAT('%',:os,'%'))) "
			+ "AND (s.project is null OR lower(s.project.name) LIKE lower(CONCAT('%', :projectName, '%')))")
	List<Server> findByNameOsAndProjectName(@Param("name") String name, @Param("os") String os, @Param("projectName") String projectName);

	@Query("SELECT s FROM Server s WHERE s.os=:os ORDER BY s.project.name, s.name")
	List<Server> findByOs(@Param("os") Os os);

}
