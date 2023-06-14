package nl.uwv.otod.otod_portal.service;

import nl.uwv.otod.otod_portal.model.PasswordResetToken;

public interface SecurityService {
	public String validatePasswordResetToken(String token);
	
}
