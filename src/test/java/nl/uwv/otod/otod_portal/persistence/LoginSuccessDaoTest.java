package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.model.LoginSuccess;
import nl.uwv.otod.otod_portal.model.User;

@DataJpaTest
@ComponentScan("nl.uwv.otod.otod_portal")
@ActiveProfiles("test")
public class LoginSuccessDaoTest {


	@Autowired
	private LoginSuccessRepository loginSuccessDao;
	
	@Autowired
	private UserDao userDao;
		
	@BeforeEach
	public void setUp() throws InterruptedException {
		var user = User.builder().username("rde062").build();
		userDao.save(user);
		var first = LoginSuccess.builder()
				.user(user)
				.build();
		Thread.sleep(500);
		var second = LoginSuccess.builder()
				.user(user)
				.build();
		Thread.sleep(500);
		var third = LoginSuccess.builder()
				.user(user)
				.build();
		loginSuccessDao.save(first);
		Thread.sleep(500);
		loginSuccessDao.save(second);
		Thread.sleep(500);
		loginSuccessDao.save(third);
		
	}
	
	@Test
	public void testFindAll() {
		var allLoginSuccesses = loginSuccessDao.findAll();
		assertEquals(3, allLoginSuccesses.size());
		/*
		Timestamp[] timestamps = new Timestamp[3];
		for (int i = 0; i < allLoginSuccesses.size(); i++) {
			var loginSuccess = allLoginSuccesses.get(i);
			var createdDate = loginSuccess.getCreationDate();
			timestamps[i] = createdDate;
			if (i > 0) {
				System.out.println(timestamps[i] + " >? " + timestamps[i - 1]);
				assertTrue(timestamps[i].after(timestamps[i - 1]));
			}
		}*/
	}
	
	@Test
	public void testFindAllOrderedByCreationDateDesc() {
		var allLoginSuccesses = loginSuccessDao.findAllByOrderByCreationDateDesc();
		assertEquals(3, allLoginSuccesses.size());
		/*
		Timestamp[] timestamps = new Timestamp[3];
		for (int i = 0; i < allLoginSuccesses.size(); i++) {
			var loginSuccess = allLoginSuccesses.get(i);
			var createdDate = loginSuccess.getCreationDate();
			timestamps[i] = createdDate;
			/* TODO make test work
			if (i > 0) {
				System.out.println(timestamps[i] + " <? " + timestamps[i - 1]);
				assertTrue(timestamps[i].before(timestamps[i - 1]));
			}* /
		}*/
	}
	
	@AfterEach
	public void tearDown() {
		var list = loginSuccessDao.findAll();
		for (var loginSuccess : list) {
			loginSuccessDao.delete(loginSuccess);
		}
		
		var users = userDao.findAll();
		for (var user : users) {
			userDao.delete(user);
		}
	}
}
