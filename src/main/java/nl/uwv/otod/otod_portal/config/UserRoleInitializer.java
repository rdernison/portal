package nl.uwv.otod.otod_portal.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.uwv.otod.otod_portal.service.RoleService;
import nl.uwv.otod.otod_portal.service.UserService;

@Component
public class UserRoleInitializer {

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	public void connectUsersAndRoles() {
		var users = userService.getAll();
		
		var adminUsernames = Arrays.asList(new String[] {"fvo013", 
				"jbe074", 
				"jja042", 
				"pri045",
				"rde062", 
				"rko116",
				"rsw009",
				"sri075"});
		var userRoles = roleService.getByName("user");
		var userRole = userRoles.iterator().next();
		var adminRoles = roleService.getByName("admin");
		var adminRole = adminRoles.iterator().next();

		for (var user : users) {
//			LOGGER.info("Found user: {}", user.getUsername());
			if (user.getRoles().size() == 0) {
//				LOGGER.info("User has no roles yet");
				var username = user.getUsername();
				if (adminUsernames.contains(username)) {
					LOGGER.info("Setting admin role for user {}", user.getUsername());
					user.getRoles().add(adminRole);
				}
//				LOGGER.info("Setting user role for user {}",user.getUsername());
				user.getRoles().add(userRole);
				userService.save(user);
			}
			
		}
	}
}
