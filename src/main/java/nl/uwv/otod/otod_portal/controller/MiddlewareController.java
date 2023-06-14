package nl.uwv.otod.otod_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.service.MiddlewareService;

@Controller
@RequestMapping("/middleware")
public class MiddlewareController {

	@Autowired
	private MiddlewareService middlewareService;
	
	@RequestMapping("")
	public String admin(Model model) {
		var allMiddleware = middlewareService.getAll();
		model.addAttribute("allMiddleware", allMiddleware);
		return "adminMiddleware";
	}
}
