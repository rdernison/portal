package nl.uwv.otod.otod_portal.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import org.junit.Test;

import nl.uwv.otod.otod_portal.scraper.security.PasswordHasher;

public class PasswordHasherTest {

	@Test
	public void testHashPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
		var password = "Welkom01";
		var hasher = new PasswordHasher();
		var salt = hasher.createSalt();
		var hash = hasher.hash(password, salt);
		var saltAsString = new String(salt); 
		System.out.println(hash);
	}
	
	@Test
	public void testEncryptPassword() {
		var encoder = new BCryptPasswordEncoder();
		
		var passIn = "Goodbye, cruel world";
		var passOut1 = encoder.encode(passIn);
		var passOut2 = encoder.encode(passIn);
		
		assertEquals(passOut1, passOut2);
	}
}
