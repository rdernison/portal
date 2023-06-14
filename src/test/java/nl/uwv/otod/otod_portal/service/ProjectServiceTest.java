package nl.uwv.otod.otod_portal.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.persistence.ProjectDao;
import nl.uwv.otod.otod_portal.service.impl.ProjectServiceImpl;

@ExtendWith(SpringExtension.class)
public class ProjectServiceTest {

	private static Logger logger = LogManager.getLogger();
	
	
	@Mock
	private ProjectDao projectDao;
	
	@InjectMocks
	private ProjectServiceImpl projectService;
	
	@BeforeEach
	public void setUp() {
		logger.info("Setting up tests");
//		MockitoAnnotations.initMocks(this);
		List<Project> allProjects = makeAllProjects();
		when(projectDao.findAll()).thenReturn(allProjects);
		when(projectDao.findById(1L)).thenReturn(Optional.of(allProjects.iterator().next()));
	}
	
	private List<Project> makeAllProjects() {
		List<Project> allProjects = new ArrayList<>();
		Project p0 = makeProject("AVB-DVB Pilot", "Pilot server voor AVB/DVB");
		Project p1 = makeProject("AVB-DVB-Oracle-Pilot", "De Oracle server voor de AVB-DVB pilot");
		allProjects.add(p0);
		allProjects.add(p1);
		return allProjects;
	}

	private Project makeProject(String name, String description) {
		Project project = new Project();
		project.setName(name);
		project.setDescription(description);
		return project;
	}

	@Test
	public void testGetAll() {
		assertTrue(projectService.getAll().iterator().hasNext());
	}

	@Test
	public void testGetById() {
		assertTrue(projectService.getById(1L).isPresent());
	}
	
	public void testGetByName() {
		assertNotEquals(0, projectService.getByName("AVB-DVB Pilot"));
	}

	public void testGetOperational() {
		var operationalProjects = projectService.getOperational();
		assertTrue(operationalProjects.iterator().hasNext());
	}
	
	public void testGetByNameAndStatus() {
		var operationalProjects = projectService.getByNameAndStatus("AVB-DVB Pilot", "operational");
		assertNotEquals(0, operationalProjects.size());
	}



}
