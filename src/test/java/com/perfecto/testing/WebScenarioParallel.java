package com.perfecto.testing;

import java.io.IOException;
import java.net.MalformedURLException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.perfecto.testing.utils.Utils;

public class WebScenarioParallel {
	RemoteWebDriver driver;

	// Create Remote WebDriver based on testng.xml configuration
	@Parameters({ "platformName", "platformVersion", "browserName", "browserVersion", "screenResolution" })
	@BeforeTest
	public void beforeTest(String platformName, String platformVersion, String browserName, String browserVersion, String screenResolution) throws MalformedURLException {
		driver = Utils.getRemoteWebDriver(platformName, platformVersion, browserName, browserVersion, screenResolution);
	}
	
	// Test Method, navigate to google and perform search
	@Test
	public void searchGoogle() throws MalformedURLException {				

		driver.get("http://www.google.com");

		try {
			final String searchKey = "Perfecto Mobile";
			WebElement element = driver.findElement(By.name("q"));
			element.sendKeys(searchKey);
			element.submit();
			(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return d.getTitle().toLowerCase().startsWith(searchKey.toLowerCase());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Done: searchGoogle");
	}

	// Test Method, navigate to Geico and get insurance quote
	@Test
	public void geicoInsurance() throws MalformedURLException {

		driver.get("http://www.geico.com");
 
		try{
			driver.findElement(By.id("auto")).click();
			driver.findElement(By.id("zip")).sendKeys("01434");
			driver.findElement(By.id("submitButton")).click();
			//driver.findElement(By.id("btnSubmit")).click();
			driver.findElement(By.xpath("//*[@id= 'CustomerForm:firstName']")).sendKeys("Eran");
			driver.findElement(By.xpath("//*[@id= 'CustomerForm:lastName']")).sendKeys("Kinsbruner");
			driver.findElement(By.xpath("//*[@id= 'CustomerForm:customerMailingAddress']")).sendKeys("Lexington");

			driver.findElement(By.id("CustomerForm:birthMonth")).sendKeys("8");
			driver.findElement(By.id("CustomerForm:birthDay")).sendKeys("3");
			driver.findElement(By.id("CustomerForm:birthYear")).sendKeys("1981");

			driver.findElement(By.id("CustomerForm:continueBtn")).click();

 			driver.findElement(By.id("btnSubmit"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Done: geicoInsurance");
	}

	@AfterTest
	public void afterTest() throws IOException {
		driver.close();
		driver.quit();	
	}	
	
}
