package nl.uwv.otod.otod_portal.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import nl.uwv.otod.otod_portal.exception.UserNotFoundException;
import nl.uwv.otod.otod_portal.model.GenericResponse;
import nl.uwv.otod.otod_portal.model.Role;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.scraper.security.PasswordHasher;
import nl.uwv.otod.otod_portal.service.RoleService;
import nl.uwv.otod.otod_portal.service.SecurityService;
import nl.uwv.otod.otod_portal.service.UserService;
import nl.uwv.otod.otod_portal.service.impl.UserDetailsImpl;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger LOGGER = LogManager.getLogger();

	private static final String ADMIN_USERS = "adminUsers";

	private static final String EDIT_USER = "editUser";

	private static final String RESET_PASSWORD = "resetPassword";

	private static final String FORGOT_PASSWORD = "forgotPassword";

	private static final String UPDATE_PASSWORD = "updatePassword";
	
	private static final String PASSWORD_RESET_SUCCESS = "passwordResetSuccess";
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private SecurityService securityService;

	@RequestMapping("admin")
	public String adminUsers(Model model) {
		LOGGER.info("Administring users");
		model.addAttribute("users", userService.getAll());
		return ADMIN_USERS;
	}

	@RequestMapping("edit/{username}")
	public String editUser(Model model, @PathVariable String username) throws IOException {
		LOGGER.info("Editing user: {}", username);
		var userOpt = userService.getByUsername(username);
		if (userOpt.isPresent()) {
			var user = userOpt.get();
			var roles = isAdmin(user) ? initAdminRoleNames() : initUserRoleNames();
			model.addAttribute("user", user);
			model.addAttribute("roles", roles);
		}
		return EDIT_USER;
	}

	private boolean isAdmin(User user) throws IOException {
		var adminUsernamesStr = SettingsUtil.readSetting(SettingsUtil.ADMIN_USERNAMES);
		var adminUsernames = adminUsernamesStr.split(",");
		var isAdmin = false;
		for (var adminUsername : adminUsernames) {
			if (adminUsername.equals(user.getUsername())) {
				isAdmin = true;
				break;
			}
		}
		return isAdmin;
	}

	private List<String> initAdminRoleNames() {
		var roles = Arrays.asList("user", "admin");
		return roles;
	}

	private List<String> initUserRoleNames() {
		var roles = Arrays.asList("user");
		return roles;
	}

	@RequestMapping("new")
	public String newUser(Model model) {
		LOGGER.info("Creating new user");
		model.addAttribute("user", new User());
		var roles = initUserRoleNames();
		model.addAttribute("roles", roles);
		return EDIT_USER;
	}

	@RequestMapping("save")
	public String saveUser(@ModelAttribute User user, Model model) {
		LOGGER.info("Saving user");
		LOGGER.info("User {} {} {}", user, user.getUsername(), user.getEmailAddress());

		try {
			setRoles(model, user);
			setPassword(model, user);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			throw new RuntimeException("Error hashing password", e);
		}
		userService.save(user);
		LOGGER.info("Setting user attribute null");
		model.addAttribute("user", null);
		model.addAttribute("users", userService.getAll());
		LOGGER.info("Returning to overview");
		return ADMIN_USERS;
	}

	private void setRoles(Model model, User user) throws IOException {
		var roleNames = isAdmin(user) ? initAdminRoleNames() : initUserRoleNames();
		var roles = new HashSet<Role>();

		for (var roleName : roleNames) {
			var roleIt = roleService.getByName(roleName).iterator();
			if (roleIt.hasNext()) {
				var role = roleIt.next();
				roles.add(role);
			}
		}
		user.setRoles(roles);

	}

	private void setPassword(Model model, User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var pwd1 = user.getPassword1();// (String) model.getAttribute("password1");
		var pwd2 = user.getPassword2();// (String) model.getAttribute("password2");
		LOGGER.info("Saving user, pw1 = {}, pw2 = {}", pwd1, pwd2);
		var hasher = new BCryptPasswordEncoder();
		if (!pwd1.equals(pwd2)) {
			throw new RuntimeException("Passwords do not match");
		}
		var hashedPassword = hasher.encode(pwd1);// hash(pwd1, salt);
		user.setPassword(hashedPassword);

	}

	@RequestMapping("resetPassword")
	public String showResetPasswordForm(Model model, User user, String oldPassword, String password1,
			String password2) {
		LOGGER.info("Preparing password reset");
		var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LOGGER.info("Principal = {}", principal);
		LOGGER.info("Preparing password reset for {}", user.getUsername());
		var userFromPrincipal = (UserDetailsImpl) principal;
		LOGGER.info("User from principal = {}", userFromPrincipal);
		var username = userFromPrincipal.getUsername();
		LOGGER.info("username = {}", username);
		var userOpt = userService.getByUsername(username);
		LOGGER.info("User = {}", user);
		User user2 = null;

		byte[] hashedPassword = null;
		if (userOpt.isPresent()) {
			LOGGER.info("User from db present");
			user2 = userOpt.get();
			hashedPassword = user2.getHashedPassword();
		}

		model.addAttribute("oldPassword", ""/* hashedPassword */);
		model.addAttribute("username", username);
		model.addAttribute("password1", password1);
		model.addAttribute("password2", password2);
		model.addAttribute("user", user2);
		model.addAttribute("oldPassword", "");
		LOGGER.info("Showing reset form");
		return RESET_PASSWORD;
	}

	@PostMapping("updatePassword")
	public String updatePassword(Model model, User user) {
		LOGGER.info("Updating passwod - id = {}", user.getId());
		LOGGER.info("Updating passwod - un = {}", user.getUsername());
		LOGGER.info("Updating passwod - pw = {}", user.getPassword());
		LOGGER.info("Updating passwod - pw1 = {}", user.getPassword1());
		LOGGER.info("Updating passwod - pw2 = {}", user.getPassword2());
		var oldPasswordFromForm = (String) model.getAttribute("oldPassword");
		LOGGER.info("Updating passwod - oldPassword = {}", oldPasswordFromForm);
		var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		var userFromModel = (User) model.getAttribute("user");
		var oldPasswordFromModel = userFromModel.getOldPassword();
		var password1FromModel = userFromModel.getPassword1();
		var password2FromModel = userFromModel.getPassword2();
		LOGGER.info("Principal = {}", principal);
		LOGGER.info("Preparing password reset for {}", user.getUsername());
		var userFromPrincipal = (UserDetailsImpl) principal;
		var usernameFromPrincipal = userFromPrincipal.getUsername();
		LOGGER.info("Username from principal: {}", usernameFromPrincipal);
		LOGGER.info("User on model: {}", userFromModel);
		if (userFromModel != null) {
			LOGGER.info("User on model is present: {}", userFromModel.getUsername());
		}
		LOGGER.info("Updating user: {}", user);
		LOGGER.info("Updating user: old password: {}", oldPasswordFromModel);
		LOGGER.info("user.oldPassword: {}", user.getPassword());
//		LOGGER.info("Updating user: password1: {}", password1);
//		LOGGER.info("Updating user: password2: {}", password2);
		var userFromDbOpt = userService.getByUsername(usernameFromPrincipal);
		var returnPath = PASSWORD_RESET_SUCCESS;
		if (userFromDbOpt.isPresent()) {
			LOGGER.info("Found user in db");
			var userFromDb = userFromDbOpt.get();
			var salt = userFromDb.getSalt();
			var encoder = new BCryptPasswordEncoder();
//			var oldPasswordHashed = encoder.encode(oldPasswordFromModel);
//			LOGGER.info("Old hashed password: {}", oldPasswordHashed);
			var userPassword = userFromDb.getPassword();
			LOGGER.info("User password: {}", userPassword);
//			var password1 = user.getPassword1();
//			var password1Hashed = encoder.encode(password1);
//			if (oldPasswordHashed.equals(userPassword)) {
				LOGGER.info("Setting password 1 {} and 2 {}", password1FromModel, password2FromModel);
				userFromDb.setPassword1(password1FromModel);
				userFromDb.setPassword2(password2FromModel);
				try {
					setPassword(model, userFromDb);
					userService.save(userFromDb);
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					LOGGER.error(e.toString());
					returnPath = "/resetPassword";
				}/*
			} else {
				returnPath = "/resetPassword";
			}*/
		}
		return returnPath;
	}

	// zie:
	// https://www.baeldung.com/spring-security-registration-i-forgot-my-password
	// en https://www.codejava.net/frameworks/spring-boot/spring-security-forgot-password-tutorial
	@RequestMapping("/prepareResetForgotPassword")
	public String prepareResetForgottenPassword() {
		LOGGER.info("Preparing forgot password reset");
		return FORGOT_PASSWORD;
	}
