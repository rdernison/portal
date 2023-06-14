package nl.uwv.otod.otod_portal.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SqlReader {

	public void readSqlFile(Path inputPath) {
		var ipAddresses = "";
		var nodes = "";
		var passwords = "";
		var users = "";
		var startNodes = false;
		var startPasswords = false;
		var startIpAddresses = false;
		var startUsers = false;
		var startProjects = false;
		
		try {
			List<String> allLines;
			allLines = Files.readAllLines(inputPath);
			log.info("Concatenating lines {}", allLines.size());
			for (String line : allLines) {
				
				
				if (line.equals("LOCK TABLES `node` WRITE;")) {
					log.info("Found nodes start tag");
					startNodes = true;
				}
				
				if (line.equals("LOCK TABLES `field_data_field_admin_pass` WRITE;")) {
					log.info("Found passwords start tag");
					startPasswords = true;
				}

				if (line.equals("LOCK TABLES `field_data_field_ip_adres` WRITE;")) {
					log.info("Found ip addresses start tag");
					startIpAddresses = true;
				}
				
				if (line.equals("LOCK TABLES `users` WRITE;")) {
					startUsers = true;
				}

				if (startNodes) {
					nodes += (line + "\n");
				}
				
				if (startIpAddresses) {
					ipAddresses += (line + "\n");
				}
				
				if (startPasswords) {
					passwords += (line + "\n");
				}
				
				if (startUsers) {
					users += (line + "\n");
				}
				
				if (startNodes && line.equals("UNLOCK TABLES;")) {
					log.info("Found nodes end tag");
					startNodes = false;
				}
				
				
				if (startPasswords && line.equals("UNLOCK TABLES;")) {
					log.info("Found passwords end tag");
					startPasswords = false;
				}
				
				if (startIpAddresses && line.equals("UNLOCK TABLES;")) {
					log.info("Found ip addresses end tag");
					startIpAddresses = false;
				}
				
				if (startUsers && line.equals("UNLOCK TABLES;")) {
					log.info("Found users end tag");
					startUsers = false;
				}
				
			}
			log.info("written data");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		writeNodesToFile(nodes);
		writePasswordsToFile(passwords);
		writeIpAddressesToFile(ipAddresses);*/
		writeUsersToFile(users);
	}
	Path outputDir = Paths.get("c:/users/rde062/Documents/oude portal"); 
	private void writeNodesToFile(String nodes) {
		Path nodesPath = outputDir.resolve("nodes.txt");
		try {
			Files.writeString(nodesPath, nodes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writePasswordsToFile(String passwords) {
		Path passwordsPath = outputDir.resolve("passwords.txt");
		try {
			Files.writeString(passwordsPath, passwords);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeIpAddressesToFile(String ipAddresses) {
		Path ipAddressesPath = outputDir.resolve("ip-addresses.txt");
		try {
			Files.writeString(ipAddressesPath, ipAddresses);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void writeUsersToFile(String users) {
		Path usersPath = outputDir.resolve("users.txt");
		try {
			Files.writeString(usersPath, users);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String findServers(String fullSql) {
		log.info("Finding servers");
		Pattern p = Pattern.compile("LOCK TABLE `node` WRITE.*UNLOCK TABLES");
		Matcher m = p.matcher(fullSql);
		String g = null;
		if (m.find()) {
			g = m.group();
		}
		log.info("Found it: {}", g);
		return g;
	}
	
	public String findPasswords(String fullSql) {
		log.info("Finding passwords");
		Pattern p = Pattern.compile("LOCK TABLE `field_data_field_admin_pass` WRITE.*UNLOCK TABLES");
		Matcher m = p.matcher(fullSql);
		String g = null;
		if (m.find()) {
			g = m.group();
		}
		log.info("Found it: {}", g);
		return g;
		
	}
}
