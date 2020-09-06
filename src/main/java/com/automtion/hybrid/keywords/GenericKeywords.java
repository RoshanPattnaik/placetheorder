package com.automtion.hybrid.keywords;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.automation.hybrid.reports.ExtentManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.github.bonigarcia.wdm.WebDriverManager;

public class GenericKeywords {
	public Properties envProp;
	public Properties prop;
	public String objectKey;
	public String dataKey;
	public String proceedOnFail;
	public Hashtable<String,String> data;
	public WebDriver driver;
	public ExtentTest test;
	public SoftAssert softAssert = new SoftAssert();
	
	
	/*********************Setter functions***************************/
	public String getProceedOnFail() {
		return proceedOnFail;
	}

	public void setProceedOnFail(String proceedOnFail) {
		this.proceedOnFail = proceedOnFail;
	}


	public void setEnvProp(Properties envProp) {
		this.envProp = envProp;
	}
	
	public void setExtentTest(ExtentTest test) {
		this.test = test;
	}


	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public void setData(Hashtable<String, String> data) {
		this.data = data;
	}
    /*****************************************/
	
	
	

	public void openBrowser(){
		String browser=data.get(dataKey);
		test.log(Status.INFO,"Opening Browser "+browser );
		if(browser.equals("Mozilla")){
			// options
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "null");
			// invoke profile
			System.setProperty("webdriver.gecko.driver", "D:\\Common\\drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
		}else if(browser.equals("Chrome")){
			// init options
			System.out.println(System.getProperty("user.dir"));
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/driver/chromedriver.exe");		
			driver = new ChromeDriver();
		}else if(browser.equals("IE")){
			driver = new InternetExplorerDriver();
		}else if(browser.equals("Edge")){
			driver = new EdgeDriver();
		}
		
		// max and set implicit wait
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	
	public void navigate(){
		test.log(Status.INFO,"Navigating to website "+envProp.getProperty(objectKey));
		driver.get(envProp.getProperty(objectKey));
	}

	public void click(){
		test.log(Status.INFO,"Clicking "+prop.getProperty(objectKey));
		getObject(objectKey).click();
	}
	
	
	public void type(){
		getObject(objectKey).clear();
		test.log(Status.INFO,"Typing in "+prop.getProperty(objectKey)+" . Data - "+data.get(dataKey));
		getObject(objectKey).sendKeys(data.get(dataKey));
	}
	
	public void clear(){
		//test.log(Status.INFO,"Typing in "+prop.getProperty(objectKey)+" . Data - "+data.get(dataKey));
		getObject(objectKey).clear();
	}
	
	
	public void selectDropdown() {
		test.log(Status.INFO,"Dropdown "+prop.getProperty(objectKey)+" . Data - "+data.get(dataKey));
		Select dropdownValue = new Select(getObject(objectKey));
		dropdownValue.selectByVisibleText(data.get(dataKey));
	}
	
	public void dynamicClick() {
		String xpath_start = "//label[contains(.,'";
		String xpath_end = "')]/preceding-sibling::input";
		String xpath = xpath_start + data.get(dataKey) + xpath_end;
		test.log(Status.INFO, "Clicking " + data.get(dataKey));
		driver.findElement(By.xpath(xpath)).click();
	}
	
	public void dynamicType() {
		String xpath_start = "//input[@value='";
		String xpath_end = "']/parent::div/following-sibling::div/input";
		String xpath = xpath_start + dataKey + xpath_end;
		getObject(objectKey).clear();
		test.log(Status.INFO, dataKey+" Price-" + data.get(dataKey));
		driver.findElement(By.xpath(xpath)).sendKeys(data.get(dataKey));
	}
	
	public void validateTitle(){
		test.log(Status.INFO,"Validating title - "+prop.getProperty(objectKey) );
		String expectedTitle = prop.getProperty(objectKey);
		String actualTitle=driver.getTitle();
		if(!expectedTitle.equals(actualTitle)){
			// report failure
			reportFailure("Titles did not match. Got title as "+ actualTitle);
		}
	}
	
	public void validateElementPresent(){
		if(!isElementPresent(objectKey)){
			// report failure
			reportFailure("Element not found "+objectKey);
		}
	}
	
	public void scroll() {
				
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getObject(objectKey));
	}
	
	public void quit(){
		if(driver!=null)
			driver.quit();
	}
	/*********************Utitlity Functions************************/
	// central function to extract Objects
	public WebElement getObject(String objectKey){
		WebElement e=null;
		try{
		if(objectKey.endsWith("_xpath")) {
			if(objectKey.endsWith("chooseFile_xpath")) {
				e = driver.findElement(By.xpath(prop.getProperty(objectKey)));
			}
			e = driver.findElement(By.xpath(prop.getProperty(objectKey)));
		}
		else if(objectKey.endsWith("_id"))
			e = driver.findElement(By.id(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_css"))
			e = driver.findElement(By.cssSelector(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_name"))
			e = driver.findElement(By.name(prop.getProperty(objectKey)));

		WebDriverWait wait = new WebDriverWait(driver,20);
		// visibility of Object
		wait.until(ExpectedConditions.visibilityOf(e));
		// state of the object-  clickable
		wait.until(ExpectedConditions.elementToBeClickable(e));
		
		}catch(Exception ex){
			// failure -  report that failure
			reportFailure("Object Not Found "+ objectKey);
		}
		return e;
		
	}
	// true - present
	// false - not present
	public boolean isElementPresent(String objectKey){
		List<WebElement> list=null;
		
		if(objectKey.endsWith("_xpath"))
			list = driver.findElements(By.xpath(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_id"))
			list = driver.findElements(By.id(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_css"))
			list = driver.findElements(By.cssSelector(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_name"))
			list = driver.findElements(By.name(prop.getProperty(objectKey)));

		if(list.size()==0)
			return false;
		else
			return true;
	}
	
	public void uploadPic() throws IOException {
		new ProcessBuilder(System.getProperty("user.dir")+"//autoit//ProductPic.Exe").start();
	}
	
	/*******Reporting function********/
	public void reportFailure(String failureMsg){
		// fail the test
		test.log(Status.FAIL, failureMsg);
		// take the screenshot, embed screenshot in reports
		takeSceenShot();
		// fail in testng
		//Assert.fail(failureMsg);// stop on this line
		if(proceedOnFail.equals("Y")){// soft assertion
			softAssert.fail(failureMsg);
		}else{
			softAssert.fail(failureMsg);
			softAssert.assertAll();
		}
	}
	
	public void takeSceenShot(){
		// fileName of the screenshot
		Date d=new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		// take screenshot
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			// get the dynamic folder name
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath+screenshotFile));
			//put screenshot file in reports
			test.log(Status.INFO,"Screenshot-> "+ test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath+screenshotFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void assertAll(){
		softAssert.assertAll();
	}
	

}
