package nl.uwv.otod.otod_portal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.config.RolePrivilegeInitializer;
import nl.uwv.otod.otod_portal.config.UserRoleInitializer;
import nl.uwv.otod.otod_portal.connector.ServerProjectConnector;
import nl.uwv.otod.otod_portal.correction.DuplicateOsRemover;
import nl.uwv.otod.otod_portal.init.PortalInitializer;
import nl.uwv.otod.otod_portal.init.UserInitializer;
import nl.uwv.otod.otod_portal.io.CalculationReader;
import nl.uwv.otod.otod_portal.io.CostCenterReader;
import nl.uwv.otod.otod_portal.io.DiskReader;
import nl.uwv.otod.otod_portal.io.ExcelReader;
import nl.uwv.otod.otod_portal.io.GenericOtodSizingReader;
import nl.uwv.otod.otod_portal.io.MiddlewareReader;
import nl.uwv.otod.otod_portal.io.OsReader;
import nl.uwv.otod.otod_portal.io.ProjectReader;
import nl.uwv.otod.otod_portal.io.RequestReader;
import nl.uwv.otod.otod_portal.io.ServerReader;
import nl.uwv.otod.otod_portal.io.ServerUpdater;
import nl.uwv.otod.otod_portal.model.Disk;
import nl.uwv.otod.otod_portal.model.DuplicateOs;
import nl.uwv.otod.otod_portal.model.FileSystem;
import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.model.Project;
import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.model.SizingRow;
import nl.uwv.otod.otod_portal.model.UsedIpAddress;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.scraper.security.PasswordHasher;
import nl.uwv.otod.otod_portal.service.CalculationService;
import nl.uwv.otod.otod_portal.service.CostCenterService;
import nl.uwv.otod.otod_portal.service.DiskService;
import nl.uwv.otod.otod_portal.service.FileSystemService;
import nl.uwv.otod.otod_portal.service.MiddlewareService;
import nl.uwv.otod.otod_portal.service.OsService;
import nl.uwv.otod.otod_portal.service.ProjectService;
import nl.uwv.otod.otod_portal.service.RequestService;
import nl.uwv.otod.otod_portal.service.ServerService;
import nl.uwv.otod.otod_portal.service.SizingRowService;
import nl.uwv.otod.otod_portal.service.StorageService;
import nl.uwv.otod.otod_portal.service.UsedIpAddressService;
import nl.uwv.otod.otod_portal.service.UserService;
import nl.uwv.otod.otod_portal.service.impl.StorageProperties;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

@SpringBootApplication /*
						 * (scanBasePackages={
						 * "nl.uwv.otod.otod_portal","nl.uwv.otod.otod_portal.service",
						 * "nl.uwv.otod.otod_portal.service.impl"})
						 */
@EnableScheduling
@EnableConfigurationProperties(StorageProperties.class)
@Log4j2
public class OtodPortalApp {
	/*
	 * @Autowired private ProjectService projectService;
	 * 
	 * @Autowired private ServerService serverService;
	 * 
	 * @Autowired private DiskService diskService;
	 * 
	 * @Autowired private OsService osService;
	 * 
	 * @Autowired private RequestService requestService;
	 * 
	 * @Autowired private UserService userService;
	 * 
	 * @Autowired private UsedIpAddressService usedIpAddressService;
	 * 
	 * @Autowired private MiddlewareService middlewareService;
	 * 
	 * private static Logger logger = LogManager.getLogger();
	 * 
	 * @Autowired private ServerProjectConnector serverProjectConnector;
	 * 
	 * @Autowired private CalculationReader calculationReader;
	 * 
	 * @Autowired private CalculationService calculationService;
	 * 
	 * @Autowired private SizingRowService sizingRowService;
	 * 
	 * @Autowired private RolePrivilegeInitializer rolePrivilegeInitializer;
	 * 
	 * @Autowired private UserRoleInitializer userRoleInitializer;
	 * 
	 * @Autowired private ExcelReader serverReader;
	 * 
	 * @Autowired private FileSystemService fileSystemService;
	 * 
	 * @Autowired private DuplicateOsRemover duplicateOsRemover;
	 * 
	 * @Autowired private CostCenterService costCenterService;
	 * 
	 * @Autowired private UserInitializer userInitializer;
	 * 
	 */
	@Autowired
	private PortalInitializer portalInitializer;

