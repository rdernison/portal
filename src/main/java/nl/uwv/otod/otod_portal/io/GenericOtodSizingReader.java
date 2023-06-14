package nl.uwv.otod.otod_portal.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import nl.uwv.otod.otod_portal.converter.ExcelLineToSizingRowConverter;
import nl.uwv.otod.otod_portal.model.SizingRow;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

public class GenericOtodSizingReader extends GenericExcelReader {

	private static Logger logger = LogManager.getLogger();
	
	public static void main(String[] args) throws InvalidFormatException, IOException {
		logger.info("Running main method");
		var genericOtodSizingReader = new GenericOtodSizingReader();
		genericOtodSizingReader.readOtodSizing(SettingsUtil.readSetting(SettingsUtil.SIZING_DOC_PATH));
	}
	
	public List<SizingRow> readOtodSizing(String path) throws IOException, InvalidFormatException {
		logger.info("Reading otod sizing doc");
		var file = new File(path);
		var workbook = WorkbookFactory.create(file);
		logger.info("Read workbook");
		var numberOfSheets = workbook.getNumberOfSheets();
		logger.info("Number of sheets: {}" , numberOfSheets);
		var sheet = workbook.getSheetAt(3);
		var sheetName = sheet.getSheetName();
		logger.info("Found sheet: {}" , sheetName);
		var lastRowNum = sheet.getLastRowNum();
		logger.info("Found {} rows", lastRowNum);
		var requests = new ArrayList<SizingRow>();
		var excelLineToRowConverter = new ExcelLineToSizingRowConverter();
		for (int i = 2; i < lastRowNum; i++) {
			var row = sheet.getRow(i);
			if (row == null) {
				logger.info("Row == null, breaking");
				break;
			}
			logger.info("Making request {}", i);
			var sizingLine = excelLineToRowConverter.convertExcelLine(row, (long)i - 2);
			if (sizingLine.getApplicationName() == null || sizingLine.getApplicationName().length() == 0) {
				logger.info("No application name: blocking");
				break;
			} else if (sizingLine != null) {
				requests.add(sizingLine);
				logger.info("Added line to list");
			}
			
		}
		
		return requests;
	}
	
}
