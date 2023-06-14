package nl.uwv.otod.otod_portal.persistence;


import static org.junit.jupiter.api.Assertions.assertTrue;

//import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.model.Middleware;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan("nl.uwv.otod.otod_portal")
public class MiddlewareDaoTest {

	@Autowired
	private MiddlewareDao middlewareDao;
	
	@BeforeEach
	public void setUp() {
		var m1 = new Middleware();
		m1.setName("mw1");
		middlewareDao.save(m1);
	}
	
	@Test
	public void testFindAll() {
		var allMiddleware = middlewareDao.findAll();
		var middlewareIt = allMiddleware.iterator();
		assertTrue(middlewareIt.hasNext());
	}
	
	@AfterEach
	public void tearDown() {
		var allMiddleware = middlewareDao.findAll();
		var middlewareIt = allMiddleware.iterator();
		while (middlewareIt.hasNext()) {
			var m = middlewareIt.next();
			middlewareDao.delete(m);				
		}
	}
	
}
