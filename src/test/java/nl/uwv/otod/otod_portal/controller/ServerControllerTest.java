package nl.uwv.otod.otod_portal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

//import nl.uwv.otod.otod_portal.builder.ProjectBuilder;
import nl.uwv.otod.otod_portal.model.Disk;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.service.impl.DiskServiceImpl;
import nl.uwv.otod.otod_portal.service.impl.ServerServiceImpl;

public class ServerControllerTest {
/*
	@InjectMocks
	private ServerController serverController;
	
	@Mock
	private ServerServiceImpl serverService;
	
	@Mock
	private DiskServiceImpl diskService;
	
	private List<Server> allServers = new ArrayList<>();
	
	private Server server;
	private Project project;

	private List<Disk> allDisks = new ArrayList<>();
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		server = new ServerBuilder()
				.setIpAddress("10.140.6.29")
				.setName("vmt010140006029")
				.setObjectId("Object-id")
				.build();
		project = new ProjectBuilder()
				.setBudgetOwner("")
				.setDescription("")
				.setName("")
				.build();
		allServers.add(server);
		when(serverService.getAll()).thenReturn(allServers);
		when(serverService.getById(1L)).thenReturn(Optional.of(server));
		when(serverService.getByName("vmt010140006029")).thenReturn(Optional.of(server));
		when(serverService.getByProject(project)).thenReturn(allServers);
		when(diskService.getAll()).thenReturn(allDisks);
	}
	
	@Test
	public void testGetAllServers() {
		Model model = new ExtendedModelMap();
		var result = serverController.getAllServers(model);
		assertEquals("showServers", result);
		var allServers = (List<Server>)model.getAttribute("servers");
		assertEquals(1, allServers.size());
	}
	
	@Test
	public void testGetServerById() {
		Model model = new ExtendedModelMap();
		var result = serverController.getServer(model, 1L);
		assertEquals("showServer", result);
		var server = (Server)model.getAttribute("server");
		assertNotNull(server);
		assertEquals("10.140.6.29", server.getIpAddress());
		System.out.println(server.getIpAddress());
	}
	*/
}
