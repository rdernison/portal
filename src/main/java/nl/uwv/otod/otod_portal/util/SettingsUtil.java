package nl.uwv.otod.otod_portal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SettingsUtil {

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
	public static final String CONNECT_SERVERS_WITH_OSES_REQUIRED = "CONNECT_SERVERS_WITH_OSES_REQUIRED";
	public static final String SAVE_HOST_NAMES_WITH_PASSWORD_REQUIRED = "SAVE_HOST_NAMES_WITH_PASSWORD_REQUIRED";
	public static final String CONNECT_REQUESTS_WITH_USERS_REQUIRED = "CONNECT_REQUESTS_WITH_USERS_REQUIRED";
	public static final String READ_NEW_SERVERS_REQUIRED = "READ_NEW_SERVERS_REQUIRED";
	public static final String CONNECT_SERVERS_WITH_PROJECTS_REQUIRED = "CONNECT_SERVERS_WITH_PROJECTS_REQUIRED";
	public static final String READ_CALCULATIONS_REQUIRED = "READ_CALCULATIONS_REQUIRED";
	public static final String INIT_SIZING_ROWS_REQUIRED = "INIT_SIZING_ROWS_REQUIRED";
	public static final String SET_UP_ROLES_AND_PRIVILEGES_REQUIRED = "SET_UP_ROLES_AND_PRIVILEGES_REQUIRED";
	public static final String INIT_USER_ROLES_REQUIRED = "INIT_USER_ROLES_REQUIRED";
	public static final String READ_SERVERS_FROM_EXCEL_REQUIRED = "READ_SERVERS_FROM_EXCEL_REQUIRED";
	public static final String HOSTNAMES_WITH_PASSWORDS_PATH = "HOSTNAMES_WITH_PASSWORDS_PATH";
	public static final String SIZING_DOC_PATH = "SIZING_DOC_PATH";
	public static final String NUMBER_OF_SERVERS_PER_REQUEST = "NUMBER_OF_SERVERS_PER_REQUEST";
	public static final String INITIAL_NUMBER_OF_DISKS_PER_SERVER = "INITIAL_NUMBER_OF_DISKS_PER_SERVER";
	public static final String COST_CENTER_DOC_PATH = "COST_CENTER_DOC_PATH";
	public static final String OLD_PORTAL_SQL_PATH = "OLD_PORTAL_SQL_PATH";
	public static final String ADMIN_USERNAMES = "ADMIN_USERNAMES";
	public static final String INPUT_PATH = "INPUT_PATH";
	
	public static Properties readSettings() throws IOException {
		Properties properties = new Properties();
		try (InputStream inStream = new FileInputStream(new File("settings.properties"))) {
			
			properties.load(inStream);
		}
		
		return properties;
	}
	
	public static String readSetting(String settingName) throws IOException {
		var settings = readSettings();
		return (String)settings.get(settingName);
	}
}
