package nl.uwv.otod.otod_portal.ldap;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.imperva.ddc.core.query.ConnectionResponse;
import com.imperva.ddc.core.query.Endpoint;
import com.imperva.ddc.service.DirectoryConnectorService;

@Disabled
public class DdcTest {

	private static final String USERNAME = "UWPOL\\rde062";//"rde062-ldap@T-dc.ba.uwv.nl";
	private static final String PASSWORD = "HatseFlatse00!";
	
	@Test
	public void testConnectSecured() {
		Logger logger = LogManager.getLogger();
		
		Endpoint endpoint = new Endpoint();
		endpoint.setSecuredConnection(true);
//		endpoint.setSecuredConnectionSecondary(false);
		endpoint.setPort(389);
		endpoint.setSecondaryPort(389);
		endpoint.setHost("10.178.128.160"); // was 47
//		endpoint.setSecondaryHost("10.100.10.47");
		endpoint.setPassword(PASSWORD);
		endpoint.setUserAccountName(USERNAME); //* You can use the user's Distinguished Name as well
		
		ConnectionResponse connectionResponse = DirectoryConnectorService.authenticate(endpoint);
		boolean succeeded = !connectionResponse.isError();
		logger.info("Login successful? {}", succeeded);
		assertTrue(succeeded);
	}

	@Test
	public void testConnectUnsecured() {
		Logger logger = LogManager.getLogger();
		
		Endpoint endpoint = new Endpoint();
		endpoint.setSecuredConnection(false);
//		endpoint.setSecuredConnectionSecondary(false);
		endpoint.setPort(389);
//		endpoint.setSecondaryPort(389);
		endpoint.setHost("10.178.128.160");
//		endpoint.setSecondaryHost("10.100.10.47");
		endpoint.setPassword(PASSWORD);
		endpoint.setUserAccountName(USERNAME); //* You can use the user's Distinguished Name as well
		
		ConnectionResponse connectionResponse = DirectoryConnectorService.authenticate(endpoint);
		boolean succeeded = !connectionResponse.isError();
		logger.info("Login successful? {}", succeeded);
		assertTrue(succeeded);
	}
}
