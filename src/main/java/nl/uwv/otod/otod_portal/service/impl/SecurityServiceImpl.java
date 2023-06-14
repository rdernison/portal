package nl.uwv.otod.otod_portal.service.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.PasswordResetToken;
import nl.uwv.otod.otod_portal.persistence.PasswordTokenRepository;
import nl.uwv.otod.otod_portal.service.SecurityService;

@Service
public class SecurityServiceImpl implements SecurityService {
	@Autowired
	private PasswordTokenRepository passwordTokenRepository;
	
	public String validatePasswordResetToken(String token) {
	    final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

	    return !isTokenFound(passToken) ? "invalidToken"
	            : isTokenExpired(passToken) ? "expired"
	            : null;
	}

	private boolean isTokenFound(PasswordResetToken passToken) {
	    return passToken != null;
	}

	private boolean isTokenExpired(PasswordResetToken passToken) {
	    final Calendar cal = Calendar.getInstance();
	    return passToken.getExpiryDate().before(cal.getTime());
	}
	
	
}
