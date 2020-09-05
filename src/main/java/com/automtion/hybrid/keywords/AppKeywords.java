package com.automtion.hybrid.keywords;


import com.aventstack.extentreports.Status;

public class AppKeywords extends GenericKeywords{


	
	public void validateLogin(){
		test.log(Status.INFO, "Validating Login");
		String expectedResult = data.get("ExpectedResult");
		String actualResult="";
		
		boolean result = isElementPresent("product_xpath");
		if(result)
			actualResult = "LoginSuccess";
		else
			actualResult= "LoginFailure";
		
		
		if(!expectedResult.equals(actualResult))
			reportFailure("Got result as "+ actualResult);
	}
	
	public void defaultLogin(){
		String username=envProp.getProperty("adminusername");
		String password=envProp.getProperty("adminpassword");
		getObject("username_xpath").sendKeys(username);
		getObject("password_xpath").sendKeys(password);
		getObject("loginButton_xpath").click();
	}
}
