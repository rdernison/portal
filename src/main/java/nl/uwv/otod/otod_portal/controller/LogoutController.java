package nl.uwv.otod.otod_portal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController extends LoginLogoutController {

	private static Logger logger = LogManager.getLogger();
	
	@RequestMapping("/logout")
	public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Logging out");
		var auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    
//	    request.setAttribute("projectCount", getProjectCount());
//	    request.setAttribute("requestCount", getRequestCount());
	    var serverCount = getServerCount();
//	    request.setAttribute(SERVER_COUNT, serverCount);
		model.addAttribute(SERVER_COUNT, serverCount);

		return "login";
	}
}
