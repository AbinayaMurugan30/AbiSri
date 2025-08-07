package com.teknotrait.webautomation.pages;

import static org.testng.Assert.assertEquals;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.teknotrait.webautomation.webActions.WebPageActions;

public class HomePage {
	private WebDriver driver;

//WebElements
	@FindBy(xpath = "//body/div[@id='root']/div[@id='wrapper']/div/div/div[@id='left-sidebar']/div[@class='sidebar-scroll']"
			+ "			/div[@id='menu']/div[@id='left-sidebar-nav']/ul[@id='main-menu']/li[3]/a[1]")
	private WebElement mySchool;

	@FindBy(xpath = "//a[@href='/student']")
	private WebElement students;

	@FindBy(xpath = "//div[@aria-label='Dropdown select']")
	private WebElement status;

	@FindBy(xpath = "//span[@role='option' and contains(text(), 'Active')]")
	private WebElement active;

	@FindBy(xpath = "//span[@role='option' and contains(text(), 'Active')]")
	private WebElement inactive;
	
	@FindBy(xpath = "//button[normalize-space()='InActive']")
	private WebElement inactiveButton;

//Constructor
	public HomePage(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this); // To initilaize the webelements and bound it with DOM

	}

//Actions

	public void selectMySchool() {
//	WebPageActions.waitForVisibilityOfElementLocated(mySchool, Duration.ofSeconds(10));
		mySchool.click();
	}

	public void selectStudents() {
		// WebPageActions.waitForVisibilityOfElementLocated(students,
		// Duration.ofSeconds(10));
		students.click();
	}

	public void setstatus() {
		status.click();

}
	
	public String inActiveStatus() {
		WebPageActions.waitForElementToBeClickable(inactive, Duration.ofSeconds(10));
		inactive.click();
		return inactive.getText();
		
	}
	
	public String activeStatus() {
		WebPageActions.waitForElementToBeClickable(active, Duration.ofSeconds(10));
		active.click();
		return active.getText();
		
	}
	
//	public void selectStatusAndVerify(String statusToSelect) throws InterruptedException {
//	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//	    // 1. Click the Status dropdown
//	    WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(
//	        By.xpath("//div[contains(@class,'react-dropdown-select-content')]")));
//	    statusDropdown.click();
//
//	    // 2. Select the desired option (Active or Inactive)
//	    WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
//	        By.xpath("//span[@role='option' and normalize-space(text())='" + statusToSelect + "']")));
//	    option.click();
//
//	    // 3. Wait for table refresh (optional: use dynamic wait if there's a loader)
//	    Thread.sleep(2000); // Replace with explicit wait if possible
//
//	    // 4. Get all status buttons from the student rows
//	    List<WebElement> statusButtons = driver.findElements(By.xpath("//tbody//tr//td[last()]//button"));
//
//	    // 5. Validate each status matches the selected one
//	    for (WebElement button : statusButtons) {
//	        String buttonText = button.getText().trim();
//	        System.out.println("Status displayed: " + buttonText);
//	        Assert.assertEquals(buttonText, statusToSelect, "Mismatch in status!");
//	    }
//
//	    System.out.println("✅ All students listed are '" + statusToSelect + "'");
//	}

}