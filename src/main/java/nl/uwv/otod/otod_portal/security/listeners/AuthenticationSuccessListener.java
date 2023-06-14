package nl.uwv.otod.otod_portal.security.listeners;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.LoginSuccess;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.persistence.LoginSuccessRepository;
import nl.uwv.otod.otod_portal.service.UserService;

@Component
@Log4j2
@RequiredArgsConstructor
public class AuthenticationSuccessListener {
	
	@Autowired
	private UserService userService;

	private final LoginSuccessRepository loginSuccessRepository;
	
	
	@EventListener
	public void listen(AuthenticationSuccessEvent event) {
		LoginSuccess.LoginSuccessBuilder builder = LoginSuccess.builder();
		log.info("User logged in okay");
		
		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)event.getSource();
			log.info("Principal: {}" , token.getPrincipal());
			if (token.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
				org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User)token.getPrincipal();
				Optional<User>userOpt = userService.getByUsername(springUser.getUsername().toLowerCase());
				if (userOpt.isPresent()) {
					User user = userOpt.get();//(User)token.getPrincipal();
					builder.user(user);
					log.info("User logged in: {}", user.getUsername());
				}
			}
			
			if (token.getDetails() instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails)token.getDetails();
				
				log.info("Source ip = {}", details.getRemoteAddress());
				builder.sourceIp(details.getRemoteAddress());
			}
			
			LoginSuccess loginSuccess = loginSuccessRepository.save(builder.build());
			
			log.info("Login success saved. Id = {}" , loginSuccess.getId());
		}
	}
	
}
