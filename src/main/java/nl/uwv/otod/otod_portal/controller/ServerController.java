package nl.uwv.otod.otod_portal.controller;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.service.DiskService;
import nl.uwv.otod.otod_portal.service.OsService;
import nl.uwv.otod.otod_portal.service.ProjectService;
import nl.uwv.otod.otod_portal.service.ServerService;

@Controller
@RequestMapping("/server")
public class ServerController {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String SERVERS = "servers";

	private static final String SERVER = "server";
	
	private static final String NEW_SERVER = "newServer";
	
	private static final String PROJECTS = "projects";
	
	private static final String OSES = "oses";
	
	private static final String SHOW_SERVER = "showServer";

	private static final String SHOW_SERVERS = "showServers";

	@Autowired
	private ServerService serverService;
	
	@Autowired
	private DiskService diskService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private OsService osService;
	
	@RequestMapping("")
	public String getAllServers(Model model) {
		LOGGER.info("Getting all servers");
		var allServers = serverService.getAllOrdered();
		var serverIterator = allServers.iterator();
		
		LOGGER.info("Servers found? {}", serverIterator.hasNext());
		model.addAttribute(SERVERS, allServers);
		
		model.addAttribute(SERVER, new Server());
		return SHOW_SERVERS;
	}

	@RequestMapping("new")
	public String getPrepareNewServer(Model model) {
		LOGGER.info("Preparing new server");
		var projects = projectService.getAll();
		model.addAttribute(SERVER, new Server());
		var oses = osService.getEnabled();
		model.addAttribute(OSES, oses);
		model.addAttribute(PROJECTS, projects);
		return NEW_SERVER;
	}

	@RequestMapping("create")
	public String createNewServer(/*@Valid*/ @ModelAttribute Server server, Model model) {
		LOGGER.info("Creating new server");
		server.setProductionDate(LocalDate.now());
		model.addAttribute(SERVER, server);
		serverService.save(server);
		return SHOW_SERVER;
	}
	
	@RequestMapping("/{id}")
	public String getServer(Model model, @PathVariable long id) {
		LOGGER.info("Getting server {}", id);
		var optServer = serverService.getById(id);
		if (optServer.isPresent()) {
			var server = optServer.get();
			model.addAttribute("server", server);
			LOGGER.info("Found server, getting disks");
			var disks = diskService.getByServer(server);
			LOGGER.info("Found {} disks", disks.size());
			model.addAttribute("disks", disks);
		}
		return SHOW_SERVER;
	}
	
	@GetMapping("/find")
	public String findServers(/*@Valid*/ @ModelAttribute Server server, Model model) {
		LOGGER.info("Got server: {}" , server);
		
		var name = server.getName();
		var osName = server.getOsName();
		var projectName = server.getProject().getName();
		LOGGER.info("Finding servers by name {}, os {} and project name {}" , name == null ? null : String.format("'%s'", name), osName == null ? null : String.format("'%s'", osName), projectName == null ? null : String.format("'%s'", projectName));
		var servers = serverService.getByNameOsAndProjectName(name, osName, projectName);
		LOGGER.info("Found {} servers", servers.size());
		model.addAttribute("servers", servers);
		return SHOW_SERVERS;
	}

}
