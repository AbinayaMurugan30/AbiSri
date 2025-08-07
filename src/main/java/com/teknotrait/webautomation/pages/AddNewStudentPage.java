package com.teknotrait.webautomation.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.teknotrait.webautomation.webActions.WebPageActions;

import io.cucumber.datatable.DataTable;

public class AddNewStudentPage {
	private WebDriver driver;

	// WebElements
	@FindBy(xpath = "//div[@class='input-filed']//input[contains(@class,'form-control custom-input')]")
	private WebElement firstName;

	@FindBy(xpath = "//div[@class='row text-start mt-3']//div[2]//div[1]//input[1]")
	private WebElement lastName;

	@FindBy(xpath = "//img[@class='svg-icon calender-icon']")
	private WebElement dob;

	@FindBy(xpath = "//select[@class='react-datepicker__month-select']")
	private WebElement monthSelectionFromCalendar;

	@FindBy(xpath = "//select[@class='react-datepicker__year-select']")
	private WebElement yearSelectionFromCalendar;

	@FindBy(xpath = "//input[@value='+91']")
	private WebElement phno;

	@FindBy(xpath = "//select[@class='select-dropdown form-select custom-input']")
	private WebElement nationality;

	@FindBy(xpath = "//button[@class='btn primary-btn ']")
	private WebElement continueButton;

	@FindBy(xpath = "//div[@class='input-filed']//select[contains(@class,'select-dropdown form-select custom-input')]")
	private WebElement selectLevel;

	@FindBy(xpath = "//div[contains(@class,'col-lg-6')]//div[2]//div[1]//select[1]")
	private WebElement selectGrade;

	@FindBy(xpath = "//div[3]//div[1]//select[1]")
	private WebElement classRoom;

	@FindBy(xpath = "//div[4]//div[1]//select[1]")
	private WebElement timing;

	@FindBy(xpath = "//span[normalize-space()='M']")
	private WebElement weekMonday;

	@FindBy(xpath = "//div[6]//div[1]//select[1]")
	private WebElement terms;

	@FindBy(xpath = "//span[text()='First Name']/following-sibling::input")
	private WebElement parentsFName;

	@FindBy(xpath = "//div[contains(@class,'col-lg-6')]//div[2]//div[1]//input[1]")
	private WebElement parentsLName;

	@FindBy(xpath = "//div[3]//div[1]//select[1]")
	private WebElement relationSelection;

	@FindBy(xpath = "//input[@type='tel']")
	private WebElement parentsPhNo;

	@FindBy(xpath = "//div[5]//div[1]//input[1]")
	private WebElement parentsEmailAddress;

	@FindBy(xpath = "//div[6]//div[1]//select[1]")
	private WebElement country;

	@FindBy(xpath = "//div[7]//div[1]//input[1]")
	private WebElement state;

	@FindBy(xpath = "//span[text()='City']/following-sibling::input")
	private WebElement city;

	@FindBy(css = ".btn.primary-btn")
	private WebElement finishButton;

	public AddNewStudentPage(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	public void setStudent(String studentFirstName, String studentLastName, String studentDob, String studentPhno,
			String studentNationality, String studentlevel, String studentGrade, String studentClassRoom,
			String studentTiming, String studentTerms, String parentFirstName, String ParentLastName, String relation,
			String parentPhNo, String parentEmailId, String parentCountry, String parentState, String parentCity) {
		
		firstName.sendKeys(studentFirstName);
		
		lastName.sendKeys(studentLastName);

		try {
// Step 1: Click on date field to open calendar
			WebPageActions.clickElement(dob);
// Step 2: Split the input DOB: dd-MM-yyyy
			String[] dobParts = studentDob.split("-");
			String day = dobParts[0];
			String month = dobParts[1];
			String year = dobParts[2];

// Step 3: Select Year
			Select yearSelect = new Select(driver.findElement(By.className("react-datepicker__year-select")));
			yearSelect.selectByVisibleText(year);

// Step 4: Select Month (0 = Jan, so 12 - 1 = 11 for December)
			Select monthSelect = new Select(driver.findElement(By.className("react-datepicker__month-select")));
			monthSelect.selectByIndex(Integer.parseInt(month) - 1);

// Step 5: Click on the correct day
// Look for aria-label containing exact match for day
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			String dateXpath = "//div[contains(@class,'react-datepicker__day') and text()='" + Integer.parseInt(day)
					+ "']" + "[not(contains(@class,'--outside-month'))]";
			WebElement dateElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dateXpath)));
			dateElement.click();

		} catch (Exception e) {
			throw new RuntimeException("Failed to select DOB: " + studentDob, e);
		}

