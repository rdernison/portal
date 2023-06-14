package nl.uwv.otod.otod_portal.builder;

import nl.uwv.otod.otod_portal.model.Server;

public class ServerBuilder {

	private String name;
	private String objectId;
	private String ipAddress;
	
	public Server build() {
		var server = new Server();
		
		server.setName(name);
		server.setObjId(objectId);
		server.setIpAddress(ipAddress);
		
		return server;
	}
	
	public ServerBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ServerBuilder setObjectId(String objectId) {
		this.objectId = objectId;
		return this;
	}

	public ServerBuilder setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		return this;
	}
}
