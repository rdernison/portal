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

import nl.uwv.otod.otod_portal.model.LoginFailure;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.service.LoginFailureService;

public class LoginFailureControllerTest {


	@InjectMocks
	private LoginFailureController controller;
	
	@Mock
	private LoginFailureService loginFailureService;
	
	private Timestamp firstT, secondT, thirdT;

	@BeforeEach
	public void setUp() throws InterruptedException {
		MockitoAnnotations.initMocks(this);

		var user = User.builder().username("rde062").build();
		var first = LoginFailure.builder()
				.user(user)
				.createdDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();
		this.firstT = first.getCreatedDate();
		Thread.sleep(50);
		var second = LoginFailure.builder()
				.user(user)
				.createdDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();
		this.secondT = second.getCreatedDate();
		Thread.sleep(50);
		var third = LoginFailure.builder()
				.user(user)
				.createdDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();
		this.thirdT = third.getCreatedDate();
				
		List<LoginFailure> listDesc = Stream.of(third, second, first).collect(Collectors.toList());;
		when(loginFailureService.getAllOrderedByCreatedDateDesc()).thenReturn(listDesc);

	}
	
	@Test
	public void testGetAll() {
		Model model = new ExtendedModelMap();
		controller.getAll(model);
		var loginFailureesFromController = (List<LoginFailure>)model.getAttribute("loginFailures");
		var firstFromController = loginFailureesFromController.get(0).getCreatedDate();
		var secondFromController = loginFailureesFromController.get(1).getCreatedDate();
		var thirdFromController = loginFailureesFromController.get(2).getCreatedDate();
		assertEquals(thirdT, firstFromController);
		assertEquals(secondT, secondFromController);
		assertEquals(firstT, thirdFromController);
	}
}
