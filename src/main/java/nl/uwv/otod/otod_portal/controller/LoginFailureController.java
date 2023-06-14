package nl.uwv.otod.otod_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.service.LoginFailureService;

@Controller
@RequestMapping("/loginFailure")
public class LoginFailureController {

	private static final String SHOW_LOGIN_FAILURES = "showLoginFailures";
	
	@Autowired
	private LoginFailureService loginFailureService;
	
	
	@RequestMapping("")
	public String getAll(Model model) {
		model.addAttribute("loginFailures", loginFailureService.getAllOrderedByCreatedDateDesc());
		return SHOW_LOGIN_FAILURES;
	}
}
	
