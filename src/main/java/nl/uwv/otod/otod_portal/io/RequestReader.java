package nl.uwv.otod.otod_portal.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

public class RequestReader extends GenericReader { 

	private final static Logger logger = LogManager.getLogger();
	
	
	private String requestFilename;
	private String applicationnameFilename;
	private String applicationDescriptionFilename;
	private String applicationtypeFilename;
	private String nameBudgetOwnerFilename;
	private String emailBudgetOwnerFilename;
	private String additionalRemarksFilename;
	private String additionalRemarks1Filename;
	private String additionalRemarks2Filename;
	private String emailAddressServiceProviderFilename;
	
	public RequestReader() {
		try {
			requestFilename = SettingsUtil.readSetting("REQUEST_FILENAME");
			applicationnameFilename = SettingsUtil.readSetting("APPLICATION_NAME_FILENAME");
			applicationDescriptionFilename = SettingsUtil.readSetting("APPLICATION_DESCRIPTION_FILENAME");
			applicationtypeFilename = SettingsUtil.readSetting("APPLICATION_TYPE_FILENAME");
			nameBudgetOwnerFilename = SettingsUtil.readSetting("NAME_BUDGET_OWNER_FILENAME");
			emailBudgetOwnerFilename = SettingsUtil.readSetting("EMAIL_BUDGET_OWNER_FILENAME");
			additionalRemarksFilename = SettingsUtil.readSetting("ADDITIONAL_REMARKS_FILENAME");
			additionalRemarks1Filename = SettingsUtil.readSetting("ADDITIONAL_REMARKS1_FILENAME");
			additionalRemarks2Filename = SettingsUtil.readSetting("ADDITIONAL_REMARKS2_FILENAME");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Request> readRequestsFromPipedLines() throws IOException {
		var requests = new ArrayList<Request>();
		var path = Paths.get(inputPath, requestFilename);
		
		var allLines = Files.readAllLines(path);
			
		for (var line : allLines) {
			
			if ( !line.contains("Decommissioned")) {
				var request = convertPipedLine(line);
				requests.add(request);
			}
		}
		return requests;

	}
	
	private Request convertPipedLine(String line) {
		var request = new Request();
		var cols = line.split("\\|");
		int requestId = Integer.parseInt(cols[0]);
		request.setTitle(cols[4]);
		var ms = Long.parseLong(cols[7]) * 1000;
		var date = convertMillisToLocalDat(ms);
		var requestedEndMS = Long.parseLong(cols[8]) * 1000;
		var requestDate = convertMillisToLocalDat(requestedEndMS);
		request.setDate(date);
		request.setRequestDate(requestDate);
		try {
			var applicationName = readApplicatienaam(requestId);
			request.setApplicationName(applicationName);
			var applicationDescription = readApplicatieomschrijving(requestId);
			request.setApplicationDescription(applicationDescription);
			
			var budgetOwnerName = readBudgethouderName(requestId);
			request.setNameBudgetOwner(budgetOwnerName);
			var budgetOwnerEmail = readBudgethouderEmail(requestId);
			request.setEmailAddressBudgetOwner(budgetOwnerEmail);
			
			var applicationType = readApplicatietype(requestId);
			var additionalRemarks = readAdditionalRemarks(requestId);
			var additionalRemarks1 = readAdditionalRemarks1(requestId);
			var additionalRemarks2 = readAdditionalRemarks2(requestId);
			
			request.setAdditionalRemarks(additionalRemarks);
			request.setAdditionalRemarks1(additionalRemarks);
			request.setAdditionalRemarks2(additionalRemarks);
			request.setApplicationType(applicationType);
			
			var comment = readComment(requestId);
			request.setComment(comment);
			var deliveryDate = readDeliveryDate(requestId);
			request.setDeliveryDate(deliveryDate);
			var department = readDepartment(requestId);
			request.setDepartment(department);
			var emailAddressBudgetOwner = readEmailaddressBudgetOwner(requestId);
			request.setEmailAddressBudgetOwner(emailAddressBudgetOwner);
			var emailAddressDelegate = readEmailAdderssDelegate(requestId);
			request.setEmailAddressDelegate(emailAddressDelegate);
			var emailAddressServiceProvider = readEmailAddressServiceProvider(requestId);
			request.setEmailAddressServiceProvider(emailAddressServiceProvider);
			var emailAddressServiceRequester = readEmailAddressServiceRequester(requestId);
			request.setEmailAddressServiceRequester(emailAddressServiceRequester);
			
//			request.setInterfs(interfs);
			var nameBudgetOwner = readBudgethouderName(requestId);
			request.setNameBudgetOwner(nameBudgetOwner);
//			var nameServiceRequester = readS
//			request.setNameServiceRequester(nameServiceRequester);
//			
//			request.setSignedContract(signedContract);
			
//			request.setdev
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return request;
	}
	
	private LocalDate readDeliveryDate(int requestId) throws IOException {
		String str = readStringByFilenameAndId("opleverdatum", requestId);
		var dateAndTime = str.split(" ");
		var dateStr = dateAndTime[0];
		var date = LocalDate.parse(dateStr);
		return date;
	}

	private String readEmailAddressServiceProvider(int requestId) throws IOException {
		logger.info("Reading email address service provider");
		String applicationName = readStringByFilenameAndId(emailAddressServiceProviderFilename, requestId);
		return applicationName;
	}

	private String readEmailAddressServiceRequester(int requestId) throws IOException {
		String emailAddressServiceRequester = readStringByFilenameAndId("emailadres-serviceaanvrager", requestId);
		return emailAddressServiceRequester;
	}

	private String readEmailAdderssDelegate(int requestId) throws IOException {
		String emailAddressServiceRequester = readStringByFilenameAndId("emailadres-gedelegeerde", requestId);
		return emailAddressServiceRequester;
	}

	private String readEmailaddressBudgetOwner(int requestId) throws IOException {
		String emailAddressBudgetOwner = readStringByFilenameAndId("email-budgethouder", requestId);
		return emailAddressBudgetOwner;
	}

	private String readDepartment(int requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	private String readComment(int requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	private LocalDate convertMillisToLocalDat(long millis) {
		return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	private String readApplicatienaam(int requestId) throws IOException {
		logger.info("Reading application name");
		String applicationName = readStringByFilenameAndId(applicationnameFilename, requestId);
		return applicationName;
	}
	
	private String readStringByFilenameAndId(String filename, int requestId) throws IOException {
		String string = "";
		
		if (inputPath == null) {
			logger.error("Input path null");
		} else if (filename == null) {
			logger.error("file name null");
		} else {
			
			var path = Paths.get(inputPath, filename);
			var allLines = Files.readAllLines(path);
			
			for (var line : allLines) {
				var cols = line.split("\\|");
				
				if (cols.length < 3) {
					logger.error("Error reading request from line: {}", line);
				} else {
					var requestIdFromLine = cols[3];
					if (Integer.parseInt(requestIdFromLine) == requestId) {
						string = cols[7];
						break;
					}
				}
			}
		}
		return string;
	}
	
	private String readApplicatieomschrijving(int requestId) throws IOException {
		logger.info("Reading application description");
		String applicationDescription = readStringByFilenameAndId(applicationDescriptionFilename, requestId);
		
		if (applicationDescription.endsWith("\\N")) {
			applicationDescription = applicationDescription.substring(0, applicationDescription.length() - 2);
		}
		return applicationDescription;
	}
	
	private String readApplicatietype(int requestId) throws IOException {
		String applicationType = readStringByFilenameAndId(applicationtypeFilename, requestId);
		return applicationType;
	}

	private String readBudgethouderName(int requestId) throws IOException {
		// budgethouder-20220202.csv
		logger.info("Reading budget owner");
		String budgetOwnerName = readStringByFilenameAndId(nameBudgetOwnerFilename, requestId);
		return budgetOwnerName;
	}
	
	private String readBudgethouderEmail(int requestId) throws IOException {
		// budgethouder-20220202.csv
		logger.info("Reading budget owner");
		String budgetOwnerEmail = readStringByFilenameAndId(emailBudgetOwnerFilename, requestId);
		return budgetOwnerEmail;
	}
	
	private String readAdditionalRemarks(int requestId) throws IOException {
		// budgethouder-20220202.csv
		logger.info("Reading additional remarks");
		String budgetOwnerEmail = readStringByFilenameAndId(additionalRemarksFilename, requestId);
		return budgetOwnerEmail;
	}
	
	private String readAdditionalRemarks1(int requestId) throws IOException {
		// budgethouder-20220202.csv
		logger.info("Reading additional remarks");
		String budgetOwnerEmail = readStringByFilenameAndId(additionalRemarks1Filename, requestId);
		return budgetOwnerEmail;
	}
	
	private String readAdditionalRemarks2(int requestId) throws IOException {
		// budgethouder-20220202.csv
		logger.info("Reading additional remarks");
		String budgetOwnerEmail = readStringByFilenameAndId(additionalRemarks2Filename, requestId);
		return budgetOwnerEmail;
	}
	
}
