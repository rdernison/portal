package nl.uwv.otod.otod_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.service.CalculationService;

@Controller
public class CalculationController {

	private static final String SHOW_CALCULATIONS = "showCalculations";
	private static final String SHOW_MY_CALCULATIONS = "showMyCalculations";
	
	@Autowired
	private CalculationService calculationService;
	
	@RequestMapping("/calculation")
	public String getAllCalculations(Model model) {
		var calculations = calculationService.getAll();
		model.addAttribute("calculations", calculations);
		return SHOW_CALCULATIONS;
	}

	@RequestMapping("/myCalculations")
	public String getMyCalculations() {
		
		return SHOW_MY_CALCULATIONS;
	}
}
