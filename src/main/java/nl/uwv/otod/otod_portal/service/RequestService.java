package nl.uwv.otod.otod_portal.service;

import java.util.List;
import java.util.Optional;

import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.model.ServerRequest;
import nl.uwv.otod.otod_portal.model.ServiceRequest;
import nl.uwv.otod.otod_portal.model.User;

public interface RequestService {
	List<Request> getAll();

	Optional<Request> get(Long id);

	void save(Request request);

	Iterable<Request> getMyRequests(User user);

	Iterable<Request> getByEmailAddress(String emailAddress);

	Iterable<Request> getSubmitted();

	void addDevUser(Request request);

	void addTestUser(Request request);

	void addDevServer(Request request);

	void addTestServer(Request request);

	void removeDevUser(Request request);

	void removeDevServer(Request request);

	void removeTestUser(Request request);

	void removeTestServer(Request request);
	
	void deleteRequest(Request request);
}
