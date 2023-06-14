package nl.uwv.otod.otod_portal.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.model.ServerConfiguration;
import nl.uwv.otod.otod_portal.persistence.ServerConfigurationDao;
import nl.uwv.otod.otod_portal.service.impl.ServerConfigurationServiceImpl;

@ExtendWith(SpringExtension.class)
public class ServerConfigurationServiceTest {

	@Mock
	private ServerConfigurationDao serverConfigurationDao;
	
	@InjectMocks
	private ServerConfigurationServiceImpl serverConfigurationService;
	
	@BeforeEach
	public void setUp() {
		var serverConfiguration = new ServerConfiguration();
		var optionalServerConfiguration = Optional.of(serverConfiguration);
		var serverConfiguration2 = new ServerConfiguration();
		
		var allServerConfigurations = new ArrayList<ServerConfiguration>();
		allServerConfigurations.add(serverConfiguration);
		allServerConfigurations.add(serverConfiguration2);
		when(serverConfigurationDao.findById(1L)).thenReturn(optionalServerConfiguration);
		when(serverConfigurationDao.findAll()).thenReturn(allServerConfigurations);
	}
	
	@Test
	public void testGet() {
		var serverConfiguration = serverConfigurationService.get(1L);
		assertTrue(serverConfiguration.isPresent());
	}
	
	@Test
	public void testGetAll() {
		var allServerConfigurations = serverConfigurationService.getAll();
		assertTrue(allServerConfigurations.iterator().hasNext());
	}
	
	@AfterEach
	public void tearDown() {
		
	}
	
}
