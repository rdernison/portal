package nl.uwv.otod.otod_portal.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.io.ExcelReader;
import nl.uwv.otod.otod_portal.io.ServerReader;
import nl.uwv.otod.otod_portal.io.ServerUpdater;
import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.service.OsService;
import nl.uwv.otod.otod_portal.service.ProjectService;
import nl.uwv.otod.otod_portal.service.ServerService;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

@Component
@Log4j2
public class ServerInitializer {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private OsService osService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private ExcelReader serverReader;
	

	public void checkServers() throws IOException {
		log.info("-- checking servers --");
		var serversFromDb = serverService.getAll();
		var serverIt = serversFromDb.iterator();
		var dxcServersFound = findDxcServers(serversFromDb);
		if (!serverIt.hasNext() || !dxcServersFound) {
			log.info("--- No DXC servers found ---");
			log.info("--- Initializing servers ---");
			var servers = readDxcServers();
			servers
				.stream()
				.filter(s -> !serverService.getByName(s.getName()).isPresent())
				.forEach(s -> serverService.save(s));
		} else {
			connectServersWithoutProjectToTempDxcProject();
		}
		
		log.info("Checking IBM servers");
		var foundIbmServers = findIbmServers();
		if (!foundIbmServers) {
			log.info("No IBM servers found");
			var ibmServers = readIbmServers();
			
			for (var server : ibmServers) {
				serverService.save(server);
			}
		} else {
//			log.info("Found ibm servers, checking passwords");
			var ibmServersFromFile = readIbmServersFromFile();
			var ibmPasswordsFromFile = readIbmPasswordsFromFile();
			var ibmServers = serverService.getAll();
			for (var ibmServer : ibmServers) {
				if (ibmServer.getAdminPassword() == null || ibmServer.getAdminPassword().length() == 0) {
//					log.info("No password found on {}", ibmServer.getName());
					var ibmServerFromFile = findIbmServer(ibmServer, ibmServersFromFile);
					if (ibmServerFromFile != null) {
						var password = findPassword(ibmServerFromFile, ibmPasswordsFromFile);
						ibmServer.setAdminPassword(password);
						serverService.save(ibmServer);
					}
				}
			}
					
		}
	}
	
	private boolean findDxcServers(Iterable<Server> serversFromDb) {
		var serverIt = serversFromDb.iterator();
		var foundDxcServers = false;
		while (serverIt.hasNext()) {
			var server = serverIt.next();
			if (server.getName().toLowerCase().startsWith("uwv")) {
				foundDxcServers = true;
				break;
			}
		}
		return foundDxcServers;
	}

	private void connectServersWithoutProjectToTempDxcProject() {
		log.info("Connecting servers without project to temp dxc projec");
		var dxcProjects =  projectService.getByName("DXC");
		if (dxcProjects.size() != 0) {
			var theDxcProjet = dxcProjects.get(0);
			var allServers = serverService.getAll();
			log.info("Iterating projecdts");
			for (var server : allServers) {
				if (server.getProject() == null) {
					log.info("Server {} doesn't have project, connecting");
					server.setProject(theDxcProjet);
					serverService.save(server);
				}
			}
		}
		
		
	}

	private List<Map<String, String>> readIbmServersFromFile() throws IOException {
		var servers = new ArrayList<Map<String, String>>();
		var serverLines = Files.readAllLines(Paths.get("c:/users/rde062/documents/oude portal/portal-export/servers-with-status.txt"));
		log.info("Reading server names from file");
		for (String serverLine : serverLines) {
			var serverData =  new HashMap<String, String>();
			var cols = serverLine.split("\t");
			var serverId = cols[0];
			var serverName = cols[4];
			serverData.put("serverId", serverId);
			serverData.put("serverName", serverName);
			servers.add(serverData);
			
		}
		return servers;
	}
	
	private Map<String, String> findIbmServer(Server server, List<Map<String, String>> ibmServersFromFile) {
		var serverName = server.getName();
		Map<String, String> theServerFromFile = null;
//		log.info("Finding ibm server {}", server.getName());
		for (int i = 0; i < ibmServersFromFile.size(); i++) {
			var ibmServerFromFile = ibmServersFromFile.get(i);
			if (ibmServerFromFile.get("serverName").equals(serverName) ) {
				theServerFromFile = ibmServerFromFile;
//				log.info("Found it");
				break;
			}
		}
		
		return theServerFromFile;
	}
	
	private String findPassword(Map<String, String> ibmServerFromFile, List<Map<String, String>> ibmPasswordsFromFile) {
		var password= "";
		log.info("Finding password for {}", ibmServerFromFile.get("serverName"));
		for (var ibmPasswordFromFile : ibmPasswordsFromFile) {
			var serverId = ibmPasswordFromFile.get("serverId");
			if (serverId.equals(ibmServerFromFile.get("serverId"))) {
				password = ibmPasswordFromFile.get("password");
				log.info("Found it");
				break;
			}
		}
		return password;
	}
	
