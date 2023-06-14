package nl.uwv.otod.otod_portal.init;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.io.CalculationReader;
import nl.uwv.otod.otod_portal.service.CalculationService;

@Component
@Log4j2
public class CalculationInitializer {
	@Autowired
	private CalculationReader calculationReader;
	
	@Autowired
	private CalculationService calculationService;
	
	public void readCalculations() throws IOException {
		log.info("Checking calculations");
		var calculationsFromService = calculationService.getAll();
		if (!calculationsFromService.iterator().hasNext()) {
			log.info("Found no calculations in db, reading them from file");
			var calculations = calculationReader.readCalculations();
			for (var calculation : calculations) {
				calculationService.save(calculation);
			}
		}
	}


}
