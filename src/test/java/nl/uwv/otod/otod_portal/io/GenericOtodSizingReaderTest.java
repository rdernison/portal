package nl.uwv.otod.otod_portal.io;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

import nl.uwv.otod.otod_portal.util.SettingsUtil;

public class GenericOtodSizingReaderTest {

	@Test
	public void testReadOtodSizingDocument() throws IOException, InvalidFormatException {
		GenericOtodSizingReader reader = new GenericOtodSizingReader();
		String path = SettingsUtil.readSetting(SettingsUtil.SIZING_DOC_PATH);
		var sizingRows = reader.readOtodSizing(path);
		
		assertNotNull(sizingRows);
	}
}
