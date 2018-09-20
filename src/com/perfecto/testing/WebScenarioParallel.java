
package com.perfecto.testing;


import com.perfecto.testing.utils.Utils;

//import testNG.PerfectoUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

public class WebScenarioParallel {

    protected RemoteWebDriver driver;

    // Create Reportium client

    // Create Remote WebDriver based on testng.xml configuration
    @Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution"})
    @BeforeClass
    public void baseBeforeClass(String platformName, String platformVersion, String browserName, String browserVersion, String screenResolution) throws MalformedURLException {
        driver = Utils.getRemoteWebDriver(platformName, platformVersion, browserName, browserVersion, screenResolution);
    }




    @SuppressWarnings("Duplicates")
    @AfterMethod
    public void afterMethod(ITestResult testResult) {
        int status = testResult.getStatus();
        switch (status) {
            case ITestResult.FAILURE:
                break;
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
            case ITestResult.SUCCESS:
                break;
            case ITestResult.SKIP:
                // Ignore
                break;
            default:
        }
    }


    
    @Test
    public void searchGoogle() throws MalformedURLException {

        driver.get("http://www.google.com");

        final String searchKey = "Perfecto Mobile";
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys(searchKey);
        element.submit();
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith(searchKey.toLowerCase());
            }
        });
        System.out.println("Done: searchGoogle");
    }

    // Test Method, navigate to Geico and get insurance quote
    
    @Test
    public void geicoInsurance() throws MalformedURLException {
    	
    	try{

        driver.get("http://www.geico.com");
        driver.findElement(By.id("auto")).click();
        driver.findElement(By.id("zip")).sendKeys("01434");
		driver.findElement(By.id("submitButton")).click();
		driver.findElement(By.xpath("//*[@id= 'CustomerForm:firstName']")).sendKeys("Dan");
		driver.findElement(By.xpath("//*[@id= 'CustomerForm:lastName']")).sendKeys("Kaligiery");
		driver.findElement(By.xpath("//*[@id= 'CustomerForm:customerMailingAddress']")).sendKeys("Woburn");
		driver.findElement(By.id("CustomerForm:birthMonth")).sendKeys("8");
		driver.findElement(By.id("CustomerForm:birthDay")).sendKeys("3");
		driver.findElement(By.id("CustomerForm:birthYear")).sendKeys("1981");
		driver.findElement(By.id("CustomerForm:continueBtn")).click();
        driver.findElement(By.id("CustomerForm:continueBtn")).click();

        System.out.println("Done: geicoInsurance");
    	} catch (Exception e) {
			e.printStackTrace();
    }
}}






