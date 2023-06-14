package nl.uwv.otod.otod_portal.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.persistence.UserDao;
import nl.uwv.otod.otod_portal.service.impl.UserServiceImpl;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserServiceImpl userService;
	
	@Mock
	private UserDao userDao;	
	
	@BeforeEach
	public void setUp() {
		var user = User.builder().build();
		var users = new ArrayList<User>();
		users.add(user);
		when(userDao.findAll()).thenReturn(users);
		var ids = new ArrayList<Long>();
		ids.add(1L);
		when(userDao.findAllById(ids)).thenReturn(users);
		when(userDao.findById(1L)).thenReturn(Optional.of(user));
		when(userDao.findByUsernameIgnoreCase("user000")).thenReturn(Optional.of(user));
	}
	
	@Test
	public void testGetAll() {
		var allUsers = userService.getAll();
		assertTrue(allUsers.iterator().hasNext());
	}
	
	@Test
	public void testGetByUsername() {
		var user = userService.getByUsername("user000");
		assertTrue(user.isPresent());
	}
}
