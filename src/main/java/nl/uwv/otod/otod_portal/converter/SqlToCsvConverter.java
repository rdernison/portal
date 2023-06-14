package nl.uwv.otod.otod_portal.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlToCsvConverter {

	public void convertIpAddresses(Path path) {
		Path sqlPath = path.resolve("ip-addresses.txt");
		
		
		Path ipAddressesCsvPath = path.resolve("ip-addresses.csv");
		String result = "";
		try {
			List<String> allLines = Files.readAllLines(sqlPath);
			
			for (String line : allLines) {
				if (line.contains("INSERT")) {
					result = convertSqlLine(line);
				}
			}
			
			Files.writeString(ipAddressesCsvPath, result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String convertSqlLine(String line) {
		var result = "";
		var endOfLine = line.substring(line.indexOf("VALUES") + 7);
		Pattern p = Pattern.compile("\\([^\\)]*\\)");
		Matcher m = p.matcher(endOfLine);
		while (m.find()) {
			var ip = m.group();
			result += ip.substring(1, ip.length() - 1) + "\n";
		}
		return result;
	}

	public void convertNodes(Path path) {
		Path sqlPath = path.resolve("nodes.txt");
		
		Path nodesCsvPath = path.resolve("nodes.csv");
		String result = "";
		try {
			List<String> allLines = Files.readAllLines(sqlPath);
			
			for (String line : allLines) {
				if (line.contains("INSERT")) {
					result = convertSqlLine(line);
				}
			}
			
			Files.writeString(nodesCsvPath, result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void convertPasswords(Path path) {
		Path sqlPath = path.resolve("passwords.txt");
		
		Path passwordsCsvPath = path.resolve("passwords.csv");
		
		String result = "";
		try {
			List<String> allLines = Files.readAllLines(sqlPath);
			
			for (String line : allLines) {
				if (line.contains("INSERT")) {
					result = convertSqlLine(line);
				}
			}
			
			Files.writeString(passwordsCsvPath, result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void convertUsers(Path path) {
		Path sqlPath = path.resolve("users.txt");
		
		Path passwordsCsvPath = path.resolve("users.csv");
		
		String result = "";
		try {
			List<String> allLines = Files.readAllLines(sqlPath);
			
			for (String line : allLines) {
				if (line.contains("INSERT")) {
					result = convertSqlLine(line);
				}
			}
			
			Files.writeString(passwordsCsvPath, result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
