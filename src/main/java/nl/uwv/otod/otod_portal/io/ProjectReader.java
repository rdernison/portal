package nl.uwv.otod.otod_portal.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.Project;
@Log4j2
public class ProjectReader extends GenericReader{

	private static Logger logger = LogManager.getLogger();
	
	public List<Project> readAllProjects() throws IOException {
		logger.info("Reading projects from {}", inputPath);
		var projectStatuses = readProjectStatuses();
		var allProjects = new ArrayList<Project>();
		var path = Paths.get(inputPath, "project.csv");
		var allLines = Files.readAllLines(path);
		for (var line : allLines) {
			var project = makeProject(line);
			try {
			var projectStatus = findByProjectId(projectStatuses, project.getId());
				if (projectStatus.getName().equals("Operational")) {
					allProjects.add(project);
				}
			} catch(NullPointerException e) {
				log.error("Null status: {} {}", project.getName(), project.getId());
			}
		}
		
		return allProjects;
	}

	private Project makeProject(String line) {
		var project = new Project();
		logger.info("Parsing line: {}", line);
		var subs = line.split("\\|");
		project.setId(Long.parseLong(subs[0]));
		project.setProjectId(subs[0]);
		// Project naam	Project omschrijving	Budgethouder	Datum project Begin	Datum project Eind	Project status	Datum status
		project.setName(subs[4]);
		logger.info("Set name: {}", project.getName());
		
		var col8 = subs[7];
		var long8 = Long.parseLong(col8) * 1000;
		var startDate = new Date(long8);
		project.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		var col9 = subs[8];
		var long9 = Long.parseLong(col9) * 1000;
		var statusDate = new Date(long9);
		project.setStatusDate(statusDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		return project;
	}
	
	private List<InnerProjectStatus> readProjectStatuses() throws IOException {
		var projectStatuses = new ArrayList<InnerProjectStatus>();
		Path path = Paths.get(inputPath, "projectstatus.csv");
		var allLines = Files.readAllLines(path);
		for (var line : allLines) {
			var projectStatus = makeProjectStatus(line);
			projectStatuses.add(projectStatus);
		}
		return projectStatuses;
	}

	// columns 4, 8
	private InnerProjectStatus makeProjectStatus(String line) {
		var projectStatus = new InnerProjectStatus();
		var subs = line.split("\\|");
		var projectId = Long.parseLong(subs[3]);
		var name = subs[7];
		projectStatus.setId(projectId);
		projectStatus.setName(name);
		return projectStatus;
	}
	
	private InnerProjectStatus findByProjectId(List<InnerProjectStatus> projectStatuses, Long projectId) {
		InnerProjectStatus theStatus = null;
		for (var projectStatus : projectStatuses) {
			if (projectStatus.getId() == projectId) {
				theStatus = projectStatus;
			}
		}
		return theStatus;
	}
	
	@Getter
	@Setter
	private class InnerProjectStatus {
		private long id;
		private String name;
	}
}
