package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.model.User;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan("nl.uwv.otod.otod_portal")
public class UserDaoTest {

	@Autowired
	private UserDao userDao;
	
	private long id = 0l;
	
	private static Logger logger = LogManager.getLogger();

	@BeforeEach
	public void setUp() {
		var johnDoe = User.builder().username("john.doe").emailAddress("john.doe@anywhere.com").build();
		userDao.save(johnDoe);
		id = johnDoe.getId();
		logger.info("Saved user: id = {}", id);
	}
	
	@Test
	public void testFindAll() {
		var allUsers = userDao.findAll();
		assertTrue(allUsers.iterator().hasNext());
	}
	
	@Test
	public void testFindAllById() {
		var ids = new ArrayList<Long>();
		ids.add(id);
		var users = userDao.findAllById(ids);
		assertTrue(users.iterator().hasNext());
	}
	
	@Test
	public void testFindByUsername() {
		var optUser = userDao.findByUsernameIgnoreCase("john.doe");
		assertTrue(optUser.isPresent());
	}
}
