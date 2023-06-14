package nl.uwv.otod.otod_portal.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.service.OsService;
import nl.uwv.otod.otod_portal.service.ProjectService;
import nl.uwv.otod.otod_portal.util.StringUtil;

@Log4j2
public class ServerReader extends GenericReader {

	private static Logger logger = LogManager.getLogger();
	
	private ProjectService projectService;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private OsService osService;
	
	private IpAddressReader ipAddressReader;
	
	private PasswordReader passwordReader;
	
	public ServerReader(ProjectService projectService, OsService osService) {
		this.projectService = projectService;
		this.osService = osService;
	}
	
	public List<Server> readIbmServers() throws IOException {
		var allServers = new ArrayList<Server>();
		var path = Paths.get(inputPath, "servers.csv");
		
		var allLines = Files.readAllLines(path);
		
		for (var line : allLines) {
			var server = convertPipedLine(line);///convertIbmLine(line);
			allServers.add(server);
		}
		return allServers;
	}

	private Server convertIbmLine(String line) {
		logger.debug("Read ibm line: {}", line);
		var server = new Server();
		var subs = line.split("\t");
		var projectName = subs[1];
		var projects = projectService.getByName(projectName);
		
		try {
			logger.debug("Found {} columns, #projects = {}", subs.length, projects.size());
			if (projects.size() > 0) {
				var project = projects.get(0);
				server.setProject(project);
// Naam	Project	IP-adres	Virtuele CPU's	Fysieke cores	Besturingssysteem	Aanmaakdatum	Verwijderdatum	Status
				server.setName(subs[0]);
				server.setIpAddress(subs[2]);
				server.setVirtualCpus(subs[3].length() == 0 ? 0 : Integer.parseInt(subs[3]));
				server.setPhysicalCores(Double.parseDouble(subs[4]));
				server.setRam(subs[5].length() == 0 ? 0 : StringUtil.convertStringContainingSpaceToInteger(subs[5]));
				setSystemDisk(server, subs);
			}
		} catch (NullPointerException | NumberFormatException | DateTimeParseException e) {
//			logger.error(e.toString());
//			e.printStackTrace();
			writeErrorToFile(subs[0], subs[1], e.toString(), line);
		}
		return server;
	}

	private void setSystemDisk(Server server, String[] subs) {
		if (subs.length > 6) {
			server.setSystemDisk(Integer.parseInt(subs[6]));
			setOs(server, subs);
		}
	}

	private void setOs(Server server, String[] subs) {
		if (subs.length > 7) {
			var oses = osService.getByName(subs[7]);
			if (oses.size() > 0) {
				server.setOs(oses.get(0));
			}
			server.setProductionDate(LocalDate.parse(subs[8], formatter)); // dd/MM/yyyy
			if (subs[9].length() > 0) {
				server.setRemovalDate(LocalDate.parse(subs[9], formatter)); // dd/MM/yyyy
			}
			var status = subs[10];
			logger.info("Setting status: {}", subs[10]);
			if (status == null || status.length() == 0) {
				logger.warn("Status null, #kolommen: {}", subs.length);
			}
			server.setStatus(subs[10]);
		}
	}

	private void writeErrorToFile(String serverName, String projectName, String error, String line) {
		var path = Paths.get("c:/users/rde062/documents/otod-read-errors/" + serverName + "-" + projectName + ".txt");
		var output = line + "\n" + error; 
		try {
			Files.write(path, output.getBytes());
		} catch (IOException e) {
			logger.error("Error writing error to file {}" , e.toString());
			e.printStackTrace();
		}
		
	}
	
	public List<Server> readDxcServers() throws IOException {
		var allServers = new ArrayList<Server>();
		var path = Paths.get(inputPath, "Hostnames met w.txt");
		var allLines = Files.readAllLines(path);
		var firstLine = true;
			for (var line : allLines) {
				if (!line.contains("Decommissioned")) {
				if (!firstLine) {
					var server = convertDxcLine(line);
					if (server.getIpAddress() != null && server.getIpAddress().length() > 0) {
						allServers.add(server);
					}
				} else {
					firstLine = false;
				}
			}
		}
		
		return allServers;
	}
	
