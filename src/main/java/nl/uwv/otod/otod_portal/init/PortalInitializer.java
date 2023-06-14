package nl.uwv.otod.otod_portal.init;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.config.RolePrivilegeInitializer;
import nl.uwv.otod.otod_portal.config.UserRoleInitializer;
import nl.uwv.otod.otod_portal.connector.ServerProjectConnector;
import nl.uwv.otod.otod_portal.correction.DuplicateOsRemover;
import nl.uwv.otod.otod_portal.model.FileSystem;
import nl.uwv.otod.otod_portal.service.FileSystemService;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

@Component
@Log4j2
public class PortalInitializer {
	@Autowired
	private DuplicateOsRemover duplicateOsRemover;

	@Autowired
	private FileSystemService fileSystemService;

	@Autowired
	private ProjectInitializer projectInitializer;

	@Autowired
	private ServerInitializer serverInitializer;

	@Autowired
	private DiskInitializer diskInitializer;

	@Autowired
	private OsInitializer osInitializer;

	@Autowired
	private RequestInitializer requestInitializer;

	@Autowired
	private UserInitializer userInitializer;

	@Autowired
	private UsedIpAddressInitializer usedIpAddressInitializer;

	@Autowired
	private MiddlewareInitializer middlewareInitializer;

	@Autowired
	private ServerProjectConnector serverProjectConnector;

	@Autowired
	private CalculationInitializer calculationInitializer;

	@Autowired
	private SizingRowInitializer sizingRowInitializer;

	@Autowired
	private RolePrivilegeInitializer rolePrivilegeInitializer;

	@Autowired
	private UserRoleInitializer userRoleInitializer;

	@Autowired
	private CostCenterInitilizer costCenterInitializer;
	
	@Autowired
	private DuplicateRequestRemover duplicateRequestRemover;

	public void initializePortal() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

		importFileSystems();

		duplicateOsRemover.removeDuplicateOses();

		var checkProjectsRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.CHECK_PROJECTS_REQUIRED));
		if (checkProjectsRequired) {
			projectInitializer.checkProjects();
		}

		var checkServersRequired = Boolean.parseBoolean(SettingsUtil.readSetting(SettingsUtil.CHECK_SERVERS_REQUIRED));
		if (checkServersRequired) {
			serverInitializer.checkServers();
		}

		var checkDisksRequired = Boolean.parseBoolean(SettingsUtil.readSetting(SettingsUtil.CHECK_DISKS_REQUIRED));
		if (checkDisksRequired) {
			diskInitializer.checkDisks();
		}

		var checkOsesRequired = Boolean.parseBoolean(SettingsUtil.readSetting(SettingsUtil.CHECK_OSES_REQUIRED));
		if (checkOsesRequired) {
			osInitializer.checkOses();
		}

		var checkRequestsRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.CHECK_REQUESTS_REQUIRED));
		if (checkRequestsRequired) {
			requestInitializer.checkRequests();
		}

		var updateServersRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.UPDATE_SERVERS_REQUIRED));
		if (updateServersRequired) {
			serverInitializer.updateServers();
		}

		var initUsersRequired = Boolean.parseBoolean(SettingsUtil.readSetting(SettingsUtil.INIT_USERS_REQUIRED));
		if (initUsersRequired) {
			userInitializer.initUsers();
		}

		var initUsedIpAddressesRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.INIT_USED_IP_ADDRESSES_REQUIRED));
		if (initUsedIpAddressesRequired) {
			usedIpAddressInitializer.initUsedIpAddresses();
		}

		var connectServersWithOsesRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.CONNECT_SERVERS_WITH_OSES_REQUIRED));
		if (connectServersWithOsesRequired) {
			serverInitializer.connectServersWithOses();
		}

		var saveHostnamesWithPasswordsRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.SAVE_HOST_NAMES_WITH_PASSWORD_REQUIRED));
		if (saveHostnamesWithPasswordsRequired) {
			serverInitializer.saveHostnamesWithPassword();
		}

		var checkMiddlewareRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.CHECK_MIDDLEWARE_REQUIRED));
		if (checkMiddlewareRequired) {
			middlewareInitializer.checkMiddleware();
		}

		var connectRequestsWithUsersRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.CONNECT_REQUESTS_WITH_USERS_REQUIRED));
		if (connectRequestsWithUsersRequired) {
			requestInitializer.connectRequestsWithUsers();
		}

		var readNewServersRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.READ_NEW_SERVERS_REQUIRED));
		if (readNewServersRequired) {
			serverInitializer.readNewServers();
		}

		serverInitializer.readHostnamesMetPw20210706();

		var connectServersWithProjectsRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.CONNECT_SERVERS_WITH_PROJECTS_REQUIRED));
		if (connectServersWithProjectsRequired) {
			serverProjectConnector.connectServersWithProjects();
		}

		var readCalculationsRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.READ_CALCULATIONS_REQUIRED));
		if (readCalculationsRequired) {
			calculationInitializer.readCalculations();
		}

		var initSizingRowsRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.INIT_SIZING_ROWS_REQUIRED));
		if (initSizingRowsRequired) {
			sizingRowInitializer.initSizingRows();
		}

		var setUpRolePrivilegeRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.SET_UP_ROLES_AND_PRIVILEGES_REQUIRED));
		if (setUpRolePrivilegeRequired) {
			rolePrivilegeInitializer.setUpRolesAndPrivileges();
		}

		var initUserRolesRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.INIT_USER_ROLES_REQUIRED));
		if (initUserRolesRequired) {
			userRoleInitializer.connectUsersAndRoles();
		}

		var readServersFromExcelRequired = Boolean
				.parseBoolean(SettingsUtil.readSetting(SettingsUtil.READ_SERVERS_FROM_EXCEL_REQUIRED));
		if (readServersFromExcelRequired) {
			serverInitializer.readServersFromExcel();
		}

//		osInitializer.disableCrapOs();

		costCenterInitializer.initCostCenters();

		duplicateRequestRemover.removeDuplicateRequests();
		log.info("=== Initialization ready");

	}

	private void importFileSystems() {
		String[] fileSystemNames = { "ntfs", "jfs2", "ext3" };
		log.info("Checking file systems");
		var fileSystems = fileSystemService.getAll();
		if (!fileSystems.iterator().hasNext()) {
			log.info("None found, saving new");
			for (var fileSystemName : fileSystemNames) {
				var fileSystem = new FileSystem();
				fileSystem.setName(fileSystemName);
				fileSystemService.save(fileSystem);
			}
		}
	}

}
