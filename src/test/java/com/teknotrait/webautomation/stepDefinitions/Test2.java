package com.teknotrait.webautomation.stepDefinitions;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.teknotrait.webautomation.fileHandles.DriverManager;
import com.teknotrait.webautomation.pages.AddNewStudentPage;
import com.teknotrait.webautomation.pages.HomePage;
import com.teknotrait.webautomation.pages.LoginPage;
import com.teknotrait.webautomation.pages.StudentPage;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Test2 {
	private WebDriver driver = DriverManager.getDriver();
	private static  final Logger log = LogManager.getLogger(Test2.class);
	
	LoginPage lp= new LoginPage(driver);
	HomePage hp= new HomePage(driver);
	StudentPage sp= new StudentPage(driver);
	AddNewStudentPage anp= new AddNewStudentPage(driver);
	
	@When("user clicks on My Schools and select the Inactive filter")
	public void user_clicks_on_my_schools_and_select_the_inactive_filter() {
	    hp.selectMySchool();
	    log.info("Selected My school successfully");
	    hp.selectStudents();
	    log.info("Selected My student successfully");
	
	    hp.setstatus();
	    log.info("Status is clicked");
	    
	}

	@Then("Verify all the records displayed should be inactive")
	public void verify_all_the_records_displayed_should_be_inactive() {
		String inactiveButton= hp.inActiveStatus();
		Assert.assertEquals(inactiveButton,"InActive");	    
		    log.info("Inactive - Verified successfully");
	}

	@When("Change the status to Active from the filter")
	public void change_the_status_to_from_the_filter(String string) {
		hp.setstatus();
	    log.info("Status is clicked");
	}

	@Then("Verify all the records displayed should be active")
	public void verify_all_the_records_displayed_should_be_active() {
		String activeButton= hp.activeStatus();
		Assert.assertEquals(activeButton,"InActive");	    
		log.info("Active - Verified successfully");
	}

	

}
