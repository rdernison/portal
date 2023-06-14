package nl.uwv.otod.otod_portal.password;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class PasswordGeneratorTest {

	private static Logger logger = LogManager.getLogger();
	
	private PasswordGenerator gen = new PasswordGenerator();
	@Test
	public void testGeneratePassword() {
		var password = gen.generatePassword(8);
		logger.info("Generated password: {}", password);
	}
}
