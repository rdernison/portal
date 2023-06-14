package nl.uwv.otod.otod_portal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;

public interface ServerService {
	Optional<Server> getById(Long id);
	
	Iterable<Server> getAll();
	
	Iterable<Server> getAllOrdered();
	
	Iterable<Server> getByProject(Project project);
	
	Iterable<Server> getOperational();
	
	void save(Server server);
	
	Optional<Server> getByName(String name);
	
	List<Server> getByNameAndOs(String name, String os);
	
	List<Server> getByNameOsStatus(String name, String os, String status);

	List<Server> getByNameOsStatusProductionDate(String name, String os, String status, LocalDate productionDate);

	List<Server> getByNameOsStatusProductionDates(String name, String os, String status, LocalDate productionDateBegin, LocalDate productionDateEnd, String relation);
	
	List<Server> getByNameOsAndProjectName(String name, String os, String projectName);
	
	List<Server> getByOs(Os os);
}
