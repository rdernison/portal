package nl.uwv.otod.otod_portal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.service.OsService;

@Controller
@RequestMapping("/os")
public class OsController {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String SHOW_OSES = "showOses";
	
	private static final String SHOW_OS = "showOs";

	@Autowired
	private OsService osService;
	
	@RequestMapping("")
	public String allOss(Model model) {
		LOGGER.info("Getting all OS'es");
		var allOses = osService.getAll();
		
		model.addAttribute("oses", allOses);
	
		return SHOW_OSES;
	}
	
	@RequestMapping("/{id}")
	public String getOs(@PathVariable long id, Model model) {
		LOGGER.info("Getting OS {}", id);
		var optOs = osService.getById(id);
		
		if (optOs.isPresent()) {
			var os = optOs.get();			
			model.addAttribute("os", os);
		}
		
	
		return SHOW_OS;
	}
	
}
