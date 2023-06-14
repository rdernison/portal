package nl.uwv.otod.otod_portal.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.service.ProjectService;
import nl.uwv.otod.otod_portal.service.ServerService;

@Component
public class ServerProjectConnector {

	@Autowired
	private ServerService serverService;
	
	@Autowired
	private ProjectService projectService;
	
	private static Logger logger = LogManager.getLogger();

	public void connectServersWithProjects() throws IOException {
		var serverData = readAllServersFromFile();
		var allServers = serverService.getAll();
		logger.info("Connecting servers with projects");
		for (var server : allServers) {
			var project = server.getProject();
			logger.info("Found a server: {}, project = {}", server.getName(), server.getProject());
			if (server.getName().equals("uwvm3vwounm0001")) {
				logger.warn("uwvm3vwounm0001, project??");
			}
			if (project == null) {
				connectServerWithProject(serverData, server);
			}
		}
	}

	private void connectServerWithProject(List<String> serverData, Server server) {
		var projectName = findProjectName(server, serverData);///server.getProjectName();
		logger.info("Server {} not connected to project, project name = {}", server.getName(), projectName);
		var projectsFromDb = projectService.getByName(projectName);
		Project projectToConnect = null;
		if (projectsFromDb.size() == 0) {
			logger.info("Project not found, creating new one");
			projectToConnect = new Project();
			projectToConnect.setName(projectName);
			projectService.save(projectToConnect);
		} else {
			for (var projectFromDb : projectsFromDb) {
				logger.info("Found project: {}", projectFromDb.getName());
			}
			projectToConnect = projectsFromDb.get(0);
			server.setProject(projectToConnect);
		}
		server.setProject(projectToConnect);
		serverService.save(server);
	}
	
	private String findProjectName(Server server, List<String> serverData) {
		var projectName = "";
		for (var serverLine : serverData) {
			if (serverLine.contains(server.getName())) {
				var subs = serverLine.split("\t");
				projectName = subs[0];
				break;
			}
		}
		return projectName;
	}

	private List<String> readAllServersFromFile() throws IOException {
		var allServers =  new ArrayList<String>();
		var line = "";
		try (var br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Hostnames met w-20210106.txt")))) {
			while ((line = br.readLine()) != null) {
				allServers.add(line);
			}
		}
		return allServers;
	}
}
