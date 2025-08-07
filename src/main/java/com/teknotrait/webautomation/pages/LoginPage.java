package com.teknotrait.webautomation.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.teknotrait.webautomation.webActions.WebPageActions;

public class LoginPage {
	private WebDriver driver;

//WebElements
	@FindBy(xpath = "//input[@placeholder='Email address']")
	private WebElement email;

	@FindBy(xpath = "//input[@id='signin-password']")
	private WebElement password;

	@FindBy(xpath = "//button[@type='submit']")
	private WebElement loginButton;

//Constructor
	public LoginPage(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this); // To initilaize the webelements and bound it with DOM

	}

//Actions

	public void login(String userEmail, String userPassword) {
	//	WebPageActions.waitForPageToLoad(driver, Duration.ofSeconds(10));
		email.sendKeys(userEmail);
		password.sendKeys(userPassword);
		loginButton.click();

	}

}