package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nl.uwv.otod.otod_portal.converter.TransientToPersistentConverter;
import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.model.ServerRequest;

@SpringBootTest
public class ServerRequestDaoTest {

	@Autowired
	private ServerRequestDao serverRequestDao;
	
	@Autowired
	private RequestDao requestDao;
	
	// TODO
	@Test
	public void testFindByRequest() {
		/*
		var request = new Request();
		var serverRequest = new ServerRequest();
		serverRequest.setEnvironment("dev");
//		serverRequestDao.save(serverRequest);
		var transientToPeristentConverter = new TransientToPersistentConverter();
		var devServerRequests = new ArrayList<ServerRequest>();
		devServerRequests.add(serverRequest);
		request.setDevServers(devServerRequests);
		transientToPeristentConverter.convertTransientToPersistent(request);
		requestDao.save(request);
		
		var id = request.getId();
		
		var request2Opt = requestDao.findById(id);
		assertTrue(request2Opt.isPresent());
		
		var request2 = request2Opt.get();
		
		var serverRequests = serverRequestDao.findByRequestAndEnvironment(request2, "dev");
		assertEquals(1, serverRequests.size());
		*/
	}
}
