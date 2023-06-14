package nl.uwv.otod.otod_portal.init;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.io.OsReader;
import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.service.OsService;

@Component
@Log4j2
public class OsInitializer {
	@Autowired
	private OsService osService;
	
	public void checkOses() {
		log.info("Checking oses");
		var oses = osService.getAll();
		var osIt = oses.iterator();
		log.info("Any oses found? {}", osIt.hasNext());
		if (!osIt.hasNext()) {
			readAndStoreOses();
		}
	}

	private void readAndStoreOses() {
		log.info("Reading and storing os'es");
		try {
			var oses = new OsReader().readOses();
			for (var os : oses) {
				log.info("Saving os: {}", os.getName());
				osService.save(os);
			}
		} catch (IOException e) {
			log.error(e.toString());
		}
	}

	public void disableCrapOs() {
		log.debug("Disabling crap oses");
		var allOses = osService.getAll();
		
		var osesToBeDisabled = new ArrayList<Os>();
		var osesToBeEnabled = new ArrayList<Os>();
		
		for (var os : allOses) {
			if (os.getName().equals("274940")
						|| os.getName().contains("Lindows")) {
				log.debug("Disabling os: {}", os.getName());
				osesToBeDisabled.add(os);
			}
		}
		
		var osesBeforeDisabling = osService.getAll();
		var oBI = osesBeforeDisabling.iterator();
		var obiCount = 0;
		while (oBI.hasNext()) {
			var ob = oBI.next();
			obiCount++;
		}
		log.debug("Got {} oses before disabling", obiCount);
		log.debug("Storing disabled oses");
		for (var os : osesToBeDisabled) {
			os.setEnabled(false);
			osService.save(os);
		}
		
		var osesBeforeEnabling = osService.getAll();
		var oBEI = osesBeforeEnabling.iterator();
		var oBEICount = 0;
		while (oBEI.hasNext()) {
			var obe = oBEI.next();
			oBEICount++;
		}
		log.debug("Got {} oses before enabling", oBEICount);
		log.debug("Storing enabled oses");
		for (var os : osesToBeEnabled) {
			os.setEnabled(true);
			osService.save(os);
		}
		var osesAfterEnabling = osService.getAll();
		var oAEI = osesAfterEnabling.iterator();
		var oAECount = 0;
		while (oAEI.hasNext()) {
			var obe = oAEI.next();
			oAECount++;
		}
		log.debug("Got {} oses after enabling", oAECount);
	}


}
