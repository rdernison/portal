package nl.uwv.otod.otod_portal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.converter.TransientToPersistentConverter;
import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.model.ServerRequest;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.service.impl.RequestServiceImpl;
import nl.uwv.otod.otod_portal.service.impl.UserServiceImpl;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@Log4j2
public class RequestControllerTest {

	private static final String APPLICATION_NAME = "Otod-portal";
	private static final String EMAIL_ADDRES = "john@doe.com";
	
	@InjectMocks
	private RequestController requestController;
	
	@Mock
	private RequestServiceImpl requestService;
	
	@Mock
	private UserServiceImpl userService;
	
	private User user;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		var request = new Request();
		request.setApplicationName(APPLICATION_NAME);
		var optRequest = Optional.of(request);
		
		var requests = new ArrayList<Request>();
		requests.add(request);
		
		user = User.builder().build();
		when(requestService.getAll()).thenReturn(requests);
		when(requestService.get(1L)).thenReturn(optRequest);
		when(requestService.getByEmailAddress(EMAIL_ADDRES)).thenReturn(requests);
		when(requestService.getMyRequests(user)).thenReturn(requests);
		when(requestService.getSubmitted()).thenReturn(requests);
		
	}
	
	@Test
	public void testGetById() {
		Model model = new ExtendedModelMap();
		var response = requestController.getRequestById(model, 1L);
		assertEquals("showRequest", response);
		var request = (Request)model.getAttribute("request");
		assertNotNull(request);

	}
	
	@Test
	public void testGetMyRequests() {		
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		var userDetails = new org.springframework.security.core.userdetails.User("username", "password", new ArrayList<GrantedAuthority>());
		when(authentication.getPrincipal()).thenReturn(userDetails);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
//		var user = new User(1L, "username", "user@uwv.nl", "020-68959929", new byte[], new byte[], new ArrayList<Role>());
		var user = User.builder()
				.username("username")
				.emailAddress("john@doe.com")
				.phoneNumber("020-6875929")
				.build();
		var optionalUser = Optional.of(user);
		when(userService.getByUsername("username")).thenReturn(optionalUser);
		var users = new ArrayList<User>();
		users.add(user);
		var model = new ExtendedModelMap();
		var response = requestController.getMyRequests(model);
		@SuppressWarnings("unchecked")
		var requests = (List<Request>)model.getAttribute("myRequests");
		assertNotEquals(0, requests.size());
	}
	
	@Test
	public void testGetAll() {
		var model = new ExtendedModelMap();
		requestController.getRequest(model);
		@SuppressWarnings("unchecked")
		var allRequests = (List<Request>)model.getAttribute("allRequests");
		assertEquals(1, allRequests.size());
	}
	
	@Test
	public void testGetSubmitted() {
		var model = new ExtendedModelMap();
		requestController.getSubmittedRequests(model);
		@SuppressWarnings("unchecked")
		var submittedRequests = (List<Request>)model.getAttribute("allRequests");
		assertNotEquals(0, submittedRequests.size());
	}
	
	
	@Test
	public void testSave() {
		/*
		var devServerRequests = makeDevServerRequests();
		Request request = new Request();
		request.setTransientDevServers(devServerRequests);
		Model model = new ExtendedModelMap();
		BindingResult bindingResult = mock(BindingResult.class);
		MockMultipartFile file = new MockMultipartFile("file", new byte[20]);
		var converter = new TransientToPersistentConverter();
		converter.convertTransientToPersistent(request);
		requestController.saveRequest(request, bindingResult, file, model);
		
		long id = request.getId();
		log.info("ID = {}", id);
		
		*/
	}
	
	private Set<ServerRequest> makeDevServerRequests() {
		var devServerRequests = new HashSet<ServerRequest>();
		var devServer = new ServerRequest();
		devServerRequests.add(devServer);
		
		return devServerRequests;
	}
	
 }
