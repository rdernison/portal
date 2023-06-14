package nl.uwv.otod.otod_portal.persistence;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.LoginFailure;
import nl.uwv.otod.otod_portal.model.User;

@Repository
public interface LoginFailureRepository extends JpaRepository<LoginFailure, Long>{
	public List<LoginFailure> findAllByUserAndCreatedDateIsAfter(User user, Timestamp timestamp);
	
	public List<LoginFailure> findAllByOrderByCreatedDateDesc();
}
