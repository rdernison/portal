package nl.uwv.otod.otod_portal.init;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.scraper.security.PasswordHasher;
import nl.uwv.otod.otod_portal.service.UserService;

@Component
@Log4j2
public class UserInitializer {
	
	@Autowired
	private UserService userService;
	
	public void initUsers() throws NoSuchAlgorithmException, InvalidKeySpecException {
		var usersFromDb = userService.getAll();
		var passwordHasher = new PasswordHasher();
		var usersIt = usersFromDb.iterator();
		var passwordEncoder = new BCryptPasswordEncoder();
		if (!usersIt.hasNext()) {
			var frankie = initFrankie(passwordHasher);
			var jochem = initJochem(passwordHasher);
			var jur = initJur(passwordHasher);
			var praveen = initPraveen(passwordHasher);
			var robert = intitRobert(passwordHasher);
			var roderik = initRoderik(passwordHasher);
			var roel = initRoel(passwordHasher);
			
			userService.save(frankie);
			userService.save(jochem);
			userService.save(jur);
			userService.save(praveen);
			userService.save(robert);
			userService.save(roderik);
			userService.save(roel);
		
		} else {
			while (usersIt.hasNext()) {
				var user = usersIt.next();
				var pwd = user.getPassword();
				if (pwd == null || pwd.equals("Welkom01") || pwd.length() == 0) {
					user.setPassword(passwordEncoder.encode("Welkom01"));
					userService.save(user);
				} else if (user.getHashedPassword() == null) {
					log.info("Updating user {} {}", user.getUsername(), user.getEmailAddress());
					var salt = user.getSalt();
					if (salt == null) {
						salt = passwordHasher.createSalt();
					}
					var pwd0 = user.getPassword();
					var password = pwd0 == null ? "Welkom01" : new String(pwd);
					var hashedPassword = passwordHasher.hash(password, salt);
					if (pwd0 == null) {
						user.setPassword(password/*.getBytes()*/);
					}
					user.setHashedPassword(hashedPassword);
					user.setSalt(salt);
					userService.save(user);
				}
			}
			
		}	

		
		var sietoOpt = userService.getByUsername("sri075");
		if (!sietoOpt.isPresent()) {
			log.info("Sieto not found, adding him");
			var sieto = initSieto(passwordHasher);
			userService.save(sieto);
			
		}
		
		/* don't update test users */
		var uwvUserOpt = userService.getByUsername("uwv001");
		if (!uwvUserOpt.isPresent()) {
			var uwvUser = initUwvUser(passwordHasher);
			userService.save(uwvUser);
		} else {
			log.info("User 1 present, updating");
			var user1 = uwvUserOpt.get();
			var salt = passwordHasher.createSalt();
			var pwd1FromAstrid = "pYQ+h,nKYxWN";
			var hashedPassword = passwordEncoder.encode("HatseFlatse00!"/*, salt*/);
			user1.setPassword(new String(hashedPassword));
			user1.setSalt(salt);
			userService.save(user1);
		}
		
		var uwvUser2Opt = userService.getByUsername("uwv002");
		log.info("Checking user 2");
		if (!uwvUser2Opt.isPresent()) {
			log.info("User 2 not present, creating");
			var uwvUser2 = initUwvUser2(passwordHasher);
			userService.save(uwvUser2);
		} else {
			log.info("User 2 present, updating");
			var user2 = uwvUser2Opt.get();
			var salt = passwordHasher.createSalt();
			var pwd2FromAstrid = "tp*-j;EmU_x^";
			var hashedPassword = passwordEncoder.encode("HatseFlatse00!"/* , salt */);
			user2.setPassword(new String(hashedPassword));
			user2.setSalt(salt);
			userService.save(user2);
		}
	}
	
	private User initFrankie(PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var user = initUser("fvo013", "frankie.vos@uwv.nl", "Welkom01", hasher);
		return user;
	}

	private User initUser(String username, String emailAddress, String password, PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var salt = hasher.createSalt();
		var hashedPassword = hasher.hash(password, salt);
		var user = User
				.builder()
				.username(username)
				.emailAddress(emailAddress)
				.password(new String(hashedPassword))
				.salt(salt)
				.build();
		
		return user;

	}
	private User initJochem(PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var user = initUser("jbe074","jochem.bermon@uwv.nl","Welkom01", hasher);
		return user;
	}

	private User initJur(PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// jja042
		var user = initUser("jja042", "jur.jansen@uwv.nl", "Welkom01", hasher);//new User();
		return user;
	}

	private User initPraveen(PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var user = initUser("pri045", "praveen.vanrijswijk@uwv.nl", "Welkom01", hasher);//new User();
		return user;
	}

	private User intitRobert(PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var user = initUser("rko116", "robert.koote@uwv.nl", "Welkom01", hasher);//new User();
		return user;
	}

	private User initRoderik(PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var user = initUser("rde062", "roderik.dernison@uwv.nl", "Welkom01", hasher);//new User();
		return user;
	}

	private User initRoel(PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var user = initUser("rsw009", "roel.swierts@uwv.nl", "Welkom01", hasher);//new User();
		return user;
	}

	private User initSieto(PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var sieto = initUser("sri075", "sieto.richardson@uwv.nl", "Welkom01", hasher);
		return sieto;
	}

	private User initUwvUser(PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var uwvUser = initUser("uwv001", "uwv001@uwv.nl", "pYQ+h,nKYxWN", hasher);
		return uwvUser;
	}

	private User initUwvUser2(PasswordHasher hasher) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var uwvUser = initUser("uwv002", "uwv002@uwv.nl", "tp*-j;EmU_x^", hasher);
		return uwvUser;
	}



}
