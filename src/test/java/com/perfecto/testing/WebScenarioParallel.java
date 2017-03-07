
package com.perfecto.testing;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.exception.ReportiumException;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
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
    protected ReportiumClient reportiumClient;

    // Create Remote WebDriver based on testng.xml configuration
    @Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution"})
    @BeforeClass
    public void baseBeforeClass(String platformName, String platformVersion, String browserName, String browserVersion, String screenResolution) throws MalformedURLException {
        driver = Utils.getRemoteWebDriver(platformName, platformVersion, browserName, browserVersion, screenResolution);
        reportiumClient = createReportium(driver);
    }

    @AfterClass
    public void baseAfterClass() {
        if (reportiumClient != null) {
            System.out.println("Report URL = " + reportiumClient.getReportUrl());
        }
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        String testName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        reportiumClient.testStart(testName, new TestContext());
    }

    @SuppressWarnings("Duplicates")
    @AfterMethod
    public void afterMethod(ITestResult testResult) {
        int status = testResult.getStatus();
        switch (status) {
            case ITestResult.FAILURE:
                reportiumClient.testStop(TestResultFactory.createFailure("An error occurred", testResult.getThrowable()));
                break;
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
            case ITestResult.SUCCESS:
                reportiumClient.testStop(TestResultFactory.createSuccess());
                break;
            case ITestResult.SKIP:
                // Ignore
                break;
            default:
                throw new ReportiumException("Unexpected status " + status);
        }
    }

    private static ReportiumClient createReportium(WebDriver driver) {
        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                .withProject(new Project("Sample Geico Test","1.0"))
				.withContextTags("Regression")
                .withWebDriver(driver)
                .build();
        return new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
    }
    
    
    @Test
    public void searchGoogle() throws MalformedURLException {

        reportiumClient.testStep("Navigate to Google webpage");
        driver.get("http://www.google.com");

        reportiumClient.testStep("Searching for Perfecto Mobile");
        final String searchKey = "Perfecto Mobile";
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys(searchKey);
        element.submit();
        reportiumClient.testStep("Verify search results");
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

        reportiumClient.testStep("Navigate to Geico webpage");
        driver.get("http://www.geico.com");
        reportiumClient.testStep("Select Insurance type");
        driver.findElement(By.id("auto")).click();
        reportiumClient.testStep("Enter Zip Code");    
        driver.findElement(By.id("zip")).sendKeys("01434");
        reportiumClient.testStep("Click Submit");
		driver.findElement(By.id("submitButton")).click();
        reportiumClient.testStep("Enter First Name");
		driver.findElement(By.xpath("//*[@id= 'CustomerForm:firstName']")).sendKeys("Dan");
        reportiumClient.testStep("Enter Last Name");
		driver.findElement(By.xpath("//*[@id= 'CustomerForm:lastName']")).sendKeys("Kaligiery");
        reportiumClient.testStep("Enter Mailing Adress");
		driver.findElement(By.xpath("//*[@id= 'CustomerForm:customerMailingAddress']")).sendKeys("Woburn");
        reportiumClient.testStep("Enter Birth Mnoth");
		driver.findElement(By.id("CustomerForm:birthMonth")).sendKeys("8");
        reportiumClient.testStep("Enter Birth Day");
		driver.findElement(By.id("CustomerForm:birthDay")).sendKeys("3");
        reportiumClient.testStep("Enter Birth Year");
		driver.findElement(By.id("CustomerForm:birthYear")).sendKeys("1981");
        reportiumClient.testStep("Click Continue");
		driver.findElement(By.id("CustomerForm:continueBtn")).click();
        reportiumClient.testStep("Click Submit");
        driver.findElement(By.id("CustomerForm:continueBtn")).click();

        System.out.println("Done: geicoInsurance");
    	} catch (Exception e) {
			e.printStackTrace();
    }
}}






