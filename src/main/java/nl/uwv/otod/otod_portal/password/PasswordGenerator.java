package nl.uwv.otod.otod_portal.password;

import org.apache.commons.lang3.RandomStringUtils;

public class PasswordGenerator {

	public String generatePassword(int length) {
		var passwd = RandomStringUtils.randomAscii(length);//randomAlphanumeric(length);
		return passwd;
	}
}
