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

public class Test1 {
	private WebDriver driver = DriverManager.getDriver();
	private static  final Logger log = LogManager.getLogger(Test1.class);
	private DataTable studentDataTable;

	
	LoginPage lp= new LoginPage(driver);
	HomePage hp= new HomePage(driver);
	StudentPage sp= new StudentPage(driver);
	AddNewStudentPage anp= new AddNewStudentPage(driver);
	
	
	
	@Given("Login to the application with a valid credentials")
	public void login_to_the_application_with_a_valid_credentials() {
	    lp.login("croissance.abhik@gmail.com ","Abcd@1234");   
	    log.info("Application Launched successfully with Valid Credentials");
	}

	@When("user clicks on My Schools")
	public void user_clicks_on_my_schools() throws InterruptedException {
	    hp.selectMySchool();	    
	    log.info("Selected My school successfully");
	   
	    hp.selectStudents();
	    log.info("Selected Students successfully");
	}

	@When("tries to Add a New Student with the details")
	public void tries_to_add_a_new_student_with_the_details(DataTable dataTable) throws Exception {
		this.studentDataTable = dataTable;
		sp.addNewStudent();
	    log.info("Add New Student button selected successfully");
	    List<Map<String, String>> studentList = dataTable.asMaps(String.class, String.class);
	    Map<String, String> student = studentList.get(0);

	    new AddNewStudentPage(driver).setStudent(
	        student.get("studentFirstName"),
	        student.get("studentLastName"),
	        student.get("studentDob"),
	        student.get("studentPhno"),
	        student.get("studentNationality"),
	        student.get("studentLevel"),
	        student.get("studentGrade"),
	        student.get("studentClassRoom"),
	        student.get("studentTiming"),
	        student.get("studentTerms"),
	        student.get("parentFirstName"),
	        student.get("ParentLastName"),
	        student.get("relation"),
	        student.get("parentPhNo"),
	        student.get("parentEmailId"),
	        student.get("parentCountry"),
	        student.get("parentState"),
	        student.get("parentCity")
	      
	        
	    );
	    log.info("Student Details are added successfully");
	}

	@Then("verify student should be successfully created")
	public void verify_student_should_be_successfully_created() {
		
		anp.checkNewlyAddedStudentVerification(studentDataTable);
		log.info("Student Details are verified successfully");
	}

	

}
