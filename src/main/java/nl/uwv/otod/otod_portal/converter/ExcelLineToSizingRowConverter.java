package nl.uwv.otod.otod_portal.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import nl.uwv.otod.otod_portal.controller.handler.BlankCellHandler;
import nl.uwv.otod.otod_portal.controller.handler.BooleanCellHandler;
import nl.uwv.otod.otod_portal.controller.handler.CellHandler;
import nl.uwv.otod.otod_portal.controller.handler.FormulaCellHandler;
import nl.uwv.otod.otod_portal.controller.handler.NoneCellHandler;
import nl.uwv.otod.otod_portal.controller.handler.NumericCellHandler;
import nl.uwv.otod.otod_portal.controller.handler.StringCellHandler;
import nl.uwv.otod.otod_portal.model.SizingRow;

public class ExcelLineToSizingRowConverter {

	private static Logger logger = LogManager.getLogger();
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
	
	// Opgepakt door	Datum  binnen gekomen bij OTOD	Status	Datum Opgeleverd  door OTOD	wave	Applicatienaam	Divisie	"Servernaam
	// (OToD IBM)"	OToD IBM Projectnaam	"Platform
	// (OToD IBM)"	Omgeving	"UWV IP adres
	// (OToD IBM)"	vCPU (aantal)	Geheugen (GB)	Opslag (GB)	Functie	Datacenter	Environment	DXC UWV Managed	Service Tier	Type server	OS	"Oracle DB
	// (Y/N)"	"Oracle MW
	// (zie opmerking)
	// (Y/N)"	Gewenste MW	T-Shirt Size CPU - MEM	Datadisk 1 (MANDATORY)	Datadisk 2	Datadisk 3	Datadisk 4	Datadisk 5	Datadisk 6	Datadisk 7	Datadisk 8	Datadisk 9	Servername	IP	"Post Action
	// AD-OU"	"Post Action
	// Flexera"	"Post Action
	// AV Endpoint"	"Post Action
	// Backup"	"Post Action
	// Backup Agent"	Opmerking

