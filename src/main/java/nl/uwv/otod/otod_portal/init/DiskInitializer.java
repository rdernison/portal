package nl.uwv.otod.otod_portal.init;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.io.DiskReader;
import nl.uwv.otod.otod_portal.model.Disk;
import nl.uwv.otod.otod_portal.service.DiskService;
import nl.uwv.otod.otod_portal.service.ServerService;

@Component
@Log4j2
public class DiskInitializer {
	
	@Autowired
	private DiskService diskService;
	
	@Autowired
	private ServerService serverService;
	
	public void checkDisks() {
		Iterable<Disk> disksFromDb = diskService.getAll();
		Iterator<Disk> diskIt = disksFromDb.iterator();
		log.info("--- Checking disks ---");
		/* TODO save disks
		if (!diskIt.hasNext()) {
			try {
				new DiskReader()
					.readAllDisks()
					.forEach(d -> writeDisk(d));
			} catch (IOException e) {
				log.error(e.toString());
			}
		}*/
 		
	}

	private void writeDisk(Disk disk) {
		var optServer = serverService.getByName(disk.getComputerName());
		if (optServer.isPresent()) {
			var server = optServer.get();
			log.info("Found server, connecting to disk");
			disk.setServer(server);
			diskService.save(disk);
		}
	}


}
