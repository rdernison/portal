package nl.uwv.otod.otod_portal.init;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.service.RequestService;

@Component
@Log4j2
public class DuplicateRequestRemover {
	@Autowired
	private RequestService requestService;
	
	public void removeDuplicateRequests() {
		log.info("Removing duplicate requests");
		var allRequests = requestService.getAll();
		for (var request : allRequests) {
			var duplicates = getDuplicates(request, allRequests);
			removeDuplicates(duplicates);
		}
	}

	private List<Request> getDuplicates(Request request, List<Request> allRequests) {
		log.info("Getting duplicates for pn {}, tit {}", request.getProjectName(), request.getTitle());
		var duplicates = new ArrayList<Request>();
		
		for (var req : allRequests) {
			if (nullOrEquals(req.getProjectName(), request.getProjectName()) && 
					
							nullOrEquals(req.getTitle(), request.getTitle())  && req.getId() != request.getId()) {
				log.info("Found duplicate");
				duplicates.add(req);
			}
		}
		return duplicates;
	}
	
	private boolean nullOrEquals(String s1, String s2) {
		var equals = false;
		if (s1 == null) {
			if (s2 == null || s2.equals("")) {
				equals = true;
			}
		} else if (s2 == null) {
			if (s1.equals("")) {
				equals = true;
			}
		}
		
		return equals;
	}
	
	private void removeDuplicates(List<Request> duplicates) {
		for (var request : duplicates) {
			requestService.deleteRequest(request);
		}
	}
}