	public List<Map<String, String>> readIbmPasswordsFromFile() throws IOException {
		var serverIdsWithPasswords = new ArrayList<Map<String, String>>();
		// kolommen 3 en 7 
		
		
		List<String> allLines = Files.readAllLines(Paths.get("c:/users/rde062/Documents/oude portal/portal-export/admin-pass.txt"));
		
		for (String line : allLines) {
			var cols = line.split("\t");
			var serverId = cols[3];
			var password = cols[7];
			var serverIdWithPassword = new HashMap<String, String>();
			serverIdWithPassword.put("serverId", serverId);
			serverIdWithPassword.put("password", password);
			
			serverIdsWithPasswords.add(serverIdWithPassword);
		}
		return serverIdsWithPasswords;
	}
	
	private List<Server> readIbmServers() throws IOException {
		var serverReader = new ServerReader(projectService, osService);
		var ibmServers = serverReader.readIbmServers();
		return ibmServers;
	}

	private List<Server> readDxcServers() {
		log.info("Reading Dxc servers");
		List<Server> servers = null;
		ServerReader serverReader = new ServerReader(projectService, osService);
		try {
			servers = serverReader.readDxcServers();
		} catch (IOException e) {
			log.error(e.toString());
		}
		log.info("Read {} Dxc servers", servers.size());
		return servers;
	}
	
	private boolean findIbmServers() {
		var foundIbmServers = false;
		var servers = serverService.getAll();
		var serverIt = servers.iterator();
		while (serverIt.hasNext()) {
			var server = serverIt.next();
			var ipAddress = server.getIpAddress();
			if (ipAddress.startsWith("10.140")) {
				foundIbmServers = true;
				break;
			}
		}
		return foundIbmServers;
	}
	
	public void updateServers() {
		var serverUpdater = new ServerUpdater(serverService, osService);
		serverUpdater.updateServers();
	}
	
	public void connectServersWithOses() {
		log.info("Connecting servers with oses");
		
		var servers = serverService.getAll();
		
		for (var server : servers) {
			var osName = server.getOsName();
			if (osName != null && osName.length() > 0) {
				var oses = osService.getByName(osName);
				log.info("Got os name: {}", osName);
				if (server.getOs() == null) {
					setOsOnServer(server, oses);
				}
			}
		}
	}

	private void setOsOnServer(Server server, List<Os> oses) {
		if (oses.size() > 0) {
			var os = oses.get(0);
			log.info("Got os");
			server.setOs(os);
			serverService.save(server);
		} else {
			log.info("No os found");
		}
	}


	public void saveHostnamesWithPassword() throws IOException {
		log.info("Checking hostnames with passwords");
		var hostnamesWithPPath = SettingsUtil.readSetting(SettingsUtil.HOSTNAMES_WITH_PASSWORDS_PATH);
		var allLines = readAllLinesFromInputStream(getClass().getResourceAsStream(hostnamesWithPPath));
		log.info("Found {} lines in input file", allLines.size());
		var serversFromDb = serverService.getAll();
		
		for (var line : allLines) {
			log.info("Checking line: {}", line);
			if (!line.contains("Applicatienaam")) {
				var subs = line.split("\t");
				var projectName = subs[0];
				var osName = subs[1];
				var serverName = subs[2];
				var ipAddress = subs[3];
				var username = subs.length > 4 ? subs[4] : "";
				var passwd = subs.length > 5 ? subs[5] : "";
				var foundIp = false;
				Server theServer = null;
				for (var serverFromDb : serversFromDb) {
					var ipFromServer = serverFromDb.getIpAddress();
					if (ipFromServer.equals(ipAddress)) {
						log.info("Found ip address, updating admin password");
						foundIp = true;
						theServer = serverFromDb;
						if (theServer.getAdminPassword() == null) {
							theServer.setAdminPassword(passwd);
							serverService.save(theServer);
						}
						break;
					}
				}
				if (!foundIp) {
					log.info("IP-address not found, creating new server");
					var newServer = new Server();
					newServer.setName(serverName);
					var projects = projectService.getByName(projectName);
					if (projects != null && projects.size() > 0) {
						var project = projects.get(0);
						newServer.setProject(project);
					}
					var oses = osService.getByName(osName);
					if (oses != null && oses.size() > 0) {
						var os = oses.get(0);
						newServer.setOs(os);
					}
					newServer.setIpAddress(ipAddress);
					newServer.setAdminPassword(passwd);
					newServer.setUsername(username);
					serverService.save(newServer);
				}
			}
		}
	}
	
