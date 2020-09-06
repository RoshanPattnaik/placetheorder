package com.automation.hybrid.suitea;


import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.automation.hybrid.base.BaseTest;
import com.automation.hybrid.util.Constants;
import com.automation.hybrid.util.DataUtil;

// name of java file should be same as name in xls file
public class LoginTest extends BaseTest {
	// make sure that i have my prop and xls file object init
	// before test starts
	
	
	
	
	@Test(dataProvider="getData")
	public void LoginTest(Hashtable<String,String> data) throws Exception{
		
		test.log(Status.INFO, "Starting "+ testName);

		if(DataUtil.isSkip(testName, xls) ||data.get(Constants.RUNMODE_COL).equals(Constants.RUNMODE_NO)){
			test.log(Status.SKIP, "Runmode is set to NO");
			throw new SkipException("Runmode is set to NO");
		}		
		System.out.println("Running Login Test");
	    ds.executeKeywords(testName, xls, data);
	}
	
	
}
