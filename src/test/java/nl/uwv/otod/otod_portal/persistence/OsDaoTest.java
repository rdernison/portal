package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.model.Os;

@ActiveProfiles("test")
@DataJpaTest
@ComponentScan("nl.uwv.otod.otod_portal")
public class OsDaoTest {
	@Autowired
	private OsDao osDao;
	
	@BeforeEach
	public void setUp() {
		var aix5 = new Os();
		aix5.setName("AIX5.0");
		aix5.setEnabled(false);
		osDao.save(aix5);
		
		var aix6 = new Os();
		aix6.setName("AIX6.0");
		aix6.setEnabled(true);
		osDao.save(aix6);
		
		
	}
	
	@AfterEach
	public void tearDown() {
		var oses = osDao.findAll();
		
		for (var os : oses) {
			osDao.delete(os);
		}
		
	}

	@Test
	public void testFindAllEnabled() {
		var enabledOses = osDao.findAllEnabled();
		assertEquals(1, enabledOses.size());
	}
	
	@Test
	public void testFindEnabledByName() {
		var enabledAix5 = osDao.findEnabledByName("AIX5.0");
		assertEquals(0, enabledAix5.size());

		var enabledAix6 = osDao.findEnabledByName("AIX6.0");
		assertEquals(1, enabledAix6.size());
	}
}
