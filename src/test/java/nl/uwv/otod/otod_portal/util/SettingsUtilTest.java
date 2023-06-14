package nl.uwv.otod.otod_portal.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class SettingsUtilTest {
	public static final String AVAILABLE_IP_ADDRESSES_PATH = "AVAILABLE_IP_ADDRESSES_PATH";
	public static final String FILE_UPLOAD_DIR = "FILE_UPLOAD_DIR";
	public static final String SERVERS_XLS_FILE = "SERVERS_XLS_FILE";
	public static final String CHECK_PROJECTS_REQUIRED = "CHECK_PROJECTS_REQUIRED";
	public static final String CHECK_SERVERS_REQUIRED = "CHECK_SERVERS_REQUIRED";
	public static final String CHECK_DISKS_REQUIRED = "CHECK_DISKS_REQUIRED";
	public static final String CHECK_OSES_REQUIRED = "CHECK_OSES_REQUIRED";
	public static final String CHECK_REQUESTS_REQUIRED = "CHECK_REQUESTS_REQUIRED";
	public static final String UPDATE_SERVERS_REQUIRED = "UPDATE_SERVERS_REQUIRED";
	public static final String INIT_USERS_REQUIRED = "INIT_USERS_REQUIRED";
	public static final String INIT_USED_IP_ADDRESSES_REQUIRED = "INIT_USED_IP_ADDRESSES_REQUIRED";
	public static final String CHECK_MIDDLEWARE_REQUIRED = "CHECK_MIDDLEWARE_REQUIRED";

	@Test
	public void testReadSetting() throws IOException {
		var availableIpAddressesPath = SettingsUtil.readSetting(SettingsUtil.AVAILABLE_IP_ADDRESSES_PATH);
		assertNotNull(availableIpAddressesPath);
		
		var fileUploadPath = SettingsUtil.readSetting(SettingsUtil.FILE_UPLOAD_DIR);
		assertNotNull(fileUploadPath);
		
		var serversXlsFile = SettingsUtil.readSetting(SettingsUtil.SERVERS_XLS_FILE);
		assertNotNull(serversXlsFile);
		
		var checkProjectsRequired = SettingsUtil.readSetting(SettingsUtil.CHECK_PROJECTS_REQUIRED);
		assertNotNull(checkProjectsRequired);
		
		var checkServersRequired = SettingsUtil.readSetting(SettingsUtil.CHECK_SERVERS_REQUIRED);
		assertNotNull(checkServersRequired);
		
		var checkDisksRequired = SettingsUtil.readSetting(SettingsUtil.CHECK_DISKS_REQUIRED);
		assertNotNull(checkDisksRequired);
		
		var checkOsesRequired = SettingsUtil.readSetting(SettingsUtil.CHECK_OSES_REQUIRED);
		assertNotNull(checkOsesRequired);
		
		var checkRequestsRequired = SettingsUtil.readSetting(SettingsUtil.CHECK_REQUESTS_REQUIRED);
		assertNotNull(checkRequestsRequired);
		
		var updateServersRequired = SettingsUtil.readSetting(SettingsUtil.UPDATE_SERVERS_REQUIRED);
		assertNotNull(updateServersRequired);
		
		var initUsersRequired = SettingsUtil.readSetting(SettingsUtil.INIT_USERS_REQUIRED);
		assertNotNull(initUsersRequired);
		
		var initUsedIpAddressesRequired = SettingsUtil.readSetting(SettingsUtil.INIT_USED_IP_ADDRESSES_REQUIRED);
		assertNotNull(initUsedIpAddressesRequired);
		
		var checkMiddlewareRequired = SettingsUtil.readSetting(SettingsUtil.CHECK_MIDDLEWARE_REQUIRED);
		assertNotNull(checkMiddlewareRequired);
		
		var connectRequestsWithUsersRequired = SettingsUtil.readSetting(SettingsUtil.CONNECT_REQUESTS_WITH_USERS_REQUIRED);
		assertNotNull(connectRequestsWithUsersRequired);
		
		var readNewServersRequired = SettingsUtil.readSetting(SettingsUtil.READ_NEW_SERVERS_REQUIRED);
		assertNotNull(readNewServersRequired);
		
		var connectServersWithProjectsRequired = SettingsUtil.readSetting(SettingsUtil.CONNECT_SERVERS_WITH_PROJECTS_REQUIRED);
		assertNotNull(connectServersWithProjectsRequired);
		
		var readCalculationsRequired = SettingsUtil.readSetting(SettingsUtil.READ_CALCULATIONS_REQUIRED);
		assertNotNull(readCalculationsRequired);
		
		var initSisingRowsRequired = SettingsUtil.readSetting(SettingsUtil.INIT_SIZING_ROWS_REQUIRED);
		assertNotNull(initSisingRowsRequired);
		
		var setUpRolesAndPrivilegesRequired = SettingsUtil.readSetting(SettingsUtil.SET_UP_ROLES_AND_PRIVILEGES_REQUIRED);
		assertNotNull(setUpRolesAndPrivilegesRequired);
		
		var initUserRolesRequired = SettingsUtil.readSetting(SettingsUtil.INIT_USER_ROLES_REQUIRED);
		assertNotNull(initUserRolesRequired);
		
		var readServersFromExcelRequired = SettingsUtil.readSetting(SettingsUtil.READ_SERVERS_FROM_EXCEL_REQUIRED);
		assertNotNull(readServersFromExcelRequired);
		
		
	
	}
}
