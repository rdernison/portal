package nl.uwv.otod.otod_portal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.uwv.otod.otod_portal.model.SizingRow;
import nl.uwv.otod.otod_portal.service.SizingRowService;

@Controller
@RequestMapping("sizingRow")
public class SizingRowController {

	private static Logger logger = LogManager.getLogger();
	
	private static final String SHOW_SIZING_ROWS = "showSizingRows";
	private static final String EDIT_SIZING_ROW = "editSizingRow";
	
	@Autowired
	private SizingRowService sizingRowService;
	
	@RequestMapping("")
	public String getAll(Model model) throws InvalidFormatException, IOException {
		model.addAttribute("sizingRow", new SizingRow());
		var sizingRows = sizingRowService.getAll();//reader.readOtodSizing(SettingsUtil.readSetting(SettingsUtil.SIZING_DOC_PATH));
		logger.info("Found sizing rows:  {} {}", sizingRows, sizingRows.iterator().hasNext()/*, sizingRows.size()*/);
		model.addAttribute("sizingRows", /*sizingRowService.getAll()*/sizingRows);
		model.addAttribute("sizingRow", new SizingRow());
		var statuses = initStatuses();
		model.addAttribute("statuses", statuses);
		var oses = initOses();
		model.addAttribute("oses", oses);
		return SHOW_SIZING_ROWS;
	}

	private List<String> initStatuses() {
		var statuses = new ArrayList<String>();
		statuses.add("");
		statuses.add("New");
		statuses.add("In-Progress");
		statuses.add("Delivered");
		
		return statuses;
	}

	private Set<String> initOses() {
		var oses = new HashSet<String>();
		oses.add("");
		oses.add("RedHat");
		oses.add("AIX");
		oses.add("Windows");
		return oses;
	}

	@RequestMapping("edit/{id}")
	public String getById(Model model, @PathVariable long id) throws InvalidFormatException, IOException {
		var sizingRowOpt = sizingRowService.getById(id);//allSizingRows.get(idInt - 1);
		if (sizingRowOpt.isPresent()) {
			var sizingRow = sizingRowOpt.get();
			logger.info("Got sizing row: arrival  date: '{}' delivery date: '{}'", sizingRow.getArrivalDate(), sizingRow.getOtodDeliveryDate());
			model.addAttribute("sizingRow", sizingRow);
		}
		return EDIT_SIZING_ROW;
	}

	@RequestMapping("save")
	public String save(/*@Valid*/ @ModelAttribute SizingRow sizingRow, Model model, @PathVariable long id) throws InvalidFormatException, IOException {
		sizingRowService.save(sizingRow);
		return SHOW_SIZING_ROWS;
	}
	
	@GetMapping("find")
	public String find(/*@Valid*/ @ModelAttribute SizingRow sizingRow, Model model) {
		logger.info("Finding sizing rows by status: '{}', application '{}', division '{}' and os '{}'", sizingRow.getStatus(), sizingRow.getApplicationName(), sizingRow.getDivision(), sizingRow.getOs());
		var sizingRows = sizingRowService.getByStatusApplicationNameDivisionOs(sizingRow.getStatus(), sizingRow.getApplicationName(), sizingRow.getDivision(), sizingRow.getOs());
		logger.info("Found {} sizing rows", sizingRows.size());
		model.addAttribute("sizingRows", sizingRows);
		var statuses = initStatuses();
		model.addAttribute("statuses", statuses);
		var oses = initOses();
		model.addAttribute("oses", oses);
		return SHOW_SIZING_ROWS;
	}
	
	@GetMapping("new")
	public String newSizingRow(@ModelAttribute SizingRow sizingRow, Model model) {
		return EDIT_SIZING_ROW;
	}
}
