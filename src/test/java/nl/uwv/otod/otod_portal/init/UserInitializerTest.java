package nl.uwv.otod.otod_portal.init;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.scraper.security.PasswordHasher;

public class UserInitializerTest {

	@Test
	public void testInitUser() throws NoSuchAlgorithmException, InvalidKeySpecException {
		var passwordHasher = new PasswordHasher();
		var passwordEncoder = new BCryptPasswordEncoder();
		
		var salt = passwordHasher.createSalt(); 
		var password = passwordEncoder.encode("Welkom01");
		var user = User.builder().username("uwv001").password(new String(password)).build();
		
		var hashedPassword = user.getPassword();
		assertEquals("$2a$10$QWs0LD7E1egpE5gb883Lw.uM9OE2ssehnmPzf/lj310WifEHAFkhi", hashedPassword);
	}
	
}