	private Server convertDxcLine(String line) {
		var server = new Server();
		var cols = line.split("\\;");
		var projectName = cols[0];
		logger.info("Found {} columns", cols.length);
		var projectsFromDb = projectService.getByName(projectName);
		server.setProjectName(projectName);
		if (projectsFromDb.size()  > 0) {
			server.setProject(projectsFromDb.get(0));
		} else {
			var project = new Project();
			if (projectName.length() == 0) {
				logger.warn("No name found, line = {}", line);
			}
			project.setName(projectName);
			logger.info("Saving project: '{}'",project.getName());
			project.setStartDate(LocalDate.now());
			project.setProjectId("66666");
			projectService.save(project);
			server.setProject(project);
		}
		String osName = cols[1];
		server.setOsName(osName);
		var oses = osService.getByName(osName);
		if (oses.size() > 0) {
			server.setOs(oses.get(0));
		}
		server.setName(cols[2]);
		server.setIpAddress(cols[3]);
		if (cols.length > 4) {
			server.setUsername(cols[4]);
			server.setAdminPassword(cols[5]);
		}
		return server;
	}
	
	public List<Server> readNewServers() throws IOException {
		var newServers = new ArrayList<Server>();
		logger.info("Reading new servers");
		try (var br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Hostnames met w-20210106.txt")))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				if (!line.contains("Servername")) {
					var server = convertToNewServer(line);
					logger.info("Read server: {} {} {}", server.getName(), server.getProjectName(), server.getIpAddress());
					newServers.add(server);
				}
			}
		}
		return newServers;
	}

	private Server convertToNewServer(String line) {
		var server = new Server();
		logger.info("Converting line to server: {}", line);
		var cols = line.split("\t");
		var projectName = cols[0];
		server.setProjectName(projectName);
		if (cols.length > 1) {
			handleMoreThanOneColumn(server, cols);
		}
		return server;
	}

	private void handleMoreThanOneColumn(Server server, String[] cols) {
		server.setOsName(cols[1]);
		if (cols.length > 2) {
			handleMoreThanTwoColumns(server, cols);
		}
	}

	private void handleMoreThanTwoColumns(Server server, String[] cols) {
		server.setName(cols[2]);
		if (cols.length > 3) {
			handleMoreThanThreeColumns(server, cols);
		}
	}

	private void handleMoreThanThreeColumns(Server server, String[] cols) {
		server.setIpAddress(cols[3]);
		if (cols.length > 5) {
			handleMoreThanFiveColumns(server, cols);
		}
	}

	private void handleMoreThanFiveColumns(Server server, String[] cols) {
		server.setAdminPassword(cols[5]);
		if (cols.length > 6) {
			handleMoreThanSixColumns(server, cols);
		}
	}

	private void handleMoreThanSixColumns(Server server, String[] cols) {
		server.setUsername(cols[6]);
		if (cols.length > 7) {
			handleMoreThanSevenColumns(server, cols);
		}
	}

	private void handleMoreThanSevenColumns(Server server, String[] cols) {
		server.setRootPassword(cols[7]);
		if (cols.length > 8) {
			handleMoreThanEightColumns(server, cols);
		}
	}

	private void handleMoreThanEightColumns(Server server, String[] cols) {
		server.setUsername2(cols[8]);
		if (cols.length > 9) {
			handleMoreThanNineColumns(server, cols);
		}
	}

	private void handleMoreThanNineColumns(Server server, String[] cols) {
		server.setPassword2(cols[9]);
		if (cols.length > 10) {
			server.setRemark(cols[10]);
		}
	}
	
	public List<Server> readHostnamesMetPw20210706() throws IOException {
		var servers = new ArrayList<Server>();
		try (var in = getClass().getResourceAsStream("/hostnames_pw_20210706.txt" );
				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			var line  = " ";
			var lineCount = 0;
			while ((line = br.readLine()) != null) {
				if (lineCount > 0) {
					var server = makeServer(line);
					servers.add(server);
				}
				lineCount++;
			}
		}
		
		return servers;
	}
	
	private Server makeServer(String line) {
		var server = new Server();
		logger.info("Read line: {}", line);
		var subs = line.split("\t");
		logger.info("Found {} subs", subs.length);
		server.setProjectName(subs[0]);
		server.setOsName(subs[1]);
		server.setName(subs[2]);
		server.setIpAddress(subs[3]);
		if (subs.length > 4) {
			server.setUsername(subs[4]);
			if (subs.length > 5) {
				server.setAdminPassword(subs[5]);
				if (subs.length > 6) {
					server.setUsername2(subs[6]);
					if (subs.length > 7) {
						server.setPassword2(subs[7]);
					}
				}
			}
		}
		return server;
	}
	
	
	private Server convertPipedLine(String line) {
		var server = new Server();
		var subs = line.split("\\|");
		var serverName = subs[4];
		server.setName(serverName);
		var id = Integer.parseInt(subs[0]);
		
		try {
			var  ipAddress = new IpAddressReader().readIpAddress(id);
			server.setIpAddress(ipAddress);
			
			var password = new PasswordReader().readPassword(id);
			server.setAdminPassword(password);
		} catch(IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		
		return server;
	}
}
