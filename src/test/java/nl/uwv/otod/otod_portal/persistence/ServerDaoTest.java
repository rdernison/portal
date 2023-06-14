package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan("nl.uwv.otod.otod_portal")
public class ServerDaoTest {

	@Autowired
	private ServerDao serverDao;
	
	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private OsDao osDao;
	
	private Project kvb = new Project();
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		Project p = new Project();
		p.setName("Sonar_IP_2019");
		projectDao.save(p);
		
		Server s1 = new Server();
		s1.setName("vmt010140177242");
		s1.setOsName("AIX7.2");
		s1.setStatus("operational");
		s1.setProductionDate(LocalDate.of(2019, 11, 19));
		s1.setProject(p);
		serverDao.save(s1);
		
		Server s2 = new Server();
		s2.setName("vmt010140177193");
		s2.setOsName("Win 2016");
		s2.setStatus("operational");
		s2.setProductionDate(LocalDate.of(2019, 12, 23));
		
		Project p0 = new Project();
		p0.setName("Systemteam-load-balancing");
		projectDao.save(p0);
		s2.setProject(p0);
		serverDao.save(s2);
		
		Server s3 = new Server();
		s3.setName("vmt010140177192");
		s3.setOsName("RHEL7.0");
		s3.setStatus("operational");
		s3.setProductionDate(LocalDate.of(2019, 12, 17));
		kvb.setName("KVB");
		projectDao.save(kvb);
		s3.setProject(kvb);
		serverDao.save(s3);
		
		// vmt010140180004 rhel7.0 decom
		Server s4 = new Server();
		s4.setName("vmt010140180004");
		s4.setOsName("RHEL7.0");
		s4.setStatus("decommissioned");
		s4.setProductionDate(LocalDate.of(2019, 10, 31));
		serverDao.save(s4);
		
		Server s5 = new Server();
		s5.setName("vmt010140180003");
		s5.setOsName("Win 2016");
		s5.setStatus("decommissioned");
		s5.setProductionDate(LocalDate.of(2019, 10, 14));
		serverDao.save(s5);
		
		Os aix7 = new Os();
		aix7.setName("AIX 7");
		osDao.save(aix7);
		
		Server s6 = new Server();
		s6.setName("rodi01");
		s6.setOs(aix7);
		s6.setProject(p0);
		serverDao.save(s6);
		
	}
	
	@Test
	public void testFindAllOrdered() {
		List<Server> allServers = serverDao.findAllOrdered();
		assertNotNull(allServers);
		
		assertNotEquals(0, allServers.size());
	}
	
	@Test
	public void testFindByProject() {
		List<Server> servers = serverDao.findByProject(kvb);
		assertEquals(1, servers.size());
	}
	
	@Test
	public void testFindOperational() {
		List<Server> servers = serverDao.findByStatus("operational");
		assertNotEquals(0, servers);
		
	}
	
	
	// operational
	// decommissioned
	
	@Test
	public void testFindByStatus() {
		List<Server> operationalServers = serverDao.findByStatus("operational");
		assertEquals(3, operationalServers.size());
//		assertThat(operationalServers.size(), Is.is(3));
		
		/* TODO
		List<Server> decommissionedServers = serverDao.findByStatus("decommissioned");
		assertThat(decommissionedServers.size(), Is.is(2));
		assertEquals(2, decommissionedServers.size());
		*/
	}
	
	@Test
	public void testFindByNameOs() {
	}

	/*
	@Test
	public void testFindByNameOsStatusOperational() {
		List<Server> operationalServers = serverDao.findByNameOsStatusProductionDate("", "", "operational", null);
		assertEquals(3, operationalServers.size());
		
	}
	
	@Test
	public void testFindByNameOsStatusDecommissioned() {
		
		List<Server> decommissionedServers = serverDao.findByNameOsStatusProductionDate("", "", "decommissioned", null);
		assertEquals(2, decommissionedServers.size());
		
	}
	
	@Test
	public void testFindByNameOsStatusProductionDate() {
		List<Server> operationalServers = serverDao.findByNameOsStatusProductionDate("", "", "operational", LocalDate.of(2019, 11, 19));
		assertEquals(1, operationalServers.size());
		
	}
	
	@Test
	public void testFindByNameOsStatusProductionDateNull() {
		List<Server> decommissionedServers = serverDao.findByNameOsStatusProductionDate("", "", "decommissioned", null);
		assertEquals(2, decommissionedServers.size());
		
	}
	
	// vmt010140180003
	@Test
	public void testFindByNameOsNullStatusNullProductionDateNull() {
		List<Server> decommissionedServers = serverDao.findByNameOsStatusProductionDate("vmt010140180003", null, null, null);
		assertEquals(2, decommissionedServers.size());
		
	}
	*/
	
	@Test
	public void testFindByProductionDate() {
		List<Server> operationalServers = serverDao.findByProductionDate(LocalDate.of(2019, 11, 19));
		assertEquals(1, operationalServers.size());
		
		List<Server> operationalServers0 = serverDao.findByProductionDate(null);
		assertEquals(6, operationalServers0.size());
		
	}
	
	@Test
	public void testFindByStatusProductionDate() {
		List<Server> decommissionedServers = serverDao.findByStatusProductionDate("decommissioned", LocalDate.of(2019, 10, 14));
		assertEquals(1, decommissionedServers.size());
		
		List<Server> operationalServers = serverDao.findByStatusProductionDate("operational", LocalDate.of(2019, 11, 19));
		assertEquals(1, operationalServers.size());
		
	}
	
	@Test
	public void testFindByNameOsAndProjectName() {
		var servers = serverDao.findByNameOsAndProjectName("rodi01", "AIX 7", "Systemteam-load-balancing");
		assertEquals(1, servers.size());
	}
	
	@AfterEach
	public void tearDown() {
		Iterable<Server> allServers = serverDao.findAll();
		allServers.forEach(s -> serverDao.delete(s));
	}
}
