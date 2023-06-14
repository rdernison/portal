package nl.uwv.otod.otod_portal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import nl.uwv.otod.otod_portal.model.Middleware;
import nl.uwv.otod.otod_portal.service.impl.MiddlewareServiceImpl;

public class MiddlewareControllerTest {

	@InjectMocks
	private MiddlewareController middlewareController;
	
	@Mock
	private MiddlewareServiceImpl middlewareService;
	
	private Middleware middleware;
	
	private List<Middleware> allMiddleware = new ArrayList<>();
	
	@BeforeEach
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);

		middleware = new Middleware();
		middleware.setName("mw01");
		
		allMiddleware.add(middleware);
		
		when(middlewareService.getAll()).thenReturn(allMiddleware);
	}
	
	@Test
	public void testAdmin() {
		Model model = new ExtendedModelMap();
		var result = middlewareController.admin(model);
		assertEquals("adminMiddleware", result);
		@SuppressWarnings("unchecked")
		var middlewareFromServer = (List<Middleware>)model.getAttribute("allMiddleware");
		assertEquals(1, middlewareFromServer.size());
		
	}
}
