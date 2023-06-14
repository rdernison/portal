package nl.uwv.otod.otod_portal.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import nl.uwv.otod.otod_portal.model.PeoplesoftProject;

@Repository
public interface PeoplesoftProjectDao extends JpaRepository<PeoplesoftProject, Long> {
	
	Iterable<PeoplesoftProject> findByMasterCode(@PathVariable String masterCode);
	
	Optional<PeoplesoftProject> findByCode(@PathVariable String code);

}
