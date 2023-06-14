package nl.uwv.otod.otod_portal.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import nl.uwv.otod.otod_portal.model.SizingRow;

@Deprecated
@Disabled
public class SizingDocumentReaderTest {

	private static final Logger logger = LogManager.getLogger();
	
	@Test
	public void testReadSizingRows() {
		Path path = Paths.get("c:/users/rde062/documents/Generic OToD sizing v1._UWV_upd_mrt2021_v1.0.xlsx");
		try {
			var sizingRows = new SizingDocumentReader().readSizingRows(path);
			assertNotEquals(0, sizingRows.size());
			var firstSizingRow = sizingRows.get(0);
			var employee = firstSizingRow.getEmployee();
			assertEquals("Frankie", employee);
			logSizingRows(sizingRows);
		} catch (IOException | InvalidFormatException e) {
			logger.error("Exception: {}", e.toString());
		}
	}
	
	private void logSizingRows(List<SizingRow> sizingRows) {
		var result = "";
		
		for (var sizingRow : sizingRows) {
			result += sizingRow.toString() + "\n";
		}
		logger.info(result);
	}
}
