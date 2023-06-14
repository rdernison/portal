package nl.uwv.otod.otod_portal.service;

import java.util.List;
import java.util.Optional;

import nl.uwv.otod.otod_portal.model.Project;

public interface ProjectService {

	Optional<Project> getById(Long id);
	
	Iterable<Project> getAll();
	
	List<Project> getByName(String name);
	
	Iterable<Project> getOperational();
	
	void save(Project project);
	
	List<Project> getByNameAndStatus(String name, String status);
}