	public SizingRow convertExcelLine(Row row, long rowCount) {
		var sizingRow = new SizingRow();
		
		var secondCell = row.getCell(1);
		if (secondCell != null) {
			var secondCellType = secondCell.getCellType();
			logger.info("Second cell type = {}" , secondCellType);
		}
		var fourthCell = row.getCell(3);
		if (fourthCell != null) {
			var fourthCellType = fourthCell.getCellType();
			logger.info("fourth cell type = {}", fourthCellType);
		}
		logger.info("Making sizing line");
		sizingRow.setId(rowCount);
		sizingRow.setEmployee(convertCellToString(row.getCell(0)));
		var cal = Calendar.getInstance();
		cal.set(2004, Calendar.FEBRUARY, 19);
		var defaultDate = cal.getTime();
		try {
			var arrivalDate = row.getCell(1);
			if (arrivalDate != null) {
				logger.info("Got arrival date: {}", arrivalDate);
				sizingRow.setArrivalDate(sdf.parse(convertCellToString(row.getCell(1))));
			}
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		sizingRow.setDelivered(true); // depcrecated
		var stat = row.getCell(2).getStringCellValue();
		sizingRow.setStatus(stat/*us*/);
		var otodDeliveryDate = row.getCell(3);
		if (otodDeliveryDate != null) {
			logger.info("Otod delivery date: {}", otodDeliveryDate);
		}
		if (otodDeliveryDate != null) {
			sizingRow.setOtodDeliveryDate(row.getCell(3) == null ? defaultDate : row.getCell(3).getDateCellValue());
		}
		sizingRow.setWave(convertCellToString(row.getCell(4)));
		sizingRow.setApplicationName(convertCellToString(row.getCell(5)));
		sizingRow.setDivision(convertCellToString(row.getCell(6)));
		sizingRow.setServerName(convertCellToString(row.getCell(7)));
		sizingRow.setOtodProjectName(convertCellToString(row.getCell(8)));
		sizingRow.setCurrentPlatform(convertCellToString(row.getCell(9)));
		sizingRow.setEnvironment(convertCellToString(row.getCell(10)));
		sizingRow.setUwvIpAddress(convertCellToString(row.getCell(11)));
		var sVCpu = convertCellToString(row.getCell(12));
		sizingRow.setVCpu((int)Double.parseDouble(sVCpu.equals("") ? "0.0" : sVCpu));
		var sMemory = convertCellToString(row.getCell(13));
		if (sMemory.contains(" ")) {
			sMemory = sMemory.substring(0, sMemory.indexOf(" "));
		}
		sizingRow.setMemory(sMemory.equals("") ? 0 : (int)Double.parseDouble(sMemory));
		sizingRow.setStorage(convertCellToString(row.getCell(14)));
		sizingRow.setFunction(convertCellToString(row.getCell(15)));
		sizingRow.setDatacenter(convertCellToString(row.getCell(16)));
		sizingRow.setDxcEnvironment(convertCellToString(row.getCell(17)));
		sizingRow.setDxcUwvManaged(convertCellToString(row.getCell(18)));
		sizingRow.setServiceTier(convertCellToString(row.getCell(19)));
		sizingRow.setServerType(convertCellToString(row.getCell(20)));
		sizingRow.setOs(convertCellToString(row.getCell(21)));
		var odb = convertCellToString(row.getCell(22));
		sizingRow.setOracleDb((odb == null || odb.length() == 0) ? 'n' : odb.charAt(0));
		var omw = convertCellToString(row.getCell(23));
		sizingRow.setOracleMw((omw == null || omw.length() == 0) ? 'n' : omw.charAt(0));
		var dmw = convertCellToString(row.getCell(24));
		sizingRow.setDesirableMw((dmw == null || dmw.length() == 0) ? 'n' : dmw.charAt(0)); //gewenst middleware
		sizingRow.setTShiftSize(convertCellToString(row.getCell(25)));
		sizingRow.setDataDisk1(convertCellToString(row.getCell(26)));
		sizingRow.setDataDisk2(convertCellToString(row.getCell(27)));
		sizingRow.setDataDisk3(convertCellToString(row.getCell(28)));
		sizingRow.setDataDisk4(convertCellToString(row.getCell(29)));
		sizingRow.setDataDisk5(convertCellToString(row.getCell(30)));
		sizingRow.setDataDisk6(convertCellToString(row.getCell(31)));
		sizingRow.setDataDisk7(convertCellToString(row.getCell(32)));
		sizingRow.setDataDisk8(convertCellToString(row.getCell(33)));
		sizingRow.setDataDisk9(convertCellToString(row.getCell(34)));
		sizingRow.setDxcServerName(convertCellToString(row.getCell(35)));
		sizingRow.setIp(convertCellToString(row.getCell(36)));
		sizingRow.setPostActionAdOu(convertCellToString(row.getCell(37)));
		sizingRow.setPostActionFlexera(convertCellToString(row.getCell(38)));
		sizingRow.setPostActionBackup(convertCellToString(row.getCell(39)));
		sizingRow.setPostActionBackupAgent(convertCellToString(row.getCell(40)));
		sizingRow.setRemark(convertCellToString(row.getCell(41)));

		return sizingRow;
	}
	
	private String convertCellToString(Cell cell) {
		if (cell == null) {
			logger.warn("Cell null");
			return "";
		} else {
			logger.info("Getting handler for cell type: {}", cell.getCellType());
			var cellHandler = cellHandlers.get(cell.getCellType());
			var retVal = cellHandler.getValue(cell);
			return "" + retVal;
		}
	}

	private static Map<CellType, CellHandler<?>> cellHandlers = new HashMap<>();
	static {
		cellHandlers.put(CellType._NONE, new NoneCellHandler());
		cellHandlers.put(CellType.BLANK, new BlankCellHandler());
		cellHandlers.put(CellType.BOOLEAN, new BooleanCellHandler());
		cellHandlers.put(CellType.FORMULA, new FormulaCellHandler());
		cellHandlers.put(CellType.NUMERIC, new NumericCellHandler());
		cellHandlers.put(CellType.STRING, new StringCellHandler());
	}
	
}
