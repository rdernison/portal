package nl.uwv.otod.otod_portal.availability;

import org.junit.jupiter.api.Test;

//import org.junit.jupiter.api.Test;

import nl.uwv.otod.otod_portal.reachability.ServerReachabilityChecker;

public class ServerAvailabiilityCheckerTest {

	@Test
	public void testCheckAvailability() {
		var checker = new ServerReachabilityChecker();
		var ips = new String[] {"10.140.177.7"/*"10.178.128.16", "10.140.176.249"	, "10.140.180.4", "10.140.177.242","10.140.177.193","10.140.177.192","10.140.177.188","10.140.177.5"*/};
		checker.checkReachability(ips);
	}
}
