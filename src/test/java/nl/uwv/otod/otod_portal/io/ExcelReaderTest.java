package nl.uwv.otod.otod_portal.io;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

public class ExcelReaderTest {

	@Test
	public void testReadServersFile() throws IOException, InvalidFormatException {
		File file = new File("c:/users/rde062/Documents/servers-met-w.xlsx");
		ExcelReader reader = new ExcelReader();
		reader.readServers(file);
	}
}
