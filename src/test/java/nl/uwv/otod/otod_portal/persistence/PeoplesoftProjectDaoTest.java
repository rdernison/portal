package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.model.PeoplesoftProject;

@ActiveProfiles("test")
@DataJpaTest
@ComponentScan("nl.uwv.otod.otod_portal")
public class PeoplesoftProjectDaoTest {

	@Autowired
	private PeoplesoftProjectDao dao;
	
	@BeforeEach
	public void setUp() {
		var un0871 = makePeoplesoftProject("UN0871", "Loondoorbetaling maatregel 2", "PKUN03");
		var un0873 = makePeoplesoftProject("UN0873", "Eenmalig inloggen	Derden", "PKUN05");
		var un0875 = makePeoplesoftProject("UN0875", "Doorontwikkeling Loghost	ICT Project - IS", "PKUN06");
		var un0879 = makePeoplesoftProject("UN0879", "Open VMS	Derden", "PKUN02");
		var un0880 = makePeoplesoftProject("UN0880", "Open VMS	Derden	2. Uitkeren	Uitkeren", "PKUN01");
		var un0885 = makePeoplesoftProject("UN0885", "Klantcontactvoorkeuren	Derden", "PKUN05");
		var un0886 = makePeoplesoftProject("UN0886", "Professionalisering IV	ICT Project - CIO", "PKUN06");
		dao.save(un0871);
		dao.save(un0873);
		dao.save(un0875);
		dao.save(un0879);
		dao.save(un0880);
		dao.save(un0885);
		dao.save(un0886);
	}
	
	private PeoplesoftProject makePeoplesoftProject(String code, String name, String masterCode) {
		var project = new PeoplesoftProject();
		project.setCode(code);
		project.setName(name);
		project.setMasterCode(masterCode);
		return project;
	}
	
	@AfterEach
	public void tearDown() {
		var allProjects = dao.findAll();
		for (var project : allProjects) {
			dao.delete(project);
		}
	}
	
	@Test
	public void testFindByCode() {
		var un0871 = dao.findByCode("UN0871");
		var un0872 = dao.findByCode("UN0872");
		var un0873 = dao.findByCode("UN0873");
		var un0874 = dao.findByCode("UN0874");
		var un0875 = dao.findByCode("UN0875");
		var un0879 = dao.findByCode("UN0879");
		var un0880 = dao.findByCode("UN0880");
		var un0885 = dao.findByCode("UN0885");
		var un0886 = dao.findByCode("UN0886");
		
		assertTrue(un0871.isPresent());
		assertFalse(un0872.isPresent());
		assertTrue(un0873.isPresent());
		assertFalse(un0874.isPresent());
		assertTrue(un0875.isPresent());
		assertTrue(un0879.isPresent());
		assertTrue(un0880.isPresent());
		assertTrue(un0885.isPresent());
		assertTrue(un0886.isPresent());
		
	}
	
	@Test
	public void testFindByMasterCode() {
		var pkun01 = dao.findByMasterCode("PKUN01"); // 1
		var pkun02 = dao.findByMasterCode("PKUN02");  // 1
		var pkun03 = dao.findByMasterCode("PKUN03"); // 1
		var pkun04 = dao.findByMasterCode("PKUN04");
		var pkun05 = dao.findByMasterCode("PKUN05");  // 2
		var pkun06 = dao.findByMasterCode("PKUN06"); // 2
		
		assertTrue(pkun01.iterator().hasNext());
		assertTrue(pkun02.iterator().hasNext());
		assertTrue(pkun03.iterator().hasNext());
		assertFalse(pkun04.iterator().hasNext());
		assertTrue(pkun05.iterator().hasNext());
		assertTrue(pkun06.iterator().hasNext());
	}
}
