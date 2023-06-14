package nl.uwv.otod.otod_portal.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MailSender {
	public void sendMail(String to, String message) throws IOException {
		var from = "ict.tsctotem@uwv.nl";
		var subject = "Update password";
		var command = String.format(
				"powershell.exe \"Send-MailMessage -From '%s' -To '%s' -SmtpServer uwvmailrelay.voorzieningen.uwv.nl -Body '%s' -Subject '%s'\"",
				from, to, message, subject);
		log.info(command);
		// Executing the command
		Process powerShellProcess = Runtime.getRuntime().exec(command);
		// Getting the results
		powerShellProcess.getOutputStream().close();
		String line;
		System.out.println("Standard Output:");
		BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
		while ((line = stdout.readLine()) != null) {
			System.out.println(line);
		}
		stdout.close();
		System.out.println("Standard Error:");
		BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
		while ((line = stderr.readLine()) != null) {
			System.out.println(line);
		}
		stderr.close();
		System.out.println("Done");
	}
}
