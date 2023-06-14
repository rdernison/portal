package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.Person;

@Repository
public interface PersonDao extends CrudRepository<Person, Long> {

	@Query(value="SELECT * FROM Person WHERE request_id=:requestId AND role='dev'", nativeQuery=true)
	public List<Person> getPeopleByDevEnvironment(long requestId);
	
	@Query(value="SELECT * FROM Person WHERE request_id=:requestId AND role='test'", nativeQuery=true)
	public List<Person> getPeopleByTestEnvironment(long requestId);
}
