package nl.uwv.otod.otod_portal.conversion;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import nl.uwv.otod.otod_portal.converter.SqlToCsvConverter;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

public class SqlToCsvConverterTest {

	@Test
	public void testConvertIpAddresses() throws IOException {
		
		var dir = SettingsUtil.readSetting(SettingsUtil.OLD_PORTAL_SQL_PATH);
		dir = dir.substring(0, dir.lastIndexOf('/'));
		var sqlToCsvConverter = new SqlToCsvConverter();
		var path = Paths.get(dir);
		sqlToCsvConverter.convertIpAddresses(path);
	}

	@Test
	public void testConvertNodes() throws IOException {
		
		var dir = SettingsUtil.readSetting(SettingsUtil.OLD_PORTAL_SQL_PATH);
		dir = dir.substring(0, dir.lastIndexOf('/'));
		var sqlToCsvConverter = new SqlToCsvConverter();
		var path = Paths.get(dir);
		sqlToCsvConverter.convertNodes(path);
	}

	@Test
	public void testConvertPasswords() throws IOException {
		
		var dir = SettingsUtil.readSetting(SettingsUtil.OLD_PORTAL_SQL_PATH);
		dir = dir.substring(0, dir.lastIndexOf('/'));
		var sqlToCsvConverter = new SqlToCsvConverter();
		var path = Paths.get(dir);
		sqlToCsvConverter.convertPasswords(path);
	}

	@Test
	public void testConvertUsers() throws IOException {
		
		var dir = SettingsUtil.readSetting(SettingsUtil.OLD_PORTAL_SQL_PATH);
		dir = dir.substring(0, dir.lastIndexOf('/'));
		var sqlToCsvConverter = new SqlToCsvConverter();
		var path = Paths.get(dir);
		sqlToCsvConverter.convertUsers(path);
	}
}
