package nl.uwv.otod.otod_portal.ws.client;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.model.ServerRequest;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

// zie https://www.baeldung.com/java-httpclient-post
// voor test zie https://stackoverflow.com/questions/67487210/java-httpclient-unit-testing-with-mockito

@Log4j2
public class DxcWsClient {
	
	private HttpClient client;
	
	public DxcWsClient() {
		log.info("Constructing Ws client");
		client = HttpClient.newHttpClient();
	}
	
	public String readProperty(String propertyName) {
		var prop = "";
		try {
			prop = SettingsUtil.readSetting(propertyName);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return prop;
	}
	
	public String readUsername() {
		var username = "";
		try {
			username = SettingsUtil.readSetting("DXC_USERNAME");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return username;
	}
	
	public String readPassword() {
		var password = "";
		try {
			password = SettingsUtil.readSetting("DXC_PASSWORD");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return password;
	}
	
	public List<Server> getOtodServers() {
		log.info("Getting servers");
		var servers = new ArrayList<Server>();
		
		return servers;
	}
	
	public void postServerRequest(ServerRequest serverRequest) {
		log.info("Posting server request");
	}
}
