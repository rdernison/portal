package nl.uwv.otod.otod_portal.correction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.service.OsService;
import nl.uwv.otod.otod_portal.service.ServerService;

@Component
@Log4j2
public class DuplicateOsRemover {

	@Autowired
	private OsService osService;
	
	@Autowired
	private ServerService serverService;
	
	public void removeDuplicateOses() {
		var allOses = osService.getAll();
		for (var os : allOses) {
			log.info("Found os: {}, {}", os.getId(), os.getName());
		}
		removeDuplicates(7444, 2557);
		removeDuplicates(7439, 2564);
		removeDuplicates(7440, 2579);
		removeDuplicates(7435, 2572, 2566);
		removeDuplicates(7436, 2571);
		removeDuplicates(7441, 2577);
		removeDuplicates(7447, 2575);
		removeDuplicates(7433, 2567, 2576, 2569);
		removeDuplicates(7434, 2568);
		removeDuplicates(2573, 2563, 2560);
		removeDuplicates(2565, 2562);
		removeDuplicates(2581, 2578, 7431, 2580, 7443, 2570);
	}
	
	private void removeDuplicates(long ...ids) {
		var correctOsId = ids[0];
		var correctOsOpt = osService.getById(correctOsId);
		if (correctOsOpt.isPresent()) {
			var correctOs = correctOsOpt.get();
			log.info("Correct os: {}", correctOs.getName());
			adjustOsOnServers(ids, correctOs);
		}
	}

	private void adjustOsOnServers(long[] ids, Os correctOs) {
		for (int i = 1; i < ids.length; i++) {
			var id = ids[i];
			var incorrectOsOpt = osService.getById(id);
			if (incorrectOsOpt.isPresent()) {
				var incorrectOs = incorrectOsOpt.get();
				log.info("Found incorrect os: {}, disabling it", incorrectOs.getName());
				incorrectOs.setEnabled(false);
				osService.save(incorrectOs);
				var serversWithIncorrectOs = serverService.getByOs(incorrectOs);
				log.info("Looping servers with this incorrect os");
				for (var serverWithIncorrectOs : serversWithIncorrectOs) {
					log.info("Old os: {}, osname: {}", serverWithIncorrectOs.getOs().getName(), serverWithIncorrectOs.getOsName());
					serverWithIncorrectOs.setOs(correctOs);
					serverService.save(serverWithIncorrectOs);
				}
			}
		}
	}
}
