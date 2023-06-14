package nl.uwv.otod.otod_portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.ProjectCount;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.model.ServerCounts;
import nl.uwv.otod.otod_portal.service.DiskService;
import nl.uwv.otod.otod_portal.service.ProjectService;
import nl.uwv.otod.otod_portal.service.RequestService;
import nl.uwv.otod.otod_portal.service.ServerService;

@Controller
public class HomeController extends LoginLogoutController {

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private DiskService diskService;
	
	@Autowired
	private RequestService requestService;
	
	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public String getHomePage(Model model) {
		model.addAttribute("serverCounts", getServerCounts());
		model.addAttribute("allProjects", getAllProjects());
		model.addAttribute("requestCount", ((List)requestService.getAll()).size());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
		return "home";
	}	
	
	private ServerCounts getServerCounts() {
		int numberOfProjects = getProjectCount();//0;
		var serverCounts = new ServerCounts();
		var operationalServers = serverService.getOperational();
		var serverIt = operationalServers.iterator();
		LOGGER.info("Iterating operational servers");
		var physicalCores = 0.0;
		var virtualCpus = 0;
		var ram = 0;
		var numberOfServers = getServerCount();
		var systemDisk = 0;
		while (serverIt.hasNext()) {
//			LOGGER.info("Got a server from the iterator");
			var server = serverIt.next();
			if (server != null && server.getStatus() != null && "Operational".equals(server.getStatus())) {
				physicalCores += server.getPhysicalCores();
				virtualCpus += server.getVirtualCpus();
				ram += server.getRam();
//				numberOfServers++;
				systemDisk += server.getSystemDisk();
			}
		}
		
		var disks = diskService.getAll();
		var diskIt = disks.iterator();
		var extraDiskSpace = 0;
		while (diskIt.hasNext()) {
			var disk = diskIt.next();
			extraDiskSpace += disk.getSize();
		}
		
		serverCounts.setNumberOfProjects(numberOfProjects);
		serverCounts.setNumberOfServers(numberOfServers);
		serverCounts.setPhysicalCores(physicalCores);
		serverCounts.setVirtualCpus(virtualCpus);
		serverCounts.setRam(ram);
		serverCounts.setSystemDisk(systemDisk);
		LOGGER.info("Found {} extra disk space", extraDiskSpace);
		serverCounts.setExtraDiskSpace(extraDiskSpace);
		LOGGER.info("--- Got server count ---");
		return serverCounts;
	}
	
	private List<ProjectCount> getAllProjects() {
		LOGGER.info("--- Getting all project data ---");
		var projectCounts = new ArrayList<ProjectCount>();
		var allProjects = projectService.getAll();
		var projectIt = allProjects.iterator();
		
		while (projectIt.hasNext()) {
			Project project = projectIt.next();
			if ("Operational".equals(project.getStatus())) {
				ProjectCount pc = makeProjectCount(project);
				projectCounts.add(pc);
			}
		}
		
		return projectCounts;
	}
	
	// Projectaflopend sorteren	Servers	Fysieke cores	Systeemschijf (GB)	Virtuele CPU's	RAM (GB)	Extra schijfruimte (GB)
	private ProjectCount makeProjectCount(Project project) {
		var pc = new ProjectCount();
		pc.setProjectId(project.getId());
		pc.setProjectName(project.getName());
		var servers = serverService.getByProject(project);
		var serverIt = servers.iterator();

		while (serverIt.hasNext()) {
			Server server = serverIt.next();
			increaseServerCounts(pc, server);
		}
		
		return pc;
	}

	private void increaseServerCounts(ProjectCount pc, Server server) {
		if ("Operational".equals(server.getStatus())) {
//			numberOfServers++;
			pc.setNumberOfServers(pc.getNumberOfServers() + 1);
//			physicalCores += server.getPhysicalCores();
			pc.setNumberOfPhysicalCores(pc.getNumberOfPhysicalCores() + server.getPhysicalCores());
//			systemDisk += server.getSystemDisk();
			pc.setSystemDisk(pc.getSystemDisk() + server.getSystemDisk());;
//			virtualCpus += server.getVirtualCpus();
			pc.setVirtualCpus(pc.getVirtualCpus() + server.getVirtualCpus());
//			ram += server.getRam();
			pc.setRam(pc.getRam() + server.getRam());
			// TODO server.getExtraDiskSpace();
		}

	}
}