/*
	@RequestMapping("/resetForgotPassword")
	public String resetForgottenPassword() {
		LOGGER.info("Resetting forgot password");
		return "";
	}

	
	@PostMapping("/resetPassword2")
	public GenericResponse resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
		LOGGER.info("Sending mail to reset password");
		MailSender mailSender = new JavaMailSenderImpl();
		User user = userService.findUserByEmail(userEmail);
		if (user == null) {
			throw new UserNotFoundException();
		}
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
		return new GenericResponse(/*
									 * messages.getMessage("message.resetPasswordEmail", null, request.getLocale())
									 * /"Reset wachtwoord");
	}

	private String getAppUrl(HttpServletRequest request) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

	}

	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
		String url = contextPath + "/user/changePassword?token=" + token;
		// TODO
		String message = "Reset password"; // messages.getMessage("message.resetPassword", null, locale);
		return constructEmail("Reset Password", message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmailAddress());
		email.setFrom(/* env.getProperty("support.email") * /"ict.tsctotem@uwv.nl");
		return email;
	}
	
	@GetMapping("/user/changePassword")
	public String showChangePasswordPage(Locale locale, Model model, 
	  @RequestParam("token") String token) {
	    String result = securityService.validatePasswordResetToken(token);
	    if(result != null) {
	        String message = result;//messages.getMessage("auth.message." + result, null, locale);
	        return "redirect:
";
	    } else {
	        model.addAttribute("token", token);
	        return "redirect:/user/updatePassword";
	    }
	}
	*/
}
