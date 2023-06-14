package nl.uwv.otod.otod_portal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.service.impl.OsServiceImpl;

public class OsControllerTest {

	@InjectMocks
	private OsController osController;
	
	@Mock
	private OsServiceImpl osService;
	
	@BeforeEach
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);

		var os = new Os();
		os.setName("Linux");
		var allOses = new ArrayList<Os>();
		allOses.add(os);
		when(osService.getById(1L)).thenReturn(Optional.of(os));
		when(osService.getAll()).thenReturn(allOses);
	}
	
	@Test
	public void testGetById() {
		Model model = new ExtendedModelMap();
		var result = osController.getOs(1L, model);
		assertEquals("showOs", result);
		var os = (Os)model.getAttribute("os");
		assertEquals("Linux", os.getName());
		
	}
	
	@Test
	public void testGetAll() {
		Model model = new ExtendedModelMap();
		var result = osController.allOss(model);
		assertEquals("showOses", result);
		@SuppressWarnings("unchecked")
		var allOses = (Iterable<Os>)model.getAttribute("oses");
		assertEquals(true, allOses.iterator().hasNext());
	}
	
}