	private List<String> readAllLinesFromInputStream(InputStream resourceAsStream) throws IOException {
		var allLines = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				allLines.add(line);
			}
		}
		log.info("Found {} lines in servers file" , allLines.size());
		return allLines;
	}
	
	public void readNewServers() {
		log.info("Reading new servers");
		var serverReader = new ServerReader(projectService, osService);
		try {
			var newServers = serverReader.readNewServers();
			for (var server : newServers) {
				var serversFromService = serverService.getByNameAndOs(server.getName(), server.getOsName());
				log.info("Found server in db? {}", serversFromService.size() > 0);
				if (serversFromService.size() == 0) {
					log.info("No, saving it");
					serverService.save(server);
				} else {
					log.info("Yes, updating it");
					updateServer(server, serversFromService);
				}
			}
			
		} catch (IOException e) {
			log.error(e.toString());
		}
	}

	private void updateServer(Server server, List<Server> serversFromService) {
		var serverFromService = serversFromService.get(0);
		var updateServer = false;
		
		if (serverFromService.getOs() == null) {
			log.info("No os found, setting it");
			updateServer = setOsOnServer(server, serverFromService);
		}
		
		if (serverFromService.getProject() == null) {
			log.info("No project found, setting it");
			updateServer = setProjectOnServer(server, serverFromService, updateServer);
		}
		
		if (updateServer) {
			log.info("Server changed, updating it");
			serverService.save(serverFromService);
		}
	}

	private boolean setOsOnServer(Server server, Server serverFromService) {
		var osName = server.getOsName();
		var updateServer = false;
		if (osName != null) {
			var oses = osService.getByName(osName);
			if (oses.size() == 1) {
				serverFromService.setOsId(oses.get(0).getId());
			}
			updateServer = true;
		}
		return updateServer;
	}

	private boolean setProjectOnServer(Server server, Server serverFromService, boolean updateServer) {
		var projects = projectService.getByName(server.getProjectName());
		if (projects.size() > 0) {
			if (!updateServer) {
				updateServer = true;
			}
			serverFromService.setProject(projects.get(0));
		}
		return updateServer;
	}

	public void readServersFromExcel() {
		try {
			var serversXlsFileName = SettingsUtil.readSetting(SettingsUtil.SERVERS_XLS_FILE);
			var serverFile = new File(serversXlsFileName);
			log.info("Reading servers from file {} {}", serverFile.getName(), serverFile.exists());
			if (serverFile.exists()) {
			var servers = serverReader.readServers(serverFile);
			log.info("-- Read {} servers from excel file", servers.size());
			for (var server : servers) {
				log.info("Storing server {} {}", server.getName(), server.getProjectName());
				setProjectOnServer(server);
				serverService.save(server);
			}
			}
		} catch (InvalidFormatException | IOException e) {
			log.error(e.toString());
		}
		
	}
	
	private void setProjectOnServer(Server server) {
		var projects = projectService.getByName(server.getProjectName());
		var projectIt = projects.iterator();
		log.info("Setting project on server '{}'", server.getProjectName());
		if (projectIt.hasNext()) {
			log.info("Found project in db, setting it");
			var project = projectIt.next();
			server.setProject(project);
		} else if (server.getProjectName() != null) {
			log.info("Found project name on server, creating project");
			var project = new Project();
			var projectName = server.getProjectName();
			if (projectName.length() > 30) {
				projectName = projectName.substring(0, 30);
			}
			project.setName(projectName);
			if (project.getName() == null || project.getName().length() < 2) {
				project.setName("UNK");
			}
			project.setStartDate(LocalDate.of(2006, Month.JUNE, 6));
			project.setProjectId("1998");
			projectService.save(project);
			server.setProject(project);
		} else {
			log.info("Finding project by id 20560");
			var dxcProjectOpt = projectService.getById(20560L);//getByName("DXC-Project");
			log.info("Is project present? {}", dxcProjectOpt.isPresent());
			var dxcProject = dxcProjectOpt.get();//dxcProjects.get(0);
			server.setProject(dxcProject);
		}
		
	}
	
	public void readHostnamesMetPw20210706() {
		log.info("Reading hostnames met pw 20210706");
		var serverReader = new ServerReader(projectService, osService);
		try {
			var servers = serverReader.readHostnamesMetPw20210706();
			
			for (var server : servers) {
//				log.info("Read server: {}", server.getName());
				var serverFromDbOpt = serverService.getByName(server.getName());
				
				if (!serverFromDbOpt.isPresent()) {
					log.info("Server not in db");
					setProjectOnServer(server);
					var os = checkOs(server);
					server.setOs(os);
					serverService.save(server);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Os checkOs(Server server) {
		var osName = server.getOsName();
		var oses = osService.getByName(osName);
		Os os = null;
		if (oses.size() == 0) {
			os = new Os();
			os.setName(osName);
			osService.save(os);
		} else {
			os = oses.get(0);
		}
		
		return os;
	}

}
