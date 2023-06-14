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

import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.persistence.OsDao;
import nl.uwv.otod.otod_portal.service.impl.OsServiceImpl;

@ExtendWith(SpringExtension.class)
public class OsServiceTest {

	@Mock
	private OsDao osDao;
	
	@InjectMocks
	private OsServiceImpl osService;
	
	@BeforeEach
	public void setUp() {
		var os = new Os();
		os.setName("Linux");
		
		var oses = new ArrayList<Os>();
		oses.add(os);
		
		when(osDao.findById(1L)).thenReturn(Optional.of(os));
		when(osDao.findAll()).thenReturn(oses);
		when(osDao.findByName("Linux")).thenReturn(oses);
	}
	
	@Test
	public void testGetAll() {
		var allOses = osService.getAll();
		assertTrue(allOses.iterator().hasNext());
	}

	@Test
	public void testGetById() {
		var optOs = osService.getById(1L);
		assertTrue(optOs.isPresent());
	}

	@Test
	public void testGetByName() {
		var oses = osService.getByName("Linux");
		assertTrue(oses.iterator().hasNext());
	}
}
