package com.teknotrait.webautomation.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.teknotrait.webautomation.webActions.WebPageActions;


public class StudentPage {
	private WebDriver driver;

//WebElement
	@FindBy(xpath = "//button[normalize-space()='Add New Student']")
	private WebElement addNewStudent;

//Constructor
	public StudentPage(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this); // To initilaize the webelements and bound it with DOM

	}

//Actions

	public void addNewStudent() {
		WebPageActions.waitForElementToBeClickable(addNewStudent, Duration.ofSeconds(10));
		addNewStudent.click();

	}

}