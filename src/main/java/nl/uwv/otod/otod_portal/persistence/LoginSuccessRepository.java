package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.LoginSuccess;

@Repository
public interface LoginSuccessRepository extends JpaRepository<LoginSuccess, Long>{

	List<LoginSuccess> findAllByOrderByCreationDateDesc();
	
	List<LoginSuccess> findFirst25ByOrderByCreationDateDesc();
}
