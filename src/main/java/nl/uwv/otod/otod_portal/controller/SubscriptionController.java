package nl.uwv.otod.otod_portal.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.Role;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.scraper.security.PasswordHasher;
import nl.uwv.otod.otod_portal.service.RoleService;
import nl.uwv.otod.otod_portal.service.UserService;

@Controller
@RequestMapping("/subscription")
@Log4j2
public class SubscriptionController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	public static final String SUBSCRIBE = "subscribe";
	
	@RequestMapping("")
	public String prepare(Model model) {
		log.info("Preparing subscription");
		var user = new User();
		model.addAttribute("user", user);
		return SUBSCRIBE;
	}

	@RequestMapping("/subscribe")
	public String subscribe(/*@Valid*/ @ModelAttribute User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
		log.info("Subscribing username = {}, password1 = {}, password2 ={}", user.getUsername(), user.getPassword(),user.getPassword2());
		
		var result = "/login";
		
		if (!validate(user)) {
			log.info("Passwords don't match");
			result = SUBSCRIBE;
		} else {
			log.info("Passwords match, saving user");
			var roles = roleService.getByName("user");
			var passwordHasher = new PasswordHasher();
			var salt = passwordHasher.createSalt();
			log.info("Created salt: {}", new String(salt));
			var hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());//passwordHasher.hash(user.getPassword(), salt);
			log.info("Setting password: {}", user.getPassword());
			user.setHashedPassword(hashedPassword.getBytes());
			user.setPassword(new String(hashedPassword));
			user.setSalt(salt);
			var rolesAsSet = convertRolesToSet(roles);
			user.setRoles(rolesAsSet);
			userService.save(user);
			log.info("Saved user");
		}
		return result;
	}
	
	private boolean validate(User user) {
		boolean valid = true;
		log.info("Validating user");
		var userFromDb = userService.getByUsername(user.getUsername());
		if (userFromDb.isPresent()) {
			log.info("User already in db");
			valid = false;
		}
		if (!user.getPassword().equals(user.getPassword2())) {
			log.info("Passwords {} and {} don't match", user.getPassword(), user.getPassword2());
			valid = false;
		}
		if (user.getUsername().length() != 6) {
			log.info("Username {} invalid", user.getUsername());
			valid = false;
		}
		var emailAddress = user.getEmailAddress();
		if (!emailAddress.contains(".") || !emailAddress.contains("@")) {
			log.info("Email {} invalid", user.getEmailAddress());
			valid = false;
		}
		log.info("Valid {}", valid);
		return valid;
	}
	
	private Set<Role> convertRolesToSet(Iterable<Role> roles) {
		var rolesIt = roles.iterator();
		var rolesSet = new HashSet<Role>();
		while (rolesIt.hasNext()) {
			var role = rolesIt.next();
			rolesSet.add(role);
		}
		
		return rolesSet;
	}
}
