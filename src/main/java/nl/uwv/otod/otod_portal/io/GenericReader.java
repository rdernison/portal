package nl.uwv.otod.otod_portal.io;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

@Log4j2
public abstract class GenericReader {

	protected String inputPath;
	
	public GenericReader() {
		try {
			inputPath = SettingsUtil.readSetting(SettingsUtil.INPUT_PATH);
		} catch (IOException e) {
			log.error(e.toString());
			e.printStackTrace();
		}
	}
}
