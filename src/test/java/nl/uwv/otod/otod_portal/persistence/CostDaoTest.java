package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.model.Cost;
import nl.uwv.otod.otod_portal.model.CostType;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan("nl.uwv.otod.otod_portal")
public class CostDaoTest {

	@Autowired
	private CostDao costDao;
	
	@BeforeEach
	public void setUp() throws InterruptedException {
		var server = Cost.builder().type(CostType.SERVER).price(0.0).build();
		costDao.save(server);
		Thread.sleep(50);
		var harddisk = Cost.builder().type(CostType.HARDDISK).price(87.15).build();
		costDao.save(harddisk);
		Thread.sleep(50);
		var harddisk1 = Cost.builder().type(CostType.HARDDISK).price(90.0).build();
		costDao.save(harddisk1);
	}
	
	@AfterEach
	public void tearDown() {
		var allCosts = costDao.findAll();
		for (var cost : allCosts) {
			costDao.delete(cost);
		}
		
	}
	
	@Test
	public void testFinByType() {
		var servers = costDao.findByType(CostType.SERVER);
		assertEquals(1, servers.size());
		var harddisks = costDao.findByType(CostType.HARDDISK);
		assertEquals(2, harddisks.size());
	}
	
	@Test
	public void testFindLatestByType() {
		var harddiskOpt = costDao.findLatestByType(CostType.HARDDISK);
		assertTrue(harddiskOpt.isPresent());		
		assertEquals(90.0, harddiskOpt.get().getPrice());
	}
}
