package nl.uwv.otod.otod_portal.init;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.io.RequestReader;
import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.service.RequestService;
import nl.uwv.otod.otod_portal.service.UserService;

@Component
@Log4j2
public class RequestInitializer {
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private UserService userService;
	
	public void checkRequests() {
		log.info("Checking requests");
		var requests = requestService.getAll();
		var requestIt = requests.iterator();
		if (!requestIt.hasNext()) {
			log.info("No requests found, reading them");
			try {
				readAndStoreRequests();
			} catch (IOException e) {
				log.error(e.toString());
				e.printStackTrace();
			}
		} else {
			log.info("Requests found, checking dates");
			// Titel	Opgepakt door	OToD projectnaam	Datum van inzending	Gewenste opleverdatum	Opgeleverd	Naam Serviceaanvrager	Naam budgethouder	Getekend contract	is gewijzigd	Wijzigingsdatum
			var firstRequest = requestIt.next();
			var desirableDeliveryDateStr = firstRequest.getDesirableDeliveryDateStr();
			log.info("Desirable delivery date str = {}", desirableDeliveryDateStr);
			log.info("Desirable delivery date  = {}", firstRequest.getDesirableDeliveryDate());
			if (false && (desirableDeliveryDateStr == null || desirableDeliveryDateStr.contains("2020-09-15"))) {
				correctRequests(requestIt, firstRequest);
			}
		}
		
		var allRequests = requestService.getAll();
		
		var olderThanAMonth = true;
		var aMonthAgo = LocalDate.now();
		aMonthAgo = aMonthAgo.minusMonths(1);
		for (var req : allRequests) {
			var date = req.getDate();
			if (date !=  null && date.isAfter(aMonthAgo)) {
				olderThanAMonth = false;
			}
		}
		
		if (olderThanAMonth) {
			var requestReader = new RequestReader();
			try {
				var requestsFromPipedLines = requestReader.readRequestsFromPipedLines();
				
				for (var request : requestsFromPipedLines) {
					
					if (request != null && !findRequestInDb(request, allRequests)) {
						requestService.save(request);
					}
				}
			} catch(IOException e) {
				log.error(e.toString());
				e.printStackTrace();
			}
		}
		
	}
	
	private boolean findRequestInDb(Request request, Iterable<Request> requestsInDb) {
		boolean found = false;
		var requestsInDbIt  = requestsInDb.iterator();
		
		while (requestsInDbIt.hasNext()) {
			var requestInDb = requestsInDbIt.next();
			try {
			if (requestInDb != null && requestInDb.getTitle() != null && requestInDb.getTitle().equals(request.getTitle())) {
				found = true;
				break;
			}
			} catch(NullPointerException e) {
				log.error("NPE request: {} requestInDb {}", request, requestInDb);
			}
		}
		return found;
	}
	
	@Deprecated
	private void correctRequests(Iterator<Request> requestIt, Request firstRequest) {
		/*
		var requestsFromFile = new RequestReader().readRequests();
		correctRequest(firstRequest, requestsFromFile);
		while (requestIt.hasNext()) {
			var request = requestIt.next();
			correctRequest(request, requestsFromFile);
		}*/
	}
	
	private void correctRequest(Request request, List<Request> requestsFromFile) {
		log.info("Correcting request: {}", request.getTitle());
		if (request != null) {
			var requestFromFile = findRequest(request, requestsFromFile);
			log.info("Request from file: {}", requestFromFile == null ? "NULL" : requestFromFile.getTitle());
			if (requestFromFile != null) {
				request.setDesirableDeliveryDate(requestFromFile.getDesirableDeliveryDate());
				request.setNameServiceRequester(requestFromFile.getNameServiceRequester());
				request.setNameBudgetOwner(requestFromFile.getNameBudgetOwner());
				request.setEmailAddressBudgetOwner(requestFromFile.getEmailAddressBudgetOwner());
				requestService.save(request);
			}
		}
	}
	
	private Request findRequest(Request request, List<Request> requestsFromFile) {
		Request foundRequest = null;
		for (var  requestFromFile : requestsFromFile) {
			if (requestFromFile.getTitle().equals(request.getTitle())) {
				foundRequest = requestFromFile;
				break;
			}
		}
		return foundRequest;
	}
	
	private void readAndStoreRequests() throws IOException {
		log.info("Reading requests");
		new RequestReader()
			.readRequestsFromPipedLines()
			.forEach(r -> requestService.save(r));
		
	}
	
	public void connectRequestsWithUsers() {
		var allRequests = requestService.getAll();
		var allUsers = userService.getAll();
		for (var request : allRequests) {
			var emp = request.getEmployee();
//logger.info("Employee from request: {}", emp);	
			var userFromReq = request.getUser();
			if (userFromReq == null && emp != null) {
				for (var user : allUsers) {
					var emailAddress = user.getEmailAddress();
					var emailBeforeAndAfterAt = emailAddress.split("@");
					var firstLastName = emailBeforeAndAfterAt[0].split("\\.");
					var empLower = emp.toLowerCase();
					if (empLower.contains(firstLastName[0]) && empLower.contains(firstLastName[1])) {
						log.info("Found user: {}", user.getUsername());
						request.setUser(user);
						requestService.save(request);
						break;
					}
				}
			}
		}
	}


}
