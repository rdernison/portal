package nl.uwv.otod.otod_portal.controller;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import nl.uwv.otod.otod_portal.service.ProjectService;
import nl.uwv.otod.otod_portal.service.RequestService;
import nl.uwv.otod.otod_portal.service.ServerService;

@Controller
public class LoginLogoutController {
	private static Logger logger = LogManager.getLogger();
	protected static final String SERVER_COUNT = "serverCount";

	@Autowired
	private RequestService requestService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ServerService serverService;

	@SuppressWarnings("rawtypes")
	protected int getRequestCount() {
		var allRequests = requestService.getAll();
		var requestCount = 0;
		if (allRequests instanceof Collection) {
			requestCount = ((Collection)allRequests).size();
			logger.debug("Found {} requests", requestCount);
		}
		return requestCount;
	}

	@SuppressWarnings("rawtypes")
	protected int getProjectCount() {
		var allProjects = projectService.getOperational();
		var projectCount = 0;
		if (allProjects instanceof Collection) {
			projectCount = ((Collection)allProjects).size();
			logger.debug("Found {} projects", projectCount);
		}
		return projectCount;
	}
	
	@SuppressWarnings("rawtypes")
	protected int getServerCount() {
		var allServers = serverService.getOperational();
		var serverCount = 0;
		if (allServers instanceof Collection) {
			serverCount = ((Collection)allServers).size();
			logger.info("Found {} servers", serverCount);
		}
		return serverCount;
	}
}
