package nl.uwv.otod.otod_portal.io;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;

import nl.uwv.otod.otod_portal.util.SettingsUtil;

public class CostCenterReaderTest {
// SGHUP 210601.xls
	
	@Test
	public void testReadCostCenters() throws EncryptedDocumentException, IOException {
		var costCentreDocPath = SettingsUtil.readSetting(SettingsUtil.COST_CENTER_DOC_PATH);
		var file = new File(costCentreDocPath);
		var reader = new CostCenterReader();
		try (InputStream in = new FileInputStream(file)) {
			var costCenters =  reader.readCostCenters(in);
			assertNotEquals(0, costCenters.size());
		}
	}
}
