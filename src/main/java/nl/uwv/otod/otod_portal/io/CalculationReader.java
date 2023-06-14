package nl.uwv.otod.otod_portal.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.uwv.otod.otod_portal.model.Calculation;
import nl.uwv.otod.otod_portal.service.OsService;

@Component
public class CalculationReader {
	
	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	private OsService osService;

	public List<Calculation> readCalculations() throws IOException {
		var calculations = new ArrayList<Calculation>();
		try (var in = getClass().getResourceAsStream("/otod-overview-calculations.csv");
				var br = new BufferedReader(new InputStreamReader(in))) {
			var line = "";
			while ((line = br.readLine()) != null) {
				logger.info("Found line: {}", line);
				var calculation = makeCalculation(line);
				logger.info("Found calculation: {} {} {} {} {} {}", calculation.getName(), calculation.getNumberOfProcessors(),calculation.getRam(), calculation.getSystemDisk(), calculation.getExtraHarddiskSpace(), calculation.getCostPerServerPerMonth());
				calculations.add(calculation);
			}
		}
		
		return calculations;
	}

	private Calculation makeCalculation(String line) {
		
		var cols = line.split("\t");
		logger.info("Found {} columns", cols.length);
		var calculation = /*Calculation.builder().build();*/new Calculation();
		calculation.setName(cols[0]);
		logger.info("Finding os : {}" , cols[1]);
		var oses = osService.getByName(cols[1]);
		calculation.setOsName(oses.size() > 0 ? oses.get(0).getName() : cols[1]);
		calculation.setNumberOfProcessors(Integer.parseInt(cols[2]));
		calculation.setRam(Integer.parseInt(cols[3]));
		calculation.setSystemDisk(Integer.parseInt(cols[4]));
		calculation.setExtraHarddiskSpace(Integer.parseInt(cols[5]));
		var costWithSign = cols[6];
		var cost = costWithSign.split(" ");
		var costPerServerPerMonth = Double.parseDouble(cost[1]);
		calculation.setCostPerServerPerMonth(costPerServerPerMonth);
		// Processoren	RAM (GB)	Systeemschijf (GB)	Extra Harddisk ruimte (GB)	Totale kosten server p/m
		return calculation;
	}
}
