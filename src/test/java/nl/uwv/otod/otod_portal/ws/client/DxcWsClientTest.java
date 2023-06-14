package nl.uwv.otod.otod_portal.ws.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.uwv.otod.otod_portal.model.ServerRequest;

public class DxcWsClientTest {

	private DxcWsClient client;
	
	
	@BeforeEach
	public void setUp() {
		client = new DxcWsClient();
	}
	
	@Test
	public void testReadUsername() {
		var username = client.readUsername();
		assertEquals("hpuss\\roderik.dernison", username);
	}
	
	@Test
	public void testReadPassword() {
		var password = client.readPassword();
		assertEquals("Leiden0011#", password);
	}
	
	
	@Test
	public void testGetOtodServers() {
		var servers = client.getOtodServers();
		assertNotSame(0, servers.size());
	}
	
	@Test
	public void testPostServerRequest() {
		var serverRequest = new ServerRequest();
		client.postServerRequest(serverRequest);
	}
}
