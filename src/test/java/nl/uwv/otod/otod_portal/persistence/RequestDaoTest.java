package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import nl.uwv.otod.otod_portal.converter.TransientToPersistentConverter;
import nl.uwv.otod.otod_portal.model.MiddlewareRequest;
import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.model.ServerRequest;
import nl.uwv.otod.otod_portal.model.User;

//@DataJpaTest
@ActiveProfiles("test")
@SpringBootTest
@ComponentScan("nl.uwv.otod.otod_portal")
public class RequestDaoTest {

	private static Logger logger = LogManager.getLogger();
	
	private static final String TAG = "RequestDaotest";
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RequestDao requestDao;
	
	private User roderik;
	private User jur;
	
	private Request jursRequest;
	private Request roderiksRequest;
	
	@Autowired
	private MiddlewareRequestDao middlewareRequestDao;
	
	@Autowired
	private ServerRequestDao serverRequestDao;
	
	@BeforeEach
	public void setUp() {
		roderik = User.builder().username("rde062").build();
		userDao.save(roderik);
		
		jur  = User.builder().username("jja042").build();
		userDao.save(jur);
		
		jursRequest = new Request();
		jursRequest.setUser(jur);
		jursRequest.setSaveToEdit(true);
		requestDao.save(jursRequest);
		
		roderiksRequest = new Request();
		roderiksRequest.setUser(roderik);
		roderiksRequest.setSaveToEdit(false);
		requestDao.save(roderiksRequest);
	}
	
	@AfterEach
	public void tearDown() {
		var allRequests = requestDao.findAll();
		for (var request : allRequests) {
			requestDao.delete(request);
		}
		
		var allUsers = userDao.findAll();
		for (var user : allUsers) {
			userDao.delete(user);
		}
	}
	
	@Test
	public void testHellWorld() {
		logger.info(TAG, "Hell, world");
	}
	
	
	@Test
	public void testFindByUser() {
		logger.info(TAG, "Finding requests by urser");
		var jursRequests = requestDao.findByUser(jur);
		assertTrue(jursRequests.iterator().hasNext());
		var roderiksRequests = requestDao.findByUser(roderik);
		assertTrue(roderiksRequests.iterator().hasNext());
	}
	
	@Test
	public void testFindBySubmitted() {
		// ro
		var submitted = requestDao.findBySubmitted(true);
		var submittedIt = submitted.iterator();
		assertTrue(submittedIt.hasNext());
		var firstSubmitted = submittedIt.next();
		assertEquals("rde062", firstSubmitted.getUser().getUsername());
		assertFalse(submittedIt.hasNext());
		
		// jur
		var unsubmitted = requestDao.findBySubmitted(false);
		var unsubmittedIt = unsubmitted.iterator();
		assertTrue(unsubmittedIt.hasNext());
		var firstUnsubmitted = unsubmittedIt.next();
		assertEquals("jja042", firstUnsubmitted.getUser().getUsername());
		assertFalse(unsubmittedIt.hasNext());
	}
	
	@Test
	public void testSaveRequest() {
		var request = initRequest();
		
//		request.getDevServers().get(0).setMiddleware(middleware);
		requestDao.save(request);
		var id = request.getId();
		var request2Opt = requestDao.findById(id);
		assertTrue(request2Opt.isPresent());
		var request2 = request2Opt.get();
		var devServers = request2.getDevServers();
		assertEquals(1, devServers.size());
		var devServer = devServers.iterator().next();
		var devMiddlewareRequests = devServer.getMiddlewareRequests();
		assertEquals(1, devMiddlewareRequests.size());
	}
	
	private Request initRequest() {
		var request = new Request();
		var devServers = initDevServers();
		request.setDevServers(devServers);
		var converter = new TransientToPersistentConverter();
//		converter.convertTransientToPersistent(request);
		return request;
	}
	
	private List<ServerRequest> initDevServers() {
		var devServers = new ArrayList<ServerRequest>();
		var devServer = new ServerRequest();
		devServer.setCpus(4);
		devServer.setRam(16);
		serverRequestDao.save(devServer);
		devServer.setMiddlewareRequests(initMiddlewareRequets());
		return devServers;
	}

	private List<MiddlewareRequest> initMiddlewareRequets() {
		var middlewareRequests = new ArrayList<MiddlewareRequest>();
		var middlewareRequest = new MiddlewareRequest();
		middlewareRequest.setEnvironment("test");
		middlewareRequests.add(middlewareRequest);
		middlewareRequestDao.save(middlewareRequest);
		return middlewareRequests;
	}
	
	@Test
	public void testFindByServiceRequester() {
		var roderiksRequests = requestDao.findByUser(roderik);
		assertNotNull(roderiksRequests);
		var jursRequests = requestDao.findByUser(jur);
		assertNotNull(jursRequests);
	}
	
}
