package nl.uwv.otod.otod_portal.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bootstrap")
public class BootstrapController {

	@RequestMapping("")
	public String home(Model model) {
		model.addAttribute("datetime", new Date());
		model.addAttribute("username", "ade042");
		return "showBootstrap";
	}
}
