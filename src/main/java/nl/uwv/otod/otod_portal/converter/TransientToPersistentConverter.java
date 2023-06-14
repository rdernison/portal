package nl.uwv.otod.otod_portal.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.MiddlewareRequest;
import nl.uwv.otod.otod_portal.model.Person;
import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.model.ServerRequest;
import nl.uwv.otod.otod_portal.model.User;

@Log4j2
@Deprecated
public class TransientToPersistentConverter {
/*
	public void convertTransientToPersistent(Request request) {
		log.info("Converting transient to persistent data");
		
		log.info("Reading transient data from request");
		var transientDevServers = request.getDevServers();
		log.info("Does transient dev servers exist? {}", !transientDevServers.isEmpty());
		var transientTestServers = request.getTestServers();
		log.info("Does transient test servers exist? {}", !transientTestServers.isEmpty());
		
		var transientDevUsers = request.getTransientDevUsers();
		var transientTestUsers = request.getTransientTestUsers();
		
		log.info("Converting transient servers to persistent servers");
		var devServers = convertServers(transientDevServers);
		log.info("Does dev servers exist? {}", !devServers.isEmpty());
		var testServers = convertServers(transientTestServers);
		log.info("Does test servers exist? {}", !testServers.isEmpty());
		
		var devUsers = convertUsers(transientDevUsers);
		var testUsers = convertUsers(transientTestUsers);
		
		
		request.setDevUsers(devUsers);
		request.setTestUsers(testUsers);
		
		request.setDevServers(devServers);
		request.setTestServers(testServers);
	}

	private List<Person> convertUsers(Set<Person> transientUsers) {
		var users = new ArrayList<Person>();
		
		for (var user : transientUsers) {
			users.add(user);
		}
		return users;
	}
	
	private List<ServerRequest> convertServers(Set<ServerRequest> transientServers) {
		var servers = new ArrayList<ServerRequest>();
		
		for (var server : transientServers) {
			servers.add(server);
			
			var transientMiddleware = server.getTransientMiddleware();
			var persistentMiddleware = convertTransientToPersistentMiddleware(transientMiddleware);
			
			server.setMiddleware(persistentMiddleware);
			
		}
		return servers;
	}
	
	private List<MiddlewareRequest> convertTransientToPersistentMiddleware(Set<MiddlewareRequest> transientMiddleweare) {
		var persistentMiddleware = new ArrayList<MiddlewareRequest>();
		
		for (var middleware : transientMiddleweare) {
			persistentMiddleware.add(middleware);
		}
		
		return persistentMiddleware;
	}

	*/
}
