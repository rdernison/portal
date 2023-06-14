package nl.uwv.otod.otod_portal.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.uwv.otod.otod_portal.model.Privilege;
import nl.uwv.otod.otod_portal.model.Role;
import nl.uwv.otod.otod_portal.service.PrivilegeService;
import nl.uwv.otod.otod_portal.service.RoleService;

@Component
public class RolePrivilegeInitializer {

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PrivilegeService privilegeService;
	
	public void setUpRolesAndPrivileges() {
		checkAdmin();
		
		checkUser();
		
	}
	
	private void checkAdmin() {
		LOGGER.info("Finding admin role");
		var adminFromDb = roleService.getByName("admin");
		var adminFromDbIt = adminFromDb.iterator();
		if (!adminFromDbIt.hasNext()) {
			LOGGER.info("Admin role not found, creating it");
			var admin = new Role();
			admin.setName("admin");
			roleService.save(admin);
			
			var createUser = new Privilege();
			createUser.setName("create_user");
			privilegeService.save(createUser);
			
			admin.getPrivileges().add(createUser);
			roleService.save(admin);
		}
	}
	
	private void checkUser() {
		LOGGER.info("Checking user role");
		var usersFromDb = roleService.getByName("user");
		var usersFromDbIt = usersFromDb.iterator();
		if (!usersFromDbIt.hasNext()) {
			LOGGER.info("User role not found, creating it");
			var user = new Role();
			user.setName("user");
			roleService.save(user);
			
			var requestEnv = new Privilege();
			requestEnv.setName("request_environment");
			privilegeService.save(requestEnv);
			
			user.getPrivileges().add(requestEnv);
			roleService.save(user);
		}
		
	}
}
