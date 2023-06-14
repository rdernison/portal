package nl.uwv.otod.otod_portal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import nl.uwv.otod.otod_portal.builder.ProjectBuilder;
import nl.uwv.otod.otod_portal.builder.ServerBuilder;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.service.impl.DiskServiceImpl;
import nl.uwv.otod.otod_portal.service.impl.ProjectServiceImpl;
import nl.uwv.otod.otod_portal.service.impl.ServerServiceImpl;

@ExtendWith(SpringExtension.class)
public class ProjectControllerTest {
/* TODO make this work again
	@InjectMocks
	private ProjectController projectController;

	@Mock
	private ProjectServiceImpl projectService;
	
	@Mock
	private ServerServiceImpl serverService;
	
	@Mock
	private DiskServiceImpl diskService;
	
	private Project project;
	
	private List<Project> allProjects = new ArrayList<>();
	
	private Server server;
	
	private List<Server> servers = new ArrayList<>();
	
	@BeforeEach
	public void setUp() {
//		MockitoAnnotations.initMocks(this);
		server = new ServerBuilder()
				.setIpAddress("10.140.6.66")
				.setName("vt010140006066")
				.build();

		servers.add(server);
		
		project = new ProjectBuilder()
				.setBudgetOwner("Jochem Bermon")
				.setDescription("Testproject")
				.setName("Testproject")
				.setStatus("operational")
				.build();
		allProjects.add(project);
		when(projectService.getAll()).thenReturn(allProjects);
		when(projectService.getById(1L)).thenReturn(Optional.of(project));
		when(projectService.getByNameAndStatus("Testproject", "operational")).thenReturn(allProjects);
		when(projectService.getByName("Testproject")).thenReturn(allProjects);
		when(serverService.getByProject(project)).thenReturn(servers);
	}
	
	
	@Test
	public void testCreateProject() {
	}
	
	@Test
	public void testGetAllProjects() throws Exception {
		Model model = new ExtendedModelMap();
		var result = projectController.getAllProjects(model);
		assertEquals("showProjects", result);
		@SuppressWarnings("unchecked")
		var allProjects = (List<Project>)model.getAttribute("projects");
		assertEquals(1, allProjects.size());
	}
	
	@Test
	public void testFindProject() {
		Model model = new ExtendedModelMap();
		var inputProject = new ProjectBuilder()
				.setName("Testproject")
				.setStatus("operational")
				.build();
		var result = projectController.findProjects(inputProject, model);
		assertEquals("showProjects", result);
		@SuppressWarnings("unchecked")	
		var projects = (List<Project>)model.getAttribute("projects");
		assertEquals(1, projects.size());
		var returnProject = projects.get(0);
		assertEquals("Testproject", returnProject.getName());
		assertEquals("operational", returnProject.getStatus());
	}
	
	@Test
	public void testGetProject() {
		Model model = new ExtendedModelMap();
		var result = projectController.getProject(model, 1L);
		assertEquals("showProject", result);
		var returnProject = (Project)model.getAttribute("project");
		assertEquals("Testproject", returnProject.getName());
		assertEquals("operational", returnProject.getStatus());
		
	}
	*/
}
