package nl.uwv.otod.otod_portal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.model.Disk;
import nl.uwv.otod.otod_portal.service.DiskService;
import nl.uwv.otod.otod_portal.service.OsService;
import nl.uwv.otod.otod_portal.service.ServerService;

@Controller
@RequestMapping("/disk")
public class DiskController {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String DISK = "disk";
	private static final String DISKS = "disks";
	private static final String NEW_DISK = "newDisk";
	private static final String SERVERS = "servers";
	private static final String OSES = "oses";
	private static final String SHOW_DISKS = "showDisks";
	
	@Autowired
	private DiskService diskService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private OsService osService;
	
	
	@RequestMapping("")
	public String allDisks(Model model) {
		LOGGER.info("Getting all disks");
		
		var disks = diskService.getAll();
		model.addAttribute("disks", disks);
	
		model.addAttribute(DISK, new Disk());
		return SHOW_DISKS;
	}
	
	@RequestMapping("new")
	public String newDisk(Model model) {
		model.addAttribute(DISK, new Disk());
		var servers = serverService.getAll();
		model.addAttribute(SERVERS, servers);
	
		// suggestie: alleen os
		var oses = osService.getAll();
		model.addAttribute(OSES, oses);
		return NEW_DISK;
	}
	
	@RequestMapping("create")
	public String createDisk(/*@Valid*/ @ModelAttribute Disk disk, Model model) {
		LOGGER.info("Saving disk");
		disk.setComputerName(disk.getServer().getName());
		diskService.save(disk);
		return SHOW_DISKS;
	}
	
	@GetMapping("/find")
	public String findServers(/*@Valid */@ModelAttribute Disk disk, Model model) {
		LOGGER.info("Got disk: {}" , disk);		
		var name = disk.getComputerName();
		var fileSystem = disk.getFileSystem();
		LOGGER.info("Finding disks by computer name '{}' and file system '{}'" , name, fileSystem);
		var disks = diskService.getByComputerNameAndFileSystem(name, fileSystem.getName());
		LOGGER.info("Found {} disks", disks.size());
		model.addAttribute(DISKS, disks);
		return SHOW_DISKS;
	}


}
