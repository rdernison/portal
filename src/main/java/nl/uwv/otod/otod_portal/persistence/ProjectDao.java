
package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.Project;

@Repository
public interface ProjectDao extends CrudRepository<Project, Long> {
	
	@Query("select p from Project p where p.name = :name")
	public List<Project> findByName(@Param("name") String name);
	
	@Query("select p from Project p where p.status = :status")
	public List<Project> findByStatus(@Param("status") String status);

	@Query("select p from Project p where lower(p.name) like lower(concat('%', :name, '%')) and lower(p.status) like lower(concat('%',:status,'%'))")
	public List<Project> findByNameAndStatus(@Param("name") String name, @Param("status") String status);
	
	@Query("select p from Project p order by p.name")
	public List<Project> findAllOrderedByName();
}
