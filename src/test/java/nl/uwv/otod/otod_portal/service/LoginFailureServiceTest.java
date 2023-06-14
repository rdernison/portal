package nl.uwv.otod.otod_portal.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.model.LoginFailure;
import nl.uwv.otod.otod_portal.persistence.LoginFailureRepository;
import nl.uwv.otod.otod_portal.service.impl.LoginFailureServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class LoginFailureServiceTest {

	@Mock
	private LoginFailureRepository loginFailureRepository;
	
	@InjectMocks
	private LoginFailureServiceImpl loginFailureService;
	
	@BeforeEach
	public void setUp() throws InterruptedException {
		var loginFailure1 = LoginFailure.builder()
				.createdDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();		
		Thread.sleep(500);
		var loginFailure2 = LoginFailure.builder()
				.createdDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();
		Thread.sleep(500);
		var loginFailure3 = LoginFailure.builder()
				.createdDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();
		var allFailures = new ArrayList<LoginFailure>();
		allFailures.add(loginFailure3);
		allFailures.add(loginFailure2);
		allFailures.add(loginFailure1);
		when(loginFailureRepository.findAllByOrderByCreatedDateDesc()).thenReturn(allFailures);
	}
	
	@Test
	public void testGetAllOrderedByCreatedDateDesc() {
		var allLoginFailures = loginFailureService.getAllOrderedByCreatedDateDesc();
		assertEquals(3, allLoginFailures.size());
		for (int i = 0; i < allLoginFailures.size(); i++) {
			var loginFailure = allLoginFailures.get(i);
			if (i > 0) {
				var timestamp = allLoginFailures.get(i - 1).getCreatedDate();
				assertTrue(timestamp.after(loginFailure.getCreatedDate()));
			}
		}
	}
}
