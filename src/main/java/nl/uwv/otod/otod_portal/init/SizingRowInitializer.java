package nl.uwv.otod.otod_portal.init;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.io.GenericOtodSizingReader;
import nl.uwv.otod.otod_portal.model.SizingRow;
import nl.uwv.otod.otod_portal.service.SizingRowService;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

@Component
@Log4j2
public class SizingRowInitializer {
	@Autowired
	private SizingRowService sizingRowService;
	
	public void initSizingRows() {
//		if (false) {
		log.info("Initializing sizing rows");
		var sizingRows = sizingRowService.getAll();
		if (!sizingRows.iterator().hasNext()) {
			log.info("No rows in db, writing");
			var sizingRowReader = new GenericOtodSizingReader();
			try {
				var otodSizingPath = SettingsUtil.readSetting(SettingsUtil.SIZING_DOC_PATH);
				var rows = sizingRowReader.readOtodSizing(otodSizingPath);
				var rowIt = rows.iterator();
				while (rowIt.hasNext()) {
					var row = rowIt.next();
					log.info("Read sizing row, saving it: {} {} {}", row.getArrivalDate(), row.getOtodProjectName(), row.getServerName());
					log.info(stringify(row));
					try {
						sizingRowService.save(row);
					} catch (IllegalStateException e) {
						log.error("Error saving sizing row: {} {} {}" , row.getArrivalDate(), row.getOtodProjectName(), row.getServerName());
					}
				}
			} catch (InvalidFormatException | IOException e) {
				log.error(e.toString());
			}
		}
//		}
	}

	private String stringify(SizingRow row) {
		String line = row.getEmployee() + ";" + row.isDelivered() + ";" 
				+ row.getWave() + ";"
				+ row.getApplicationName() + ";"
				+ row.getDivision();
		return line;
	}


}
