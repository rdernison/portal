package nl.uwv.otod.otod_portal.controller;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.service.DiskService;
import nl.uwv.otod.otod_portal.service.ProjectService;
import nl.uwv.otod.otod_portal.service.ServerService;


@Controller
@RequestMapping("/project")
public class ProjectController {
	private static final String SHOW_PROJECTS = "showProjects";

	private static final String SHOW_PROJECT = "showProject";

	private static final Logger LOGGER = LogManager.getLogger();

	private static final String NEW_PROJECT = "newProject";
	

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private DiskService diskService;
	
	@RequestMapping("")
	public String getAllProjects(Model model) {
		LOGGER.info("Gettting all projects");
		var allProjects = projectService.getAll();
		
		LOGGER.info("Got projects: {}", ((Collection<Project>)allProjects).size());
		
		model.addAttribute("projects", allProjects);
		
		model.addAttribute("project", new Project());
		
		return SHOW_PROJECTS;
	}
	

	
	@RequestMapping("/{id}")
	public String getProject(Model model, @PathVariable long id) {
		LOGGER.info("Getting project by id: {}", id);
		var optProject = projectService.getById(id);
		
		Iterable<Server> servers = null;
		if (optProject.isPresent()) {
			model.addAttribute("project", optProject.get());
			servers = serverService.getByProject(optProject.get());
			var serverIt = servers.iterator();
			while (serverIt.hasNext()) {
				var server = serverIt.next();
				LOGGER.info("Found server: {} status={}", server.getName(), server.getStatus());
				var disks = diskService.getByServer(server);
				LOGGER.info("Found disks {}", disks);
				
				server.setDisks(disks);
				
				LOGGER.info("Server.disks: {}", server.getDisks());
				
			}
			model.addAttribute("servers", servers);
		}
		
		return SHOW_PROJECT;
		
	}

	@RequestMapping("new")
	public String getPrepareNewProject(Model model) {
		LOGGER.info("Preparing new project");
		model.addAttribute("project", new Project());
		return NEW_PROJECT;
	}

	@RequestMapping("create")
	public String createNewProject(@ModelAttribute Project project, Model model, BindingResult bindingResult) {
		LOGGER.info("Creating new project");
		LOGGER.info("Start date: {}", project.getStartDate());
		LOGGER.info("Status date: {}", project.getStatusDate());
		if (bindingResult.hasErrors()) {
			LOGGER.error("There were errors");
			model.addAttribute("project", project);
			return NEW_PROJECT;
		} else {
			LOGGER.info("Project valid, saving it");
			model.addAttribute("project", project);
			projectService.save(project);
			return SHOW_PROJECTS;
		}
	}

	@GetMapping("/find")
	public String findProjects(/*@Valid */@ModelAttribute Project project, Model model, String name, String status) {
		LOGGER.info("Got project: {}" , project);
		
		LOGGER.info("Finding projects by name '{}' and status '{}'" , name, status);
		List<Project> projects = projectService.getByNameAndStatus(name, status);
		LOGGER.info("Found {} projects", projects.size());
		for (Project p : projects) {
			LOGGER.info("Found a project: {} {}", p.getName(), p.getDescription());
		}
		model.addAttribute("projects", projects);
//		request.setAttribute("projects", projects);
		return SHOW_PROJECTS;
	}
}