// Step 6: Fill remaining fields
		phno.sendKeys(studentPhno);
		nationality.sendKeys(studentNationality);
		WebPageActions.safeClick(driver, continueButton);

		selectLevel.click();

		System.out.println("Select Level" + selectLevel);
		if (studentlevel != null && !studentlevel.trim().isEmpty()) {
			Select schoollevel = new Select(selectLevel);
			schoollevel.selectByVisibleText(studentlevel);
		} else {
			throw new IllegalArgumentException("studentlevel is null or empty");
		}

		if (studentGrade != null && !studentGrade.trim().isEmpty()) {
			Select studentgrade = new Select(selectGrade);
			studentgrade.selectByVisibleText(studentGrade);
		} else {
			throw new IllegalArgumentException("studentgrade is null or empty");
		}

		if (studentClassRoom != null && !studentClassRoom.trim().isEmpty()) {
			Select classRoomDropDown = new Select(classRoom);
			classRoomDropDown.selectByVisibleText(studentClassRoom);
		} else {
			throw new IllegalArgumentException("student Class room value is null or empty");
		}

		if (studentTiming != null && !studentTiming.trim().isEmpty()) {
			Select timingDropDown = new Select(timing);
			timingDropDown.selectByVisibleText(studentTiming);
		} else {
			throw new IllegalArgumentException("student timing is null or empty");
		}

		WebPageActions.clickElement(weekMonday);

		if (studentTerms != null && !studentTerms.trim().isEmpty()) {
			Select termsDropDown = new Select(terms);
			termsDropDown.selectByVisibleText(studentTerms);
		} else {
			throw new IllegalArgumentException("student terms selection is null or empty");
		}
		WebPageActions.safeClick(driver, continueButton);
		WebPageActions.waitForVisibilityOfElementLocated(parentsFName, Duration.ofSeconds(10));
		parentsFName.sendKeys(parentFirstName);
		parentsLName.sendKeys(ParentLastName);

		if (relation != null && !relation.trim().isEmpty()) {
			Select relationDropDown = new Select(relationSelection);
			relationDropDown.selectByVisibleText(relation);
		} else {
			throw new IllegalArgumentException("Relation selection is null or empty");
		}

		parentsPhNo.sendKeys(parentPhNo);
		parentsEmailAddress.sendKeys(parentEmailId);
		
		country.sendKeys(parentCountry);
		
		state.sendKeys(parentState);
		
		city.sendKeys(parentCity);

		WebPageActions.clickElement(finishButton);
		
	}
		
		public void checkNewlyAddedStudentVerification(DataTable dataTable) {
			List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
		    String fullName = data.get(0).get("studentFirstName") + " " + data.get(0).get("studentLastName");
		    String expectedParentName = data.get(0).get("parentFirstName");
		    String expectedParentPhone = data.get(0).get("parentPhNo");
		    String expectedGrade = data.get(0).get("studentGrade");
		    String expectedClassroom = data.get(0).get("studentClassRoom");

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		    
		    // Find the row for the student
		    WebElement studentRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
		        By.xpath("//a[normalize-space()='" + fullName + "']/ancestor::tr")));

		    // Verify Parent Name
		    String parentNameText = studentRow.findElement(By.xpath("./td[4]/div[1]")).getText();
		    Assert.assertTrue(parentNameText.contains(expectedParentName), "❌ Parent name mismatch!");

		    // Verify Parent Phone
		    String parentPhoneText = studentRow.findElement(By.xpath("./td[4]/div[2]")).getText();
		    Assert.assertTrue(parentPhoneText.contains(expectedParentPhone), "❌ Parent phone mismatch!");

		    // Verify Grade
		    String actualGrade = studentRow.findElement(By.xpath("./td[5]")).getText();
		    Assert.assertEquals(actualGrade.trim(), expectedGrade, "❌ Grade mismatch!");

		    // Verify Classroom
		    String actualClassroom = studentRow.findElement(By.xpath("./td[6]")).getText();
		    Assert.assertEquals(actualClassroom.trim(), expectedClassroom, "❌ Classroom mismatch!");


		    System.out.println("✅ Student '" + fullName + "' successfully created and verified.");
		}

	}

