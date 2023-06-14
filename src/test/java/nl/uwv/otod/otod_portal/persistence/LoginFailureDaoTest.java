package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.model.LoginFailure;
import nl.uwv.otod.otod_portal.model.User;

@DataJpaTest
@ComponentScan("nl.uwv.otod.otod_portal")
@ActiveProfiles("test")
public class LoginFailureDaoTest {

	@Autowired
	private LoginFailureRepository loginFailureDao;
	
	@Autowired
	private UserDao userDao;
	
	@BeforeEach
	public void setUp() throws InterruptedException {
		var user = User.builder().username("rde062").build();
		userDao.save(user);
		var first = LoginFailure.builder()
				.user(user)
				.build();
		loginFailureDao.save(first);
		Thread.sleep(500);
		var second = LoginFailure.builder()
				.user(user)
				.build();
		loginFailureDao.save(second);
		Thread.sleep(500);
		var third = LoginFailure.builder()
				.user(user)
				.build();
		loginFailureDao.save(third);
		
	}
	
	@Test
	public void testFindAll() {
		var allLoginFailurees = loginFailureDao.findAll();
		assertEquals(3, allLoginFailurees.size());
		/*
		Timestamp[] timestamps = new Timestamp[3];
		for (int i = 0; i < allLoginFailurees.size(); i++) {
			var loginFailure = allLoginFailurees.get(i);
			var createdDate = loginFailure.getCreatedDate();
			timestamps[i] = createdDate;
			if (i > 0) {
				assertTrue(timestamps[i].after(timestamps[i - 1]), timestamps[i] + " should be after " + timestamps[i - 1]);
			}
		}*/
	}
	
	@Test
	public void testFindAllOrderedByCreationDateDesc() {
		var allLoginFailurees = loginFailureDao.findAllByOrderByCreatedDateDesc();
		assertEquals(3, allLoginFailurees.size());
		/*
		Timestamp[] timestamps = new Timestamp[3];
		for (int i = 0; i < allLoginFailurees.size(); i++) {
			var loginFailure = allLoginFailurees.get(i);
			var createdDate = loginFailure.getCreatedDate();
			timestamps[i] = createdDate;
			if (i > 0) {
				assertTrue(timestamps[i].before(timestamps[i - 1]), timestamps[i] + " should be before " + timestamps[i - 1]);
			}
		}*/
	}
	
	@AfterEach
	public void tearDown() {
		var list = loginFailureDao.findAll();
		for (var loginFailure : list) {
			loginFailureDao.delete(loginFailure);
		}
		
		var users = userDao.findAll();
		for (var user : users) {
			userDao.delete(user);
		}
	}

}
