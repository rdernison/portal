package nl.uwv.otod.otod_portal.persistence;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.User;

@Repository
public interface UserDao extends CrudRepository<User, Long> {

	Optional<User> findByUsernameIgnoreCase(String username);
	
	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.name='admin'")
	Iterable<User> findAdmins();
	
	List<User> findAllByAccountNonLockedAndLastModifiedDateIsBefore(Boolean locked, Timestamp timestamp);
	
	@Query("SELECT u FROM User u WHERE u.emailAddress=:email")
	public List<User> findByEmail(@Param("email") String email);
	
	public User findByResetPasswordToken(@Param("resetPasswordToken") String resetPasswordToken);
}
