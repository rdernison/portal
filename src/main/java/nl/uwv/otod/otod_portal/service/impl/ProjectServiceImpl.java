package nl.uwv.otod.otod_portal.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.persistence.ProjectDao;
import nl.uwv.otod.otod_portal.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {
	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	private ProjectDao projectDao;
	
	public Optional<Project> getById(Long id) {
		return projectDao.findById(id);
	}
	
	public Iterable<Project> getAll() {
		return projectDao.findAllOrderedByName();
	}

	@Override
	public List<Project> getByName(String name) {
		return projectDao.findByName(name);
	}

	@Override
	public Iterable<Project> getOperational() {
		return projectDao.findByStatus("Operational");
	}

	@Override
	public void save(Project project) {
		projectDao.save(project);
	}

	@Override
	public List<Project> getByNameAndStatus(String name, String status) {
		logger.info("Getting projects by name '{}' and status '{}'", name, status);
		return projectDao.findByNameAndStatus(name, status);
	}

}
