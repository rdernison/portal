package nl.uwv.otod.otod_portal.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import nl.uwv.otod.otod_portal.model.CostCenter;

public class CostCenterReader extends GenericExcelReader {

	public List<CostCenter> readCostCenters(InputStream in) throws EncryptedDocumentException, IOException {
		var costCenters = new ArrayList<CostCenter>();
		var workbook = WorkbookFactory.create(in);
		var firstSheet = workbook.getSheetAt(0);
		var lastRowNum = firstSheet.getLastRowNum();
		
		for (var i = 2; i < lastRowNum; i++) {
			var row = firstSheet.getRow(i);
			var costCenter = CostCenter
					.builder()
					.code(row.getCell(0).getStringCellValue())
					.description(row.getCell(1).getStringCellValue()).build();
			costCenters.add(costCenter);
		}
		return costCenters;
	}
	
	public List<String> readCostCentersFromTxtFile() throws UnsupportedEncodingException, IOException {
		var costCenters = new ArrayList<String>();
		try (var in = getClass().getResourceAsStream("/latest-import/kostenplaats.txt");
				var br = new BufferedReader(new InputStreamReader(in, "utf-8"))) {
			var line = "";
			while ((line = br.readLine()) != null) {
				costCenters.add(line);
			}
			
		}
		
		return costCenters;

	}
}
