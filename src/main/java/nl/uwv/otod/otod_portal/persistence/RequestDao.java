package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.model.User;

@Repository
public interface RequestDao extends CrudRepository<Request, Long> {

	@Query("select r from Request r order by requestDate desc")
	List<Request> findAllOrderedByCreationDateDesc();

	@Query("select r from Request r where r.user=:user")
	Iterable<Request> findByUser(@Param("user") User user);

	@Query("select r from Request r where r.user.emailAddress=:emailAddress")
	Iterable<Request> findByEmailAddress(@Param("emailAddress") String emailAddress);

	@Query("select r from Request r where r.emailAddressServiceRequester=:emailAddress order by requestDate desc")
	Iterable<Request> findByEmailAddressServiceRequester(@Param("emailAddress") String emailAddress);
	
	
	@Query("select r from Request r where r.saveToEdit <> :submitted order by requestDate desc")
	Iterable<Request> findBySubmitted(@Param("submitted") boolean submited);
}
