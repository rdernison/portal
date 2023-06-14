package nl.uwv.otod.otod_portal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.model.LoginSuccess;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.persistence.LoginSuccessRepository;
import nl.uwv.otod.otod_portal.service.impl.LoginSuccessServiceImpl;

@ExtendWith(SpringExtension.class)
public class LoginSuccessServiceTest {

	@Mock
	private LoginSuccessRepository loginSuccessDao;
	
	@InjectMocks
	private LoginSuccessServiceImpl loginSuccessService;
	
	private Timestamp firstT, secondT, thirdT;
	
	@BeforeEach
	public void setUp() throws InterruptedException {
		var user = User.builder().username("rde062").build();
		var first = LoginSuccess.builder()
				.user(user)
				.creationDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();
		this.firstT = first.getCreationDate();
		Thread.sleep(50);
		var second = LoginSuccess.builder()
				.user(user)
				.creationDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();
		this.secondT = second.getCreationDate();
		Thread.sleep(50);
		var third = LoginSuccess.builder()
				.user(user)
				.creationDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();
		this.thirdT = third.getCreationDate();
				
		List<LoginSuccess> listDesc = Stream.of(third, second, first).collect(Collectors.toList());;
		when(loginSuccessDao.findAllByOrderByCreationDateDesc()).thenReturn(listDesc);
		
	}
	
	@Test
	public void testGetAll() {
		var allLoginSuccesses = loginSuccessService.getAll();
		var first = allLoginSuccesses.get(2).getCreationDate();
		assertEquals(firstT, first);
		var second = allLoginSuccesses.get(1).getCreationDate();
		assertEquals(secondT, second);
		var third = allLoginSuccesses.get(0).getCreationDate();
		assertEquals(thirdT, third);
	}
}
