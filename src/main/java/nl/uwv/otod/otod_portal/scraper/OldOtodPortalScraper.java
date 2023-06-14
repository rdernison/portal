package nl.uwv.otod.otod_portal.scraper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.util.SeleniumUtil;

public class OldOtodPortalScraper {

	private static Logger logger = LogManager.getLogger();
	
	public List<Server> readServers() {
		List<Server>servers;
		WebDriver driver = SeleniumUtil.loadWebPage("http://otod.ba.uwv.nl");
		try {
			WebElement loginButton = driver.findElement(By.linkText("INLOGGEN"));
	//		SeleniumUtil.findElementByXPath(xpath, webDriver)
			loginButton.click();
			Thread.sleep(500);
			
			login(driver);
			
			Thread.sleep(500);
			
			servers = findServers(driver);
			writeServersToFile(servers);
		} catch (NoSuchElementException | InterruptedException | IOException e) {
			e.printStackTrace();
		} finally {
			SeleniumUtil.close(driver);
		}
		return /*servers*/null;
	}

	private void login(WebDriver driver) {
		logger.info("Logging in");
		WebElement usernameElt = driver.findElement(By.name("name"));
		WebElement passwordElt = driver.findElement(By.name("pass"));
		
		usernameElt.sendKeys("rde062");
		passwordElt.sendKeys("19Leiden02");
		
		WebElement submitElt = driver.findElement(By.name("op"));
		submitElt.click();
	}
	
	private List<Server> findServers(WebDriver driver) {
		logger.info("Finding servers");
		List<WebElement> menuElts = driver.findElements(By.className("menu"));
		boolean found = false;
		var servers = new ArrayList<Server>();
		for (int i = 0; !found && (i < menuElts.size()); i++) {
			
			WebElement menuElt = menuElts.get(i);
			try {
				WebElement serversElt = menuElt.findElement(By.partialLinkText("Servers"));
				logger.debug("Found servers elt");
				serversElt.click();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					logger.error(e.toString());
				}
				
				logger.info("Clearing input fields");
				WebElement editTitleElt = driver.findElement(By.id("edit-title"));
				logger.info("Found edit-title elt");
				editTitleElt.clear();
				WebElement editValueElt = driver.findElement(By.id("edit-field-status-value"));
				logger.info("Found edit-field-status-value elt");
				editValueElt.clear();
				found = true;
				WebElement submitElt = driver.findElement(By.id("edit-submit-servers"));
				logger.info("Clicking button");
				submitElt.click();
				
				WebElement divClassViewsContent = driver.findElement(By.className("view-content"));
				
				WebElement tableElt = divClassViewsContent.findElement(By.tagName("table"));
				WebElement tbodyElt = tableElt.findElement(By.tagName("tbody"));
				
				handleTableRows(servers, tbodyElt);
			} catch(NoSuchElementException e) {
				logger.debug("No such element, trying next");
			}
			
		}
		
		return servers;
	}

	private void handleTableRows(ArrayList<Server> servers, WebElement tbodyElt) {
		List<WebElement> trElts = tbodyElt.findElements(By.tagName("tr"));
		logger.info("Parsing html");
		for (WebElement trElt : trElts) {
			List<WebElement> tdElts = trElt.findElements(By.tagName("td"));
			WebElement titleElt = tdElts.get(0);
			WebElement ipAddressElt = tdElts.get(2);
			WebElement statusElt = tdElts.get(10);
			
			Server server = new Server();
			String title = titleElt.getText();
			String ipAddress = ipAddressElt.getText();
			String status = statusElt.getText();
			server.setName(title);
			server.setIpAddress(ipAddress);
			server.setStatus(status);
			
			servers.add(server);
		}
	}
	
	private void writeServersToFile(List<Server> servers) throws IOException {
		logger.info("Writing stuff to file");
		StringBuilder sb = new StringBuilder();
		for (Server server : servers) {
			sb.append(server.getName())
			.append(",")
			.append(server.getIpAddress())
			.append(",")
			.append(server.getStatus())
			.append("\n");
		}
		LocalDate today = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Path path = Paths.get("c:/users/rde063/Documents/servers-"  + format.format(today) + ".csv");
		Files.writeString(path, sb.toString());
	}
}
