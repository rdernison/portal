package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.Os;

@Repository
public interface OsDao extends CrudRepository<Os, Long> {

	@Query("select o from Os o where o.name = :name")
	List<Os> findByName(String name);

	@Query("select o from Os o where o.name is not null and length(o.name) > 0 order by o.name")
	List<Os> findAll();
	
	@Query("select o from Os o where o.name is not null and length(o.name) > 0 and o.enabled = true order by o.name")
	List<Os> findAllEnabled();
	
	@Query("select o from Os o where o.name = :name and o.enabled = true")
	List<Os> findEnabledByName(String name);
}
