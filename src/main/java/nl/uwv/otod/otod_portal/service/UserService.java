package nl.uwv.otod.otod_portal.service;

import java.util.Optional;

import nl.uwv.otod.otod_portal.exception.UserNotFoundException;
import nl.uwv.otod.otod_portal.model.User;

public interface UserService {

	public Optional<User> getByUsername(String username);
	
	public Iterable<User> getAll();
	
	public void save(User user);
	
	public Iterable<User> getAdmins();
	
	public void deleteAll();
	
	public User findUserByEmail(String email);
	
	public void createPasswordResetTokenForUser(User user, String token);
	
	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException;
	    
	public User getByResetPasswordToken(String token);
	
	public void updatePassword(User user, String newPassword);
	    
}
