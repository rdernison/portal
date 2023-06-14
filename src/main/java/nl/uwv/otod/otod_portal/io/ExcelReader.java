package nl.uwv.otod.otod_portal.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.service.OsService;

@Component
public class ExcelReader extends GenericExcelReader {

	private static final String BLANK = "BLANK";
	
	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	private OsService osService;
	
	public List<Server> readServers(File file) throws IOException, InvalidFormatException {
		List<Server> servers = null;
		try(FileInputStream in = new FileInputStream(file)) {
			logger.info("Opened stream");
//			POIFSFileSystem fileSystem = new POIFSFileSystem(file, true);
//			Biff8EncryptionKey.setCurrentUserPassword("Alleen4Ons?");
			servers = readDataFromXssf(in);
			logger.info("Found servers? {}", servers != null);
			if (servers != null) {
				logger.info("Found {} servers", servers.size());
			}
//			readDataFromHssf(in);
		}
		
		return servers;
	}

	private List<Server> readDataFromXssf(FileInputStream in) {
		List<Server> servers = null;
		try { 
			var workbook = new XSSFWorkbook(in);
			logger.info("Got workbook");
			var numberOfSheets = workbook.getNumberOfSheets();
			logger.info("Number of sheets: {}", numberOfSheets);
			if (numberOfSheets > 0) {
				var sheet = workbook.getSheetAt(0);
				servers = readDataFromSheet(sheet);
				logger.info("Found servers? {}", servers != null);
				if (servers != null) {
					logger.info("Found {} servers", servers.size());
				}
			}
			workbook.close();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		return servers;
	}
	
	private List<Server> readDataFromSheet(Sheet sheet) {
		logger.info("Got sheet {} rows - first {}", sheet.getLastRowNum(), sheet.getFirstRowNum());
		var servers = new ArrayList<Server>();
		for (var i = sheet.getFirstRowNum() + 1; i < sheet.getLastRowNum(); i++) {
			var row = sheet.getRow(i);
			logger.info("Got row {}", i);
			var server = readDataFromRow(row);
			logger.info("Found server: {} {}", server.getName(), server.getProjectName());
			servers.add(server);
		}		
		return servers;
	}

	private Server readDataFromRow(Row row) {
		var server = new Server();
		logger.info("Cell count: {}", row.getLastCellNum());
		if (!row.getCell(0).getCellType().name().equals(BLANK)) {
			server.setProjectName(row.getCell(0).getStringCellValue());
		}
		if (!row.getCell(1).getCellType().name().equals(BLANK)) {
			server.setOsName(row.getCell(1).getStringCellValue());
			var oses = osService.getByName(server.getOsName());
			if (oses.size()   > 0) {
				var os = oses.get(0);
				server.setOs(os);
			}
		}
		logger.info("OS {}", server.getOsName());
		if (!row.getCell(2).getCellType().name().equals(BLANK)) {
			server.setName(row.getCell(2).getStringCellValue());
		}
		logger.info("Name: {}", server.getName());
		if (row.getLastCellNum() > 3) {
			logger.info("More than 3 cols");
			var thirdCell = row.getCell(3);
			var cellType = thirdCell.getCellType();
			var cellTypeName = cellType.name();
			logger.info("Cell 3: type={}", thirdCell.getCellType());
			
			if (!cellTypeName.equals(BLANK)) {
				server.setIpAddress(cellTypeName.equals("STRING")
						? thirdCell.getStringCellValue()
						: convertDoubleToIp(thirdCell.getNumericCellValue()));
			}
			handleUsername(row, server);
		} else {
			logger.info("Only 3 cells?");
		}
		
		return server;
	}

	private void handleUsername(Row row, Server server) {
		if (row.getLastCellNum() > 4) {
			logger.info("More than 4 cols");
			if (!row.getCell(5).getCellType().name().equals(BLANK)) {
				server.setUsername(row.getCell(4).getStringCellValue());
				logger.info("User name: {}", server.getUsername());
				server.setAdminPassword(row.getCell(5).getStringCellValue());
			}
			handleAdminPassword(row, server);
		} else {
			logger.info("Only 4 cells?");
		}
	}

	private void handleAdminPassword(Row row, Server server) {
		logger.info("Admin password: {}", server.getAdminPassword());
		if (row.getLastCellNum() > 6) {
			logger.info("More than 6 cols");
			if (!row.getCell(6).getCellType().name().equals(BLANK)) {
				server.setUsername2(row.getCell(6).getStringCellValue());
			}
			logger.info("User name 2: {}", server.getUsername2());
			if (!row.getCell(7).getCellType().name().equals(BLANK)) {
				server.setPassword2(row.getCell(7).getStringCellValue());
			}
			logger.info("Password 2: {}", server.getPassword2());
		}
	}

	private String convertDoubleToIp(double doubleValue) {
		var s = String.format("%f", doubleValue);
		var beforeDot = s.substring(0, s.indexOf('.'));
		var a = beforeDot.substring(0, 2);
		var b = beforeDot.substring(2, 5);
		var c = beforeDot.substring(5, 8);
		var d = beforeDot.substring(8);
		logger.info("Combined: {}.{}.{}.{}", a,b,c,d);
		return String.format("%s.%s.%s.%s",a,b,c,d);
	}

}
