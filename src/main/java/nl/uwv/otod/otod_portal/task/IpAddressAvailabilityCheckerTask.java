package nl.uwv.otod.otod_portal.task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import nl.uwv.otod.otod_portal.reachability.ServerReachabilityChecker;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

@Component
public class IpAddressAvailabilityCheckerTask {

	private static Logger logger = LogManager.getLogger();
	
	// format: sec min hour day-of-month month day-of-week year
//	@Scheduled(cron = "${cron.expression}")
	@Scheduled(cron = "0 0 5 * * *")
	public void checkAvailability() throws IOException {
		logger.info("Executing scheduled task");
		var availableIpAddressesPath = SettingsUtil.readSetting(SettingsUtil.AVAILABLE_IP_ADDRESSES_PATH);
		var path = Paths.get(availableIpAddressesPath);
		var fileShouldBeWritten = Files.exists(path)
				? checkIfExistingFileIsOld(path)
				: true;
		if (fileShouldBeWritten) {
			var serverAvailabilityChecker = new ServerReachabilityChecker();
			var startIp = "10.140";
			var availableIpAddresses = collectAvailableAddresses(serverAvailabilityChecker, startIp);
			logger.info("Got them");
	
			var sb = new StringBuilder();
			for (var ipAddress : availableIpAddresses) {
				sb.append(ipAddress).append("\n");
			}
			Files.write(path, sb.toString().getBytes());
		}
	}
	
	private boolean checkIfExistingFileIsOld(Path path) throws IOException {
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		var age = calculateAge(attr);
		return age > 23 * 3600_000;
	}
	
	private long calculateAge(BasicFileAttributes attr) {
		var lastModificationTime = attr.lastModifiedTime();
		var now = System.currentTimeMillis();
		
		return lastModificationTime == null 
				? now - attr.creationTime().toMillis()
				: now - lastModificationTime.toMillis();
	}

	private List<String> collectAvailableAddresses(ServerReachabilityChecker serverAvailabilityChecker, String startIp) {
		var availableIpAddresses = new ArrayList<String>();
		
		for (var subnet = 177; subnet <= 180; subnet++) {
			logger.info("Pinging subnet: {}", subnet);
			iterateComputerAddresses(serverAvailabilityChecker, startIp, availableIpAddresses, subnet);
		}
		
		return availableIpAddresses;
	}

	private void iterateComputerAddresses(ServerReachabilityChecker serverAvailabilityChecker, String startIp,
			ArrayList<String> availableIpAddresses, int subnet) {
		for (var computerAddress = 1; computerAddress < 256; computerAddress++) {
			var ipAddress = String.format("%s.%d.%d", startIp, subnet, computerAddress);
			var reachable = serverAvailabilityChecker.checkReachability(ipAddress);
			if (!reachable) {
				availableIpAddresses.add(ipAddress);
			}
		}
	}
	
}
