package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.model.CostCenter;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan("nl.uwv.otod.otod_portal")
public class CostCenterDaoTest {

	@Autowired
	private CostCenterDao costCenterDao;
	
	// GALKM0	Mallegatsplein 10 Alkmaar		
	// 429008	B&F Noord loc Alkmaar	
	// 400000	Raad van Bestuur UWV	
	// 400200	Portefeuille lid RvB	
	// 420001	Facilitair Bedrijf	
	// 420400	Bedrijfsvoering FB	
	// 420401	FB Huisvesting & Facilitair	
	// 420430	H&F Beheer en Facilitair Noord	
	// 429008	B&F Noord loc Alkmaar	
	// GALKM0	Mallegatsplein 10 Alkmaar

	@BeforeEach
	public void setUp() {
		var level1 = CostCenter.builder()
				.code("GALKM0")
				.description("Mallegatsplein 10 Alkmaar")
				.build();
		costCenterDao.save(level1);
		//PKUN18
		//P-kstn E-werken ext gefinanc
		var pkun18 = CostCenter.builder()
				.code("PKUN18")
				.description("P-kstn E-werken ext gefinanc")
				.build();
		costCenterDao.save(pkun18);
	}
	
	@Test
	public void testFindByDescription() {
		var malle = costCenterDao.findByName("malle");
		assertEquals(1, malle.size());
		var kstn = costCenterDao.findByName("kstn");
		assertEquals(1, kstn.size());
	}
	
	@Test
	public void testFindByCode() {
		
	}
	
	@Test
	public void testfindByPkunOrS() {
		var ccs = costCenterDao.findByPkunOrS();
		assertEquals(1, ccs.size());
	}
	
	@AfterEach
	public void tearDown() {
		var allCcs = costCenterDao.findAll();
		for (var cc : allCcs) {
			costCenterDao.delete(cc);
		}
	}
}
