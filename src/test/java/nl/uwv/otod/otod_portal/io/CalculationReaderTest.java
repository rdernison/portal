package nl.uwv.otod.otod_portal.io;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.service.impl.OsServiceImpl;

@ExtendWith(SpringExtension.class)
public class CalculationReaderTest {

	@Mock
	private OsServiceImpl osService;
	
	@InjectMocks
	private CalculationReader calculationReader;
	
	@Test
	public void testReadCalculations() throws IOException {
		var calculations = calculationReader.readCalculations();
		assertNotNull(calculations);
		assertNotEquals(0, calculations.size());
	}
}
