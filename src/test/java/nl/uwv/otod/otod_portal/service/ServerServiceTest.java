package nl.uwv.otod.otod_portal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.persistence.ServerDao;
import nl.uwv.otod.otod_portal.service.impl.ServerServiceImpl;

@ExtendWith(SpringExtension.class)
public class ServerServiceTest {

	@Mock
	private ServerDao serverDao;
	
	@InjectMocks
	private ServerServiceImpl serverService;
	
	private List<Server> allServers = new ArrayList<>();
	private List<Server> operational = new ArrayList<>();
	private List<Server> decommissioned = new ArrayList<>();
	private List<Server> aixServers = new ArrayList<>();
	private List<Server> winServers = new ArrayList<>();
	private List<Server> linuxServers = new ArrayList<>();
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Server s1 = new Server();
		s1.setName("vmt010140177242");
		s1.setStatus("operational");
//		s1.setOs("AIX7.2");
		aixServers.add(s1);
		operational.add(s1);
//		serverService.save(s1);
		
		Server s2 = new Server();
		s2.setName("vmt010140177193");
		s2.setStatus("operational");
//		s2.setOs("Win 2016");
		winServers.add(s2);
//		serverService.save(s2);
		operational.add(s2);
		
		Server s3 = new Server();
		s3.setName("vmt010140177192");
		s3.setStatus("operational");
//		s3.setOs("RHEL7.0");
		linuxServers.add(s3);
//		serverService.save(s3);
		operational.add(s3);
		
		// vmt010140180004 rhel7.0 decom
		Server s4 = new Server();
		s4.setName("vmt010140180004");
		s4.setStatus("decommissioned");
//		s4.setOs("RHEL7.0");
//		serverService.save(s4);
		linuxServers.add(s4);
		decommissioned.add(s4);
		
		Server s5 = new Server();
		s5.setName("vmt010140180003");
		s5.setStatus("decommissioned");
//		s5.setOs("Win 2016");
//		serverService.save(s5);
		winServers.add(s5);
		decommissioned.add(s5);
		
		allServers.addAll(Arrays.asList(s1, s2, s3, s4, s5));
		operational.addAll(Arrays.asList(s1, s2, s3));
		decommissioned.addAll(Arrays.asList(s4, s5));
		
		when(serverDao.findAllOrdered()).thenReturn(allServers);
		when(serverDao.findByStatus("operational")).thenReturn(operational);
		when(serverDao.findByStatus("decommissioned")).thenReturn(decommissioned);
		
		when(serverDao.findById(1l)).thenReturn(Optional.of(s1));
		when(serverDao.findById(2l)).thenReturn(Optional.of(s2));
		when(serverDao.findById(3l)).thenReturn(Optional.of(s3));
		when(serverDao.findById(4l)).thenReturn(Optional.of(s4));
		when(serverDao.findById(5l)).thenReturn(Optional.of(s5));

		when(serverDao.findByName("vmt010140177242")).thenReturn(Arrays.asList(s1));
		when(serverDao.findByName("vmt010140177193")).thenReturn(Arrays.asList(s2));
		when(serverDao.findByName("vmt010140177192")).thenReturn(Arrays.asList(s3));
		when(serverDao.findByName("vmt010140180004")).thenReturn(Arrays.asList(s4));
		when(serverDao.findByName("vmt010140180003")).thenReturn(Arrays.asList(s5));
		
		when(serverDao.findByNameOsAndProjectName("vmt010140180003", "Win 2016", "AOC")).thenReturn(Arrays.asList(s5));
	}
	
	@AfterEach
	public void tearDown() {
		for (Server server : allServers) {
			serverDao.delete(server);
		}
	}
	
	@Test
	public void testGetAll() {
		int count = 0;
		Iterable<Server> serverIterable = serverService.getAll();
		Iterator<Server> serverIt = serverIterable.iterator();
		while (serverIt.hasNext()) {
			serverIt.next();
			count++;
		}
		assertEquals(5, count);
	}
	
	@Test
	public void testGetById() {
		Optional<Server> optServer1 = serverService.getById(1l);
		Optional<Server> optServer2 = serverService.getById(2l);
		Optional<Server> optServer3 = serverService.getById(3l);
		Optional<Server> optServer4 = serverService.getById(4l);
		Optional<Server> optServer5 = serverService.getById(5l);
		
		assertTrue(optServer1.isPresent());
		assertTrue(optServer2.isPresent());
		assertTrue(optServer3.isPresent());
		assertTrue(optServer4.isPresent());
		assertTrue(optServer5.isPresent());
		
		Server server1 = optServer1.get();
		Server server2 = optServer2.get();
		Server server3 = optServer3.get();
		Server server4 = optServer4.get();
		Server server5 = optServer5.get();
		
		assertEquals("vmt010140177242", server1.getName());
		assertEquals("vmt010140177193", server2.getName());
		assertEquals("vmt010140177192", server3.getName());
		assertEquals("vmt010140180004", server4.getName());
		assertEquals("vmt010140180003", server5.getName());
	}
	
	@Test
	public void testGetByName() {
		Optional<Server> s1Opt = serverService.getByName("vmt010140177242");
		Optional<Server> s2Opt = serverService.getByName("vmt010140177193");
		Optional<Server> s3Opt = serverService.getByName("vmt010140177192");
		Optional<Server> s4Opt = serverService.getByName("vmt010140180004");
		Optional<Server> s5Opt = serverService.getByName("vmt010140180003");
		
		assertTrue(s1Opt.isPresent());
		assertTrue(s2Opt.isPresent());
		assertTrue(s3Opt.isPresent());
		assertTrue(s4Opt.isPresent());
		assertTrue(s5Opt.isPresent());
	}
	
	@Test
	public void testGetByNameOsAndProjectName() {
		var operational = serverService.getByNameOsAndProjectName("vmt010140180003", "Win 2016", "AOC");
		assertNotEquals(0, operational.size());
	}
	
}
