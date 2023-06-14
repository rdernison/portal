package nl.uwv.otod.otod_portal.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.exception.UserNotFoundException;
import nl.uwv.otod.otod_portal.model.PasswordResetToken;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.persistence.PasswordTokenRepository;
import nl.uwv.otod.otod_portal.persistence.UserDao;
import nl.uwv.otod.otod_portal.service.UserService;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordTokenRepository passwordTokenRepository;
	
	@Override
	public Iterable<User> getAll() {
		return userDao.findAll();
	}

	@Override
	public Optional<User> getByUsername(String username) {
		return userDao.findByUsernameIgnoreCase(username);
	}

	@Override
	public void save(User user) {
		userDao.save(user);
	}

	@Override
	public Iterable<User> getAdmins() {
		var admins = userDao.findAdmins();
		for (var admin : admins) {
			log.info("Found admin {} {} {}", admin.getId(), admin.getUsername(), admin.getFullName());
		}
		return admins;
	}

	@Override
	public void deleteAll() {
		userDao.deleteAll();
	}

	@Override
	public User findUserByEmail(String email) {
		User user = null;
		
		var users = userDao.findByEmail(email);
		if (users.size()  > 0) {
			user = users.get(0);
		}
		return user;
	}
	
	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
	    PasswordResetToken myToken = new PasswordResetToken(token, user);
	    passwordTokenRepository.save(myToken);
	}

	@Override
	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
User user = null;
		
		var users = userDao.findByEmail(email);
		if (users.size()  > 0) {
			user = users.get(0);
		}
		if (user != null) {
	            user.setResetPasswordToken(token);
	            userDao.save(user);
	        } else {
	            throw new UserNotFoundException("Could not find any user with the email " + email);
	        }	}

	@Override
	public User getByResetPasswordToken(String token) {
		// TODO Auto-generated method stub
		return userDao.findByResetPasswordToken(token);
	}

	@Override
	public void updatePassword(User user, String newPassword) {
		var passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
         
        user.setResetPasswordToken(null);
        userDao.save(user);
	}
}
