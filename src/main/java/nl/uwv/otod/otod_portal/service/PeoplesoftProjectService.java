package nl.uwv.otod.otod_portal.service;

import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;

import nl.uwv.otod.otod_portal.model.PeoplesoftProject;

public interface PeoplesoftProjectService {

	Iterable<PeoplesoftProject> getByMasterCode(@PathVariable String masterCode);
	
	Optional<PeoplesoftProject> getByCode(@PathVariable String code);
	
	Optional<PeoplesoftProject> getById(Long id);
	
	Iterable<PeoplesoftProject> getAll();
	
	void save(PeoplesoftProject project);
}
