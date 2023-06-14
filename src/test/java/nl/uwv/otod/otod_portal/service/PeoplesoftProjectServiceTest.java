package nl.uwv.otod.otod_portal.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.model.PeoplesoftProject;
import nl.uwv.otod.otod_portal.persistence.PeoplesoftProjectDao;
import nl.uwv.otod.otod_portal.service.impl.PeoplesoftProjectServiceImpl;

@ExtendWith(SpringExtension.class)
public class PeoplesoftProjectServiceTest {

	@Mock
	private PeoplesoftProjectDao dao;
	
	@InjectMocks
	private PeoplesoftProjectServiceImpl service;
	
	@BeforeEach
	public void setUp() {
		var un0871 = makePeoplesoftProject("UN0871", "Loondoorbetaling maatregel 2", "PKUN03");
		var un0873 = makePeoplesoftProject("UN0873", "Eenmalig inloggen	Derden", "PKUN05");
		var un0875 = makePeoplesoftProject("UN0875", "Doorontwikkeling Loghost	ICT Project - IS", "PKUN06");
		var un0879 = makePeoplesoftProject("UN0879", "Open VMS	Derden", "PKUN02");
		var un0880 = makePeoplesoftProject("UN0880", "Open VMS	Derden	2. Uitkeren	Uitkeren", "PKUN01");
		var un0885 = makePeoplesoftProject("UN0885", "Klantcontactvoorkeuren	Derden", "PKUN05");
		var un0886 = makePeoplesoftProject("UN0886", "Professionalisering IV	ICT Project - CIO", "PKUN06");

		when(dao.findByCode("UN0871")).thenReturn(Optional.of(un0871));
		when(dao.findByCode("UN0872")).thenReturn(Optional.empty());
		when(dao.findByCode("UN0873")).thenReturn(Optional.of(un0873));
		when(dao.findByCode("UN0874")).thenReturn(Optional.empty());
		when(dao.findByCode("UN0875")).thenReturn(Optional.of(un0875));
		when(dao.findByCode("UN0879")).thenReturn(Optional.of(un0879));
		when(dao.findByCode("UN0880")).thenReturn(Optional.of(un0880));
		when(dao.findByCode("UN0885")).thenReturn(Optional.of(un0885));
		when(dao.findByCode("UN0886")).thenReturn(Optional.of(un0886));
		
		when(dao.findByMasterCode("PKUN01")).thenReturn(Lists.list(un0880));
		when(dao.findByMasterCode("PKUN02")).thenReturn(Lists.list(un0879));
		when(dao.findByMasterCode("PKUN03")).thenReturn(Lists.list(un0871));
		when(dao.findByMasterCode("PKUN04")).thenReturn(Lists.list());
		when(dao.findByMasterCode("PKUN05")).thenReturn(Lists.list(un0873, un0885));
		when(dao.findByMasterCode("PKUN06")).thenReturn(Lists.list(un0886));
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
		
	}
	
	@Test
	public void testGetByCode() {
		var un0871 = service.getByCode("UN0871");
		var un0872 = service.getByCode("UN0872");
		var un0873 = service.getByCode("UN0873");
		var un0874 = service.getByCode("UN0874");
		var un0875 = service.getByCode("UN0875");
		var un0879 = service.getByCode("UN0879");
		var un0880 = service.getByCode("UN0880");
		var un0885 = service.getByCode("UN0885");
		var un0886 = service.getByCode("UN0886");
		
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
	public void testGetByMasterCode() {
		var pkun01 = service.getByMasterCode("PKUN01");
		var pkun02 = service.getByMasterCode("PKUN02");
		var pkun03 = service.getByMasterCode("PKUN03");
		var pkun04 = service.getByMasterCode("PKUN04");
		var pkun05 = service.getByMasterCode("PKUN05");
		var pkun06 = service.getByMasterCode("PKUN06");
		
		assertTrue(pkun01.iterator().hasNext());
		assertTrue(pkun02.iterator().hasNext());
		assertTrue(pkun03.iterator().hasNext());
		assertFalse(pkun04.iterator().hasNext());
		assertTrue(pkun05.iterator().hasNext());
		assertTrue(pkun06.iterator().hasNext());
	}
	
}
