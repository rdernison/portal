package nl.uwv.otod.otod_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.service.LoginSuccessService;

@Controller
@RequestMapping("/loginSuccess")
public class LoginSuccessController {

	private static final String SHOW_LOGIN_SUCCESSES = "showLoginSuccesses";
	
	@Autowired
	private LoginSuccessService loginSuccessService;
	
	
	@RequestMapping("")
	public String getAll(Model model) {
		model.addAttribute("loginSuccesses", loginSuccessService.getAll());
		return SHOW_LOGIN_SUCCESSES;
	}
}
	
