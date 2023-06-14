package nl.uwv.otod.otod_portal.security.listeners;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.security.authentication.event.AuthenticationFailureExpiredEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.LoginFailure;
import nl.uwv.otod.otod_portal.persistence.LoginFailureRepository;
import nl.uwv.otod.otod_portal.persistence.UserDao;

@Component
@Log4j2
public class AuthenticationFailureListener {

	@Autowired
	private LoginFailureRepository loginFailureRepository;
	
	@Autowired
	private UserDao userDao;
	
	@EventListener
	public void listen(AuthenticationFailureBadCredentialsEvent event) {
		log.info("Login failed");
		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)event.getSource();
			LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();
			
			if (token.getPrincipal() instanceof String) {
				log.info("Attempted username: {}", token.getPrincipal());
				builder.username((String)token.getPrincipal());
				userDao.findByUsernameIgnoreCase((String)token.getPrincipal()).ifPresent(builder::user);
			}
			
			if (token.getDetails() instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails)token.getDetails();
				log.info("IP address: {}", details.getRemoteAddress());
				
				builder.sourceIp(details.getRemoteAddress());
			}
			
			LoginFailure failure = loginFailureRepository.save(builder.build());
			
			log.info("Saved failed login: {}", failure.getId());
			
			if (failure.getUser() != null) {
				lockUserAccount(failure.getUser());
			}
		}
	}
	
	private void lockUserAccount(nl.uwv.otod.otod_portal.model.User user) {
		log.info("Counting today's login failures");
		List<LoginFailure> failures = loginFailureRepository.findAllByUserAndCreatedDateIsAfter(user, Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		log.info("Found {}", failures.size());
		if (failures.size() > 3) {
			log.debug("Locking user account");
			user.setAccountNonLocked(false);
			userDao.save(user);
		}
	}

	@EventListener
	public void listen(AuthenticationFailureLockedEvent event) {
		log.info("User login failure: account locked");

		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)event.getSource();
			
			if (token.getPrincipal() instanceof User) {
				User user = (User)token.getPrincipal();
				log.info("User logged in: {}", user.getUsername());
			}
			
			if (token.getDetails()instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails)token.getDetails();
				
				log.info("Source ip = {}", details.getRemoteAddress());
			}
		}
	}

	@EventListener
	public void listen(AuthenticationFailureDisabledEvent event) {
		log.info("User login failure: account disabled");

		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)event.getSource();
			
			if (token.getPrincipal() instanceof User) {
				User user = (User)token.getPrincipal();
				log.info("User logged in: {}", user.getUsername());
			}
			
			if (token.getDetails()instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails)token.getDetails();
				
				log.info("Source ip = {}", details.getRemoteAddress());
			}
		}
	}

	@EventListener
	public void listen(AuthenticationFailureExpiredEvent event) {
		log.info("User login failure: account expired");

		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)event.getSource();
			
			if (token.getPrincipal() instanceof User) {
				User user = (User)token.getPrincipal();
				log.info("User logged in: {}", user.getUsername());
			}
			
			if (token.getDetails()instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails)token.getDetails();
				
				log.info("Source ip = {}", details.getRemoteAddress());
			}
		}
	}
}
