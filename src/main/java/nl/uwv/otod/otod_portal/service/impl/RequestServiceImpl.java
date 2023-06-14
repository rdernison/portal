package nl.uwv.otod.otod_portal.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.MiddlewareRequest;
import nl.uwv.otod.otod_portal.model.Person;
import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.model.ServerRequest;
import nl.uwv.otod.otod_portal.model.ServiceRequest;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.persistence.RequestDao;
import nl.uwv.otod.otod_portal.service.RequestService;

@Service
@Log4j2
public class RequestServiceImpl implements RequestService {

	@Autowired
	private RequestDao requestDao;
	
	@Override
	public List<Request> getAll() {
		return requestDao.findAllOrderedByCreationDateDesc();
	}

	@Override
	public Optional<Request> get(Long id) {
		return requestDao.findById(id);
	}

	@Override
	public void save(Request request) {
		log.info("Saving request");
		var devServerRequests = request.getDevServers();
		log.info("Found dev server requests {}", devServerRequests);
		if (devServerRequests != null) {
			iterateServers(devServerRequests, "Dev");
		}
		var testServerRequests = request.getTestServers();
		log.info("Found test server requests {}", testServerRequests);
		if (testServerRequests != null) {
			iterateServers(testServerRequests, "Test");
		}
		requestDao.save(request);
	}
	
	private void iterateServers(List<ServerRequest> serverRequests, String environment) {

		for (var serverRequest : serverRequests) {
			log.info("Got server request: environment = {}", serverRequest.getEnvironment());
			var middlewareRequests = serverRequest.getMiddlewareRequests();
			if (serverRequest.getEnvironment() == null || serverRequest.getEnvironment().length() == 0) {
				serverRequest.setEnvironment(environment);
			}
			log.info("Got middleware requests: {}", middlewareRequests);
			/*
			while (hasEmptyMiddleware(middlewareRequests)) {
				removeEmptyMiddleware(middlewareRequests);
			}
			*/
		}
	}

	private boolean hasEmptyMiddleware(Set<MiddlewareRequest> middlewareRequests) {
		var hasEmptyMiddleware = false;
		if (middlewareRequests != null) {
			/* TODO check
			for (var middlewareRequest : middlewareRequests) {
				if (middlewareRequest.getMiddleware() == null || middlewareRequest.getMiddleware().getId() == 0l) {
					log.info("Found empty middleware");
					hasEmptyMiddleware = true;
					break;
				}
			}*/
		}
		return hasEmptyMiddleware;
	}

	private void removeEmptyMiddleware(Set<MiddlewareRequest> middlewareRequests) {
//		for (var i = 0; i < middlewareRequests.size(); i++) {
		Iterator<MiddlewareRequest> mrIt = middlewareRequests.iterator();
		while (mrIt.hasNext()) {
			var middlewareRequest = mrIt.next();//(MiddlewareRequest)middlewareRequests.get(i);
			/* TODO check
			if (middlewareRequest.getMiddleware() == null || middlewareRequest.getMiddleware().getId() == 0l) {
				log.info("Removing item {}", middlewareRequest.getMiddleware().getName());
				middlewareRequests.remove(middlewareRequest);
				break;
			}*/
		}
	}

	@Override
	public Iterable<Request> getMyRequests(User user) {
		log.info("Getting requests by user: {} {} {}", user.getEmailAddress(), user.getId(), user.getUsername());
		return requestDao.findByEmailAddressServiceRequester(user.getEmailAddress());/*findByUser(user);*/
	}

	@Override
	public Iterable<Request> getByEmailAddress(String emailAddress) {
		log.info("Getting requests by email address: {}", emailAddress);
		var requests = requestDao.findByEmailAddressServiceRequester(emailAddress);
		var requestsIt = requests.iterator();
		return requests;
	}

	@Override
	public Iterable<Request> getSubmitted() {
		return requestDao.findBySubmitted(true);
	}

	@Override
	public void addDevUser(Request request) {
		log.info("Adding dev user, current: {}", request.getDevUsers().size());
		request.getDevUsers().add(new Person());
		log.info("After adding, found {} dev users", request.getDevUsers().size());
	}

	@Override
	public void addTestUser(Request request) {
		log.info("Adding test user");
		request.getTestUsers().add(new Person());
		log.info("After adding, found {} test users", request.getTestUsers().size());
	}

	@Override
	public void addDevServer(Request request) {
		log.info("Adding dev server, current: {}", request.getDevServers().size());
		request.getDevServers().add(new ServerRequest());
		log.info("After adding dev server, current: {}", request.getDevServers().size());
	}

	@Override
	public void addTestServer(Request request) {
		log.info("Adding test server, current: {}", request.getTestServers().size());
		request.getTestServers().add(new ServerRequest());
		log.info("After adding test server, current: {}", request.getTestServers().size());
	}

	@Override
	public void removeDevUser(Request request) {
		var numDevUsers = request.getDevUsers().size();
		request.getDevUsers().remove(numDevUsers - 1);
	}

	@Override
	public void removeDevServer(Request request) {
		var numDevServers = request.getDevServers().size();
		request.getDevServers().remove(numDevServers - 1);		
	}

	@Override
	public void removeTestUser(Request request) {
		var numTesetUsers = request.getTestUsers().size();
		request.getTestUsers().remove(numTesetUsers - 1);		
	}

	@Override
	public void removeTestServer(Request request) {
		var numTestServers = request.getTestServers().size();
		request.getTestServers().remove(numTestServers - 1);
	}

	@Override
	public void deleteRequest(Request request) {
		requestDao.delete(request);
	}
	
}
