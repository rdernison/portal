package nl.uwv.otod.otod_portal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.model.UsedIpAddress;
import nl.uwv.otod.otod_portal.service.UsedIpAddressService;

@Controller
@RequestMapping("/usedIpAddress")
public class UsedIpAddressController {

	private static Logger logger = LogManager.getLogger();
	
	private static final String USED_IP_ADDRESSES = "usedIpAddresses";
	private static final String SHOW_USED_IP_ADDRESSES = "showUsedIpAddresses";
	private static final String NEW_USED_IP_ADDRESS = "newUsedIpAddress";
	private static final String USED_IP_ADDRESS = "usedIpAddress";
	private static final String EDIT_USED_IP_ADDRESS = "editUsedIpAddress";
	
	@Autowired
	private UsedIpAddressService usedIpAddressService;
	
	@RequestMapping("")
	public String getAll(Model model) {
		Iterable<UsedIpAddress> usedIpAddresses = usedIpAddressService.getAll();
		model.addAttribute(USED_IP_ADDRESSES, usedIpAddresses);
		return SHOW_USED_IP_ADDRESSES;
	}
	
	@RequestMapping("new")
	public String prepareAdd(Model model) {
		model.addAttribute(USED_IP_ADDRESS, new UsedIpAddress());
		return NEW_USED_IP_ADDRESS;
	}
	
	@RequestMapping("create")
	public String add(/*@Valid*/ @ModelAttribute UsedIpAddress usedIpAddress, Model model) {
		usedIpAddressService.save(usedIpAddress);
		model.addAttribute("allUsedIpAddresses", usedIpAddressService.getAll());
		return SHOW_USED_IP_ADDRESSES;
	}
	
	@RequestMapping("edit/{id}")
	public String prepareEdit(Model model, @PathVariable long id) {
		logger.info("Going to edit used Ip address; id = {}", id);
		var optUsedIp = usedIpAddressService.getById(id);
		if (optUsedIp.isPresent()) {
			var ip = optUsedIp.get();
			logger.info("Got ip addres: {} id = {}", ip.getAddress(), ip.getId());
			model.addAttribute(USED_IP_ADDRESS, ip);
		}
		return EDIT_USED_IP_ADDRESS;
	}
	
	@RequestMapping("save")
	public String save(/*@Valid*/ @ModelAttribute UsedIpAddress usedIpAddress, Model model) {
		logger.info("Saving userd ip address:");
		logger.info(" address = {}", usedIpAddress.getAddress());
		logger.info("id = {}" , usedIpAddress.getId());
		usedIpAddressService.save(usedIpAddress);
		model.addAttribute("allUsedIpAddresses", usedIpAddressService.getAll());
		return SHOW_USED_IP_ADDRESSES;
	}
	
}
