package nl.uwv.otod.otod_portal.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ServerBuilderTest {

	private static final String NAME = "vt10140166133";
	private static final String IP_ADDRESS = "10.140.166.133";
	private static final String OBJ_ID = "objId";
	
	@Test
	public void testBuildServer() {
		var serverBuilder = new ServerBuilder();
		var server = serverBuilder
			.setName(NAME)
			.setObjectId(OBJ_ID)
			.setIpAddress(IP_ADDRESS)
			.build();
		var name = server.getName();
		var objId = server.getObjId();
		var ipAddress = server.getIpAddress();
		assertEquals(NAME, name);
		assertEquals(OBJ_ID, objId);
		assertEquals(IP_ADDRESS, ipAddress);
	}
}
