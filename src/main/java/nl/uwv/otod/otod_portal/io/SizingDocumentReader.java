package nl.uwv.otod.otod_portal.io;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;
import nl.uwv.otod.otod_portal.model.SizingDocumentRowStatus;
import nl.uwv.otod.otod_portal.model.SizingRow;

@Slf4j
@Deprecated
public class SizingDocumentReader {
	
	

	private static final int STARTING_ROW = 2;
	
	public List<SizingRow> readSizingRows(Path path) throws InvalidFormatException, IOException {
		log.info("Reading sizing rows from " + path.toString());
		var sizingRows = new ArrayList<SizingRow>();
		try (var workbook = new XSSFWorkbook(path.toFile())) {
			var worksheet = workbook.getSheetAt(3);
			var numRows = worksheet.getLastRowNum();
			for (var rownum = STARTING_ROW; rownum < numRows; rownum++) {
				var row = worksheet.getRow(rownum);
				var sizingRow = convertRow(row);
				sizingRows.add(sizingRow);
			}
			return sizingRows;
		}
	}
	
	private SizingRow convertRow(XSSFRow xssfRow) {
		log.info("Converting row");
		var sizingRow = new SizingRow();
		sizingRow.setEmployee(readStringFromCell(xssfRow.getCell(0)));
		var arrivalDate = readDateFromCell(xssfRow.getCell(1));
		sizingRow.setArrivalDate(arrivalDate);
		/* TODO set status
		SizingDocumentRowStatus status = SizingDocumentRowStatus.valueOf(xssfRow.getCell(2).getStringCellValue());
		sizingRow.setStatus(status);
		*/
		sizingRow.setOtodDeliveryDate(readDateFromCell(xssfRow.getCell(3)));
		sizingRow.setWave(readStringFromCell(xssfRow.getCell(6)));
		sizingRow.setApplicationName(readStringFromCell(xssfRow.getCell(7)));
		sizingRow.setDivision(readStringFromCell(xssfRow.getCell(8)));
		// columns i and j are missing
		sizingRow.setServerName(readStringFromCell(xssfRow.getCell(10)));
		sizingRow.setOtodProjectName(readStringFromCell(xssfRow.getCell(11)));
		sizingRow.setCurrentPlatform(readStringFromCell(xssfRow.getCell(12)));
		sizingRow.setEnvironment(readStringFromCell(xssfRow.getCell(13)));
		sizingRow.setUwvIpAddress(readStringFromCell(xssfRow.getCell(14)));
		try {
			sizingRow.setVCpu(Integer.parseInt(readStringFromCell(xssfRow.getCell(15))));
		} catch(NumberFormatException e) {
			sizingRow.setVCpu(0);
		}
		try {
			sizingRow.setMemory(Integer.parseInt(readStringFromCell(xssfRow.getCell(16))));
		} catch (NumberFormatException e) {
			sizingRow.setMemory(0);
		}
		sizingRow.setStorage(readStringFromCell(xssfRow.getCell(17)));
		sizingRow.setRole(readStringFromCell(xssfRow.getCell(18)));
		sizingRow.setDiscussionAction(readStringFromCell(xssfRow.getCell(19)));
		sizingRow.setProceed(readStringFromCell(xssfRow.getCell(20)));
		sizingRow.setSprint(readStringFromCell(xssfRow.getCell(21)));
		sizingRow.setDatacenter(readStringFromCell(xssfRow.getCell(22)));
		sizingRow.setDxcEnvironment(readStringFromCell(xssfRow.getCell(23)));
		return sizingRow;
	}
	
	private Date readDateFromCell(XSSFCell cell) {
		Date date = null;
		if (cell != null) {
			date = cell.getDateCellValue();
		}
		return date;
	}
	
	private String readStringFromCell(XSSFCell cell) {
		String string = null;
		if (cell != null) {
			var cellType = cell.getCellType();
			if (cellType == CellType.NUMERIC) {
				string = String.format("%f", cell.getNumericCellValue());
			} else {
				string = cell.getStringCellValue();
			}
		}
		return string;
	}
}