	public static void main(String[] args) {
		SpringApplication.run(OtodPortalApp.class, args);
	}

	@Profile("!test")
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx, StorageService storageService) {
		return args -> {
			log.info("=== Initializing portal ===");
			portalInitializer.initializePortal();
		};
	}

	/*
	 * private void checkProjects() { logger.info("-- checking projects --"); var
	 * projectsFromDb = projectService.getAll(); var projectIt =
	 * projectsFromDb.iterator(); if (!projectIt.hasNext()) {
	 * logger.info("--- Initializing projects ---"); var projects = readProjects();
	 * projects .stream() .forEach(p -> projectService.save(p)); } }
	 * 
	 * private List<Project> readProjects() { List<Project> projects = null; var
	 * projectReader = new ProjectReader(); try { projects =
	 * projectReader.readAllProjects(); } catch (IOException e) {
	 * logger.error(e.toString()); } return projects; }
	 * 
	 * private void checkServers() throws IOException {
	 * logger.info("-- checking servers --"); var serversFromDb =
	 * serverService.getAll(); var serverIt = serversFromDb.iterator(); if
	 * (!serverIt.hasNext()) { logger.info("--- No DXC servers found ---");
	 * logger.info("--- Initializing servers ---"); var servers = readDxcServers();
	 * servers .stream() .filter(s ->
	 * !serverService.getByName(s.getName()).isPresent()) .forEach(s ->
	 * serverService.save(s)); }
	 * 
	 * logger.info("Checking IBM servers"); var foundIbmServers = findIbmServers();
	 * if (!foundIbmServers) { logger.info("No IBM servers found"); var ibmServers =
	 * readIbmServers();
	 * 
	 * for (var server : ibmServers) { serverService.save(server); } } }
	 * 
	 * private List<Server> readDxcServers() { logger.info("Reading Dxc servers");
	 * List<Server> servers = null; ServerReader serverReader = new
	 * ServerReader(projectService, osService); try { servers =
	 * serverReader.readDxcServers(); } catch (IOException e) {
	 * logger.error(e.toString()); } logger.info("Read {} Dxc servers",
	 * servers.size()); return servers; }
	 * 
	 * private boolean findIbmServers() { var foundIbmServers = false; var servers =
	 * serverService.getAll(); var serverIt = servers.iterator(); while
	 * (serverIt.hasNext()) { var server = serverIt.next(); var ipAddress =
	 * server.getIpAddress(); if (ipAddress.startsWith("10.178")) { foundIbmServers
	 * = true; break; } } return foundIbmServers; }
	 * 
	 * private List<Server> readIbmServers() throws IOException { var serverReader =
	 * new ServerReader(projectService, osService); var ibmServers =
	 * serverReader.readIbmServers(); return ibmServers; }
	 * 
	 * private void checkDisks() { Iterable<Disk> disksFromDb =
	 * diskService.getAll(); Iterator<Disk> diskIt = disksFromDb.iterator();
	 * logger.info("--- Checking disks ---"); if (!diskIt.hasNext()) { try { new
	 * DiskReader() .readAllDisks() .forEach(d -> writeDisk(d)); } catch
	 * (IOException e) { logger.error(e.toString()); } }
	 * 
	 * }
	 * 
	 * private void writeDisk(Disk disk) { var optServer =
	 * serverService.getByName(disk.getComputerName()); if (optServer.isPresent()) {
	 * var server = optServer.get();
	 * logger.info("Found server, connecting to disk"); disk.setServer(server);
	 * diskService.save(disk); } }
	 * 
	 * private void checkOses() { logger.info("Checking oses"); var oses =
	 * osService.getAll(); var osIt = oses.iterator();
	 * logger.info("Any oses found? {}", osIt.hasNext()); if (!osIt.hasNext()) {
	 * readAndStoreOses(); } }
	 * 
	 * private void readAndStoreOses() { logger.info("Reading and storing os'es");
	 * try { var oses = new OsReader().readOses(); for (var os : oses) {
	 * logger.info("Saving os: {}", os.getName()); osService.save(os); } } catch
	 * (IOException e) { logger.error(e.toString()); } }
	 */
}
