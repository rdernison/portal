package nl.uwv.otod.otod_portal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.service.FileSystemService;

@Controller
@RequestMapping("/fileSystem")
public class FileSystemController {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String SHOW_FILE_SYSTEMS = "showFileSystems";
	
	private static final String SHOW_FILE_SYSTEM = "showFileSystem";

	@Autowired
	private FileSystemService fileSystemService;
	
	@RequestMapping("")
	public String allOss(Model model) {
		LOGGER.info("Getting all OS'es");
		var allFileSystems = fileSystemService.getAll();
		
		model.addAttribute("fileSystems", allFileSystems);
	
		return SHOW_FILE_SYSTEMS;
	}
	
	@RequestMapping("/{id}")
	public String getOs(@PathVariable long id, Model model) {
		LOGGER.info("Getting OS {}", id);
		var optFileSysgtem = fileSystemService.getById(id);
		
		if (optFileSysgtem.isPresent()) {
			var fileSystem = optFileSysgtem.get();			
			model.addAttribute("fileSystem", fileSystem);
		}
		
	
		return SHOW_FILE_SYSTEM;
	}
	
}
