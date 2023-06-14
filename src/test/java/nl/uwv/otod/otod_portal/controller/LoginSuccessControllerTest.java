package nl.uwv.otod.otod_portal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import nl.uwv.otod.otod_portal.model.LoginSuccess;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.service.LoginSuccessService;

public class LoginSuccessControllerTest {

	@InjectMocks
	private LoginSuccessController controller;
	
	@Mock
	private LoginSuccessService loginSuccessService;
	
	private Timestamp firstT, secondT, thirdT;

	@BeforeEach
	public void setUp() throws InterruptedException {
		MockitoAnnotations.initMocks(this);

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
		when(loginSuccessService.getAll()).thenReturn(listDesc);

	}
	
	@Test
	public void testGetAll() {
		Model model = new ExtendedModelMap();
		controller.getAll(model);
		var loginSuccessesFromController = (List<LoginSuccess>)model.getAttribute("loginSuccesses");
		var firstFromController = loginSuccessesFromController.get(0).getCreationDate();
		var secondFromController = loginSuccessesFromController.get(1).getCreationDate();
		var thirdFromController = loginSuccessesFromController.get(2).getCreationDate();
		assertEquals(thirdT, firstFromController);
		assertEquals(secondT, secondFromController);
		assertEquals(firstT, thirdFromController);
	}
}
