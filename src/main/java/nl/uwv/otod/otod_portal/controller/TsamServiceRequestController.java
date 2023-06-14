package nl.uwv.otod.otod_portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Deprecated
@Controller
@RequestMapping("/tsam-service-request")
public class TsamServiceRequestController {

	private static final String SHOW_ALL_SERVICE_REQUESTS = "showAllServiceRequests";

	@RequestMapping("")
	public String getAllServiceRequests() {
		
		return SHOW_ALL_SERVICE_REQUESTS;
	}
}
