package nl.uwv.otod.otod_portal.io;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

@Log4j2
public class SqlReaderTest {

	// select * from node n, field_data_field_admin_pass ap, field_data_field_ip_adres ip where n.nid=ap.entity_id and ip.entity_id = n.nid; 
	@Test
	public void testReadOldPortalSqlFile() throws IOException {
		var oldPortalSqlFile = SettingsUtil.readSetting(SettingsUtil.OLD_PORTAL_SQL_PATH);
		log.info(/*TAG,*/ "OPSF: " + oldPortalSqlFile);
		var sqlReader = new SqlReader();
		var path = Paths.get(oldPortalSqlFile);
		sqlReader.readSqlFile(path);
		
	}
	
}
