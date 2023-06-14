package nl.uwv.otod.otod_portal.reachability;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerReachabilityChecker {

	private static Logger logger = LogManager.getLogger();
	
	public String[] makeIpAddresses() {
		var start = "10.140.177.";
		var list = new ArrayList<String>();
		for (int i = 186; i < 189; i++) {
			list.add(start + i);
		}
		
		return list.toArray(new String[] {});
	}
	
	public void checkReachability(String[] ips) {
		for (var ip : ips) {
			var reachable = checkReachability(ip);
			logger.info("{} is reachable : {}", ip , reachable);
		}
	}
	
	public boolean checkReachability(String ip) {
		var reachable = false;
		var bytes = convert(ip);
		try {
			logger.info("Pinging ip {}" , ip);
			reachable = InetAddress.getByAddress(bytes).isReachable(50);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reachable;
	}
	
	private byte[] convert(String ipAddress) {
		String[] subs = ipAddress.split("\\.");
		byte[] bytes = new byte[4];
		
		for (int i = 0; i < subs.length; i++) {
			bytes[i] = (byte)Integer.parseInt(subs[i]);
		}
		
		return bytes;
	}
}
