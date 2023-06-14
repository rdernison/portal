package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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
public class DiskDaoTest {

	@Autowired
	private DiskDao diskDao;
	
	@Autowired
	private ServerDao serverDao;
	
	@Autowired
	private ProjectDao projectDao;
	
	
	@BeforeEach
	public void setUp() {
		
		Project p = new Project();
		p.setName("AUTO");
		projectDao.save(p);
		
		Server s = new Server();
		s.setProject(p);
		s.setName("vmt010140177242");
		serverDao.save(s);
		
		Disk disk1 = new Disk();
		disk1.setComputerName("vmt010140176001");
		disk1.setFileSystemName("NTFS");
		disk1.setServer(s);
		diskDao.save(disk1);
		
		Disk disk2 = new Disk();
		disk2.setComputerName("vmd010140178021");
		disk2.setFileSystemName("ext3");
		disk2.setServer(s);
		diskDao.save(disk2);
	}
	
	@Test
	public void testFindByFileSystem() {
		List<Disk> ext3 = diskDao.findByFileSystem("ext3");
		assertNotNull(ext3);
		assertEquals(1, ext3.size());
		
		List<Disk> ntfs = diskDao.findByFileSystem("NTFS");
		assertNotNull(ntfs);
		assertEquals(1, ntfs.size());
	}
	
	@AfterEach
	public void tearDown() {
		Iterable<Disk> allDisks = diskDao.findAll();
		
		allDisks.forEach(d -> diskDao.delete(d));
	}
}
