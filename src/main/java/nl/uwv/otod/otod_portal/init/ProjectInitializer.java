package nl.uwv.otod.otod_portal.init;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.io.BudgetOwnerReader;
import nl.uwv.otod.otod_portal.io.CostCenterReader;
import nl.uwv.otod.otod_portal.io.ProjectDescriptionReader;
import nl.uwv.otod.otod_portal.io.ProjectReader;
import nl.uwv.otod.otod_portal.io.SpocReader;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.service.ProjectService;

@Component
@Log4j2
public class ProjectInitializer {

	@Autowired
	private ProjectService projectService;
	
	public void checkProjects() {
		log.info("-- checking projects --");
		var projectsFromDb = projectService.getAll();
		var projectIt = projectsFromDb.iterator();
		if (!projectIt.hasNext()) {
			log.info("--- Initializing projects ---");
			var projects = readProjects();
			projects
				.stream()
				.forEach(p -> projectService.save(p));
		} else {
			var projectsFromFile = readProjects();
			if (projectsFromFile != null) {
			while (projectIt.hasNext()) {
				var project  = projectIt.next();
				for (var projectFromFile : projectsFromFile) {
					if (projectFromFile.getName().equals(project.getName())) {
						log.info("Updating project {} {} {} {} {}", project.getName(), project.getStartDate(), project.getStatusDate(), projectFromFile.getStartDate(), projectFromFile.getStatusDate());
						if (project.getStartDate().isBefore(LocalDate.of(2000, Month.JANUARY, 1))) {
							project.setStartDate(projectFromFile.getStartDate());
							project.setStatusDate(projectFromFile.getStatusDate());
						}
						setAttributes(project);
						projectService.save(project);
						break;
					}
				}
			}
			}
		}
		
		/*
		 * Dxc-servers zijn niet aan projecten gekoppeld. Zonder project wordt een server niet weergegeven.
		 */
		
		log.info("Checking if DXC project exists");
		var dxcProjectsFromDb = projectService.getByName("DXC");
		Project dxcProject = null;
		if (dxcProjectsFromDb.size() == 0) {
			dxcProject = new Project();
			dxcProject.setName("DXC-Project");
			dxcProject.setDescription("Leeg project om DXC-servers aan te koppelen");
			var startDate = LocalDate.of(2012, Month.JUNE, 6);
			dxcProject.setStartDate(startDate);
			dxcProject.setProjectId("66666");
			projectService.save(dxcProject);
		}
	}

	private List<Project> readProjects() {
		log.info("Reading projects");
		List<Project> projects = null;
		var projectReader = new ProjectReader();
		try {
			projects = projectReader.readAllProjects();
		} catch (IOException e) {
			log.error(e.toString());
		}
		return projects;
	}


	private void setAttributes(Project project) {
		setDescription(project);
		log.info("Checking project {}", project.getName());
		try {
			if (project.getDescription() == null || project.getDescription().length() == 0) {
				log.info("Description not found, setting it");
				setDescription(project);
			}
			if (project.getBudgetOwner() == null || project.getBudgetOwner().length() == 0) {
				log.info("Budget owner not found, setting it");
				setBudgetOwner(project);
			}
			if (project.getCostPlace() == null || project.getCostPlace().length() == 0) {
				log.info("Cost center not found, setting it");
				setCostCenter(project);
			}
			if (project.getSpoc() == null || project.getSpoc().length() == 0) {
				log.info("Spoc not found, setting it");
				setSpoc(project);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setDescription(Project project) {
		try {
			var descriptionsFromFile = new ProjectDescriptionReader().readProjectDescriptions();
			log.info("Setting description");
			for (var line : descriptionsFromFile) {
				var cols = line.split("\\|");
				var projectId = Long.parseLong(cols[3]);
				
				if (projectId == Long.parseLong(project.getProjectId())) {
					var description = cols[7];
					log.info("Found it, setting it: {}", description);
					project.setDescription(description);
					break;
				}
			}
		} catch (IOException e) {
			log.error(e.toString());
		}
		
		
	}

	private void setBudgetOwner(Project project) throws UnsupportedEncodingException, IOException {
		var budgetOwnersFromFile = new BudgetOwnerReader().readBudgetOwners();
		log.info("Setting budget owner");
		for (var line : budgetOwnersFromFile) {
			var cols = line.split("\t");
			var projectId = Long.parseLong(cols[3]);
			
			if (projectId == Long.parseLong(project.getProjectId())) {
				var budgetOwner = cols[7];
				log.info("Found it, setting it: {}", budgetOwner);
				project.setBudgetOwner(budgetOwner);
				break;
			}
		}
	}

	private void setCostCenter(Project project) throws UnsupportedEncodingException, IOException {
		var costCentersFromFile = new CostCenterReader().readCostCentersFromTxtFile();
		log.info("Setting cost center");
		for (var line : costCentersFromFile) {
			var cols = line.split("\t");
			var projectId = Long.parseLong(cols[3]);
			
			if (projectId == Long.parseLong(project.getProjectId())) {
				var costCenter = cols[7];
				log.info("Setting it: {}", costCenter);
				project.setCostPlace(costCenter);
				break;
			}
		}
		
	}

	private void setSpoc(Project project) throws UnsupportedEncodingException, IOException {
		var spocsFromFile = new SpocReader().readSpocs();
		for (var line : spocsFromFile) {
			var cols = line.split("\\|");
			var projectId = Long.parseLong(cols[3]);
			
			if (projectId == Long.parseLong(project.getProjectId())) {
				var spoc = cols[7];
				project.setSpoc(spoc);
				break;
			}
		}
		
		
	}

}
