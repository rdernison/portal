package nl.uwv.otod.otod_portal.mail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class MailSenderTest {

	@Test
	public void testSendMail() {
		var mailSender = new MailSender();
		try {
			mailSender.sendMail("ict.tsctotem@uwv.nl", "Java-mail. Gelukt.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
