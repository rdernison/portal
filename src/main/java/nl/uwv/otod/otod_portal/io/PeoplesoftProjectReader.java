package nl.uwv.otod.otod_portal.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.PeoplesoftProject;
import nl.uwv.otod.otod_portal.service.PeoplesoftProjectService;

@Component
@Log4j2
public class PeoplesoftProjectReader {

	@Autowired
	private PeoplesoftProjectService peoplesoftProjectService;
	
	public Iterable<PeoplesoftProject> readProjects(File file) throws InvalidFormatException, IOException {
		var workbook = new XSSFWorkbook(file);
		
		List<PeoplesoftProject> projects = null;
		var numberOfSheets = workbook.getNumberOfSheets();
		log.info("Number of sheets: {}", numberOfSheets);
		if (numberOfSheets > 0) {
			var sheet = workbook.getSheetAt(0);
			projects = readProjects(sheet);
		}
	
		return projects;
	}
	
	private List<PeoplesoftProject> readProjects(XSSFSheet sheet) {
		var projects = new ArrayList<PeoplesoftProject>();
		
		return projects;
	}
}
