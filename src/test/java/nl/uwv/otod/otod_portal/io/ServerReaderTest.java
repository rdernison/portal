package nl.uwv.otod.otod_portal.io;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.service.OsService;
import nl.uwv.otod.otod_portal.service.ProjectService;

@ExtendWith(SpringExtension.class)
public class ServerReaderTest {

	@Mock
	private ProjectService projectService;
	
	@Mock
	private OsService osService;

	@InjectMocks
	private ServerReader serverReader = new ServerReader(projectService, osService);
	
	@Test
	public void testReadDxcServers() throws IOException {
		var dxcServers = serverReader.readDxcServers();
		assertNotNull(dxcServers);
		assertNotEquals(0, dxcServers.size());
	}

	@Test
	public void testReadIbmServers() throws IOException {
		var ibmServers = serverReader.readIbmServers();
		assertNotNull(ibmServers);
		assertNotEquals(0, ibmServers.size());
	}
}
