package nl.uwv.otod.otod_portal.util;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class SeleniumUtil {


	static {
		String chromedriverHome = System.getenv("CHROMEDRIVER_HOME");
		
		if (chromedriverHome == null) {
			chromedriverHome = "c:\\users\\rde063\\chrome-driver";
		}
		System.setProperty("webdriver.chrome.driver", chromedriverHome + "/chromedriver.exe");
	}
	
	public static WebDriver loadWebPage(String url) {
		WebDriver driver = new ChromeDriver();
		driver.get(url);		
		return driver;
	}
	
	public static boolean findKeyword(String keyword, WebDriver webDriver) {
		String pageSource = webDriver.getPageSource();
		return pageSource.contains(keyword);
	}
	
	public static boolean findKeyword(String keyword, String pageSource) {
		return pageSource.contains(keyword);
	}
	
	public static boolean findTitle(String title, WebDriver webDriver) {
		String pageTitle = webDriver.getTitle();
		return pageTitle.equals(title);
	}
	
	public static String findTitle(WebDriver webDriver) {
		return webDriver.getTitle();
	}
	
	public static void close(WebDriver driver) {
		driver.close();
	}
	
	// zie https://github.com/paul-hammant/ngWebDriver
	public static WebDriver loadNgPage(String url) {
		ChromeDriver webDriver = new ChromeDriver();
		return webDriver;
	}
	
	public static String findTitleNg(WebDriver webDriver) {
		WebElement h1Elt = webDriver.findElement(By.xpath("/html/body/app-root/div/h1"));
		String title = null;
		title = h1Elt.getText();
		return title;
	}
	
	public static String findByXPath(String xpath, WebDriver webDriver) {
		WebElement elt = findElementByXPath(xpath, webDriver);
		return elt.getText();
	}
	
	public static WebElement findElementByXPath(String xpath, WebDriver webDriver) {
		WebElement elt = webDriver.findElement(By.xpath(xpath));
		return elt;
	}
	
	public static void clickLink(String xpath, WebDriver webDriver) throws NoSuchElementException {
		WebElement link = webDriver.findElement(By.xpath(xpath));
		try {
		link.click();
		} catch(ElementNotInteractableException e) {
			e.printStackTrace();
		}
	}
	
	public static void hover(WebElement elt, WebDriver webDriver, String expression) {
		Actions action = new Actions(webDriver);
		action.moveToElement(elt).moveToElement(webDriver.findElement(By.xpath(expression))).click().build().perform();
	}
}
