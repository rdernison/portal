package nl.uwv.otod.otod_portal.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import nl.uwv.otod.otod_portal.model.Password;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

public class PasswordReader {
	public String readPassword(int serverId) throws IOException {
		var rootPath = SettingsUtil.readSetting("path");
		
		Path inputPath = Paths.get(rootPath, "admin-pass.csv");
		var password = "";
		List<String> allLines = Files.readAllLines(inputPath);
		for (var line : allLines) {
			var subs = line.split("\\|");
			var serverIdFromLine = Integer.parseInt(subs[1]);
			if (serverIdFromLine == serverId) {
				password = subs[7];
				break;
			}
			
		}
		
		return password;
	}
	
}
