package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.model.Disk;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan("nl.uwv.otod.otod_portal")
public class ProjectDaoTest {

	// status: operational, decommssioned, canceled, draft
	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private ServerDao serverDao;
	
	@Autowired
	private DiskDao diskDao;
	
	@BeforeEach
	public void setUp() {
		// 15-11-2012 // canceled
		// 15-11-20122 decommissioned
		// AMG Gateway operational
		
		Project project0 = new Project();
		project0.setName("15-11-2012");
		project0.setDescription("15-11-2012");
		project0.setStatus("canceled");
		projectDao.save(project0);

		Project project1 = new Project();
		project1.setName("15-11-20122");
		project1.setDescription("15-11-20122");
		project1.setStatus("decommissioned");
		projectDao.save(project1);

		Project project2 = new Project();
		project2.setName("AMG Gateway");
		project2.setDescription("AMG Gateway");	
		project2.setStatus("operational");
		projectDao.save(project2);
		
		Project project3 = new Project();
		project3.setName("AUTO 07032019");
		project3.setDescription("AUTO 07032019");	
		project3.setStatus("draft");
		projectDao.save(project3);
		
	}
	
	@Test
	public void testFindByName() {
		List<Project> projects0 = projectDao.findByName("15-11-2012");
		assertNotNull(projects0);
		assertEquals(1, projects0.size());
		List<Project> projects1 = projectDao.findByName("15-11-20122");
		assertNotNull(projects1);
		assertEquals(1, projects1.size());
		List<Project> projects2 = projectDao.findByName("AMG Gateway");
		assertNotNull(projects2);
		assertEquals(1, projects2.size());
	}
	
	@Test
	public void testFindByStatus() {
		List<Project> operational = projectDao.findByStatus("operational");
		assertEquals(1, operational.size());
		List<Project> decommissioned = projectDao.findByStatus("decommissioned");
		assertEquals(1, decommissioned.size());
		List<Project> canceled = projectDao.findByStatus("canceled");
		assertEquals(1, canceled.size());

		List<Project> draft = projectDao.findByStatus("draft");
		assertEquals(1, draft.size());
}

	@Test
	public void testFindByNameAndStatus() {
		List<Project> fifteenEleven = projectDao.findByNameAndStatus("15-11-2012", "canceled");
		assertEquals(1, fifteenEleven.size());
	}
	
//	@AfterAll
	@AfterEach
//	@After
	public void tearDown() {
		Iterable<Project> all = projectDao.findAll();
		
		Iterator<Project> projectIt = all.iterator();
		while (projectIt.hasNext()) {
			Project project = projectIt.next();
			List<Server> servers = serverDao.findByProject(project);
			logger.info("Project {} has {} servers", project.getName(), servers.size());
			
			for (Server server : servers) {
				List<Disk> disks = diskDao.findByServer(server);
				
				for (Disk disk : disks) {
					diskDao.delete(disk);
				}
				
				serverDao.delete(server);
			}
		}
		all.forEach(entity -> projectDao.delete(entity));
	}
	
	
}
