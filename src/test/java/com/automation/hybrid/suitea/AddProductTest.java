package com.automation.hybrid.suitea;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.automation.hybrid.base.BaseTest;
import com.automation.hybrid.util.Constants;
import com.automation.hybrid.util.DataUtil;
import com.aventstack.extentreports.Status;

public class AddProductTest extends BaseTest {
	@Test(dataProvider = "getData")
	public void addProductTest(Hashtable<String, String> data) throws Exception {

		test.log(Status.INFO, "Starting " + testName);

		if (DataUtil.isSkip(testName, xls) || data.get(Constants.RUNMODE_COL).equals(Constants.RUNMODE_NO)) {
			test.log(Status.SKIP, "Runmode is set to NO");
			throw new SkipException("Runmode is set to NO");
		}
		System.out.println("Running Add New Product Test");
		ds.executeKeywords(testName, xls, data);
	}
}
