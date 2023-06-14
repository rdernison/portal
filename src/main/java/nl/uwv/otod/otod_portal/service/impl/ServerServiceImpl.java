package nl.uwv.otod.otod_portal.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.persistence.ServerDao;
import nl.uwv.otod.otod_portal.service.ServerService;

@Service
@Log4j2
public class ServerServiceImpl implements ServerService {

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private ServerDao serverDao;
	
	@Override
	public Optional<Server> getById(Long id) {
		return serverDao.findById(id);
	}

	@Override
	public Iterable<Server> getAll() {
		var allServers = serverDao.findAll();
		return allServers;
	}

	@Override
	public Iterable<Server> getByProject(Project project) {
		return serverDao.findByProject(project);
	}

	@Override
	public Iterable<Server> getOperational() {
		Iterable<Server> operationalServers = serverDao.findByStatus("Operational");
		
		return operationalServers;
	}

	@Override
	public void save(Server server) {
		serverDao.save(server);
	}

	@Override
	public Optional<Server> getByName(String name) {
//		LOGGER.info("Finding server: {} ", name);
		List<Server> servers = serverDao.findByName(name);
		Optional<Server> optServer = Optional.empty();
		if (servers.size() > 0) {
			optServer = Optional.of(servers.get(0));
		}
		return optServer;
	}

	@Override
	public List<Server> getByNameAndOs(String name, String os) {
		return serverDao.findByNameAndOs(name + "%", os + "%");
	}

	@Override
	public List<Server> getByNameOsStatus(String name, String os, String status) {
		return null;
	}

	@Override
	public List<Server> getByNameOsStatusProductionDate(String name, String os, String status,
			LocalDate productionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Server> getByNameOsStatusProductionDates(String name, String os, String status,
			LocalDate productionDateBegin, LocalDate productionDateEnd, String relation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Server> getByNameOsAndProjectName(String name, String os, String projectName) {
		return serverDao.findByNameOsAndProjectName(name == null ? "" : name, 
				os == null ? "" : os, 
						projectName == null ? "" : projectName);
	}

	@Override
	public List<Server> getByOs(Os os) {
		return serverDao.findByOs(os);
	}

	@Override
	public Iterable<Server> getAllOrdered() {
		var allServers = serverDao.findAllOrdered();
		/*
		var allServersIt = allServers.iterator();
		
		while (allServersIt.hasNext()) {
			var server = allServersIt.next();
			log.info("Found server {} {}", server.getName(), server.getIpAddress());
		}*/
		return allServers;
	}

}
