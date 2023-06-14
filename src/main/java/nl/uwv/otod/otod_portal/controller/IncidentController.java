package nl.uwv.otod.otod_portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IncidentController {

	private static final String INCIDENT = "incident";

	@RequestMapping("/newIncident")
	public String prepareIncident() {
		
		return INCIDENT;
	}
}
