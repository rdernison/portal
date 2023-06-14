package nl.uwv.otod.otod_portal.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.service.OsService;
import nl.uwv.otod.otod_portal.service.ServerService;

public class ServerUpdater {
	private static final Logger LOGGER = LogManager.getLogger();
	
	private ServerService serverService;
	private OsService osService;
	public ServerUpdater(ServerService serverService, OsService osService) {
		this.osService = osService;
		this.serverService = serverService;
	}
	
	public void updateServers() {
		var servers = serverService.getAll();
		LOGGER.info("Updating servers");
		for (var server : servers) {
			var serverOs = server.getOs();
			if (serverOs == null) {
				LOGGER.info("Server has no connected os object");
				var osName = server.getOsName();
				LOGGER.info("Updating server: {} os = {}", server.getName(), osName);
				var oses = osService.getByName(osName);
				Os os = null;
				if (oses.size() > 0) {
					os = oses.get(0);
					LOGGER.info("Found os: {} {}" , os.getId(),  os.getName());
				} else {
					os = new Os();
					os.setName(osName);
					osService.save(os);
				}
				server.setOs(os);
				serverService.save(server);
			}
		}
	}
 }
