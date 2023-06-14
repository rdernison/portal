package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.Middleware;

@Repository
public interface MiddlewareDao extends CrudRepository<Middleware, Long> {
	
	@Query("from Middleware where enabled = true")
	public List<Middleware> findEnabled();

}
