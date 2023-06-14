package nl.uwv.otod.otod_portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController extends LoginLogoutController {
	private static Logger logger = LogManager.getLogger();
	

	@RequestMapping("/login")
	public String prepareLogin(Model model, HttpServletRequest req, HttpServletResponse res) {
		logger.info("Preparing login page");
//		model.addAttribute("requestCount", getRequestCount());
//		model.addAttribute("projectCount", getProjectCount());
		var serverCount = getServerCount();
		req.setAttribute(SERVER_COUNT, serverCount);
		model.addAttribute(SERVER_COUNT, serverCount);
		logger.info("Got server count: {}", model.getAttribute(SERVER_COUNT));
		return "login";
	}
}
