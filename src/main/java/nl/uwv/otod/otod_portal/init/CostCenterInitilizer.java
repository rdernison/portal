package nl.uwv.otod.otod_portal.init;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.io.CostCenterReader;
import nl.uwv.otod.otod_portal.service.CostCenterService;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

@Component
@Log4j2
public class CostCenterInitilizer {
	@Autowired
	private CostCenterService costCenterService;
	public void initCostCenters() throws IOException {
		log.info("Inititalizing cost centers");
		var costCentersFromDb = costCenterService.getAll();
		if (costCentersFromDb == null || costCentersFromDb.size() == 0) {
			var costCenterReader = new CostCenterReader();
			var costCenterPath = SettingsUtil.readSetting(SettingsUtil.COST_CENTER_DOC_PATH);
			try (var in = new FileInputStream(costCenterPath)) {
				log.info("Reading cost centers from {}", costCenterPath);
				var costCenters = costCenterReader.readCostCenters(in);
				for (var costCenter : costCenters) {
					costCenterService.save(costCenter);
				}
			}
		}
	}

}
