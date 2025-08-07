package com.teknotrait.webautomation.webActions;

import java.io.File;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.zeroturnaround.zip.ZipUtil;

import com.teknotrait.webautomation.fileHandles.PropertyFileHandling;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebPageActions extends PropertyFileHandling {

	static WebDriver driver;

	public static WebDriverWait wait;

	public void launchBroswer(String browserType) {

		if (browserType.equalsIgnoreCase("Chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();

		} else if (browserType.equalsIgnoreCase("MsEdge")) {
			WebDriverManager.edgedriver().setup(); // ✅ Corrected here
			driver = new EdgeDriver();

		} else if (browserType.equalsIgnoreCase("Firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5000));
	}
	// implicitlyWait(5000, TimeUnit.MILLISECONDS);

	public void sendUrl(String url) {

		driver.get(url);

	}

	public void navigateToUrl(String url) {

		driver.navigate().to(url);

	}

	public String getTitle() {

		return driver.getTitle();
	}

	public void customWaitForElement(By locator, long seconds) {

		for (int i = 0; i <= seconds; i++) {

			try {

				if (getElement(locator) == null) {

					Thread.sleep(1000);

				} else {

					break;
				}

			} catch (Exception e) {

				continue;
			}
		}

	}

	public void customWaitAndClick(By locator, Duration seconds) {

		wait = new WebDriverWait(driver, seconds);

		try {

			wait.until(ExpectedConditions.visibilityOf(getElement(locator)));
			wait.until(ExpectedConditions.elementToBeClickable(locator)).click();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public WebElement getElement(By locator) {

		return driver.findElement(locator);

	}

	public List<WebElement> getElements(By locator) {

		return driver.findElements(locator);

	}

//	public void clickElement(By locator) {
//
//		getElement(locator).click();
//
//	}

	public void enterTextInTextBox(By locator, String text) {

		clearTextInTextBox(locator);
		getElement(locator).sendKeys(text);

	}

	public void clearTextInTextBox(By locator) {

		try {

			getElement(locator).clear();
			Thread.sleep(500);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public String getText(By locator) {

		return getElement(locator).getText();
	}

	public List<String> getTextFormListofWebElements(By locator) {

		List<String> listData = new ArrayList<String>();

		for (WebElement ele : getElements(locator)) {

			listData.add(ele.getText());
		}

		return listData;

	}

	public void pressEnter(By locator) {

		getElement(locator).sendKeys(Keys.ENTER);

	}

	public void quit() {

		driver.quit();
	}

	// Take screenshot function
	public void getScreenShot(String ssName) {

		TakesScreenshot scrShot = ((TakesScreenshot) driver);

		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

		File DestFile = new File(System.getProperty("user.dir") + "/screenshots/" + ssName + ".png");

		try {

			FileUtils.copyFile(SrcFile, DestFile);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// At the end of the test convert the html report into Zipped file
	public void generateZippedReport() {

		try {

			ZipUtil.pack(new File(System.getProperty("user.dir") + "/report"),
					new File(System.getProperty("user.dir") + "\\eargo-automation-report.zip"));

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// At the beginning of the tests, we run this function to clean up existing test
	// reports
	public void deleteAllReports() {

		File directory = new File(System.getProperty("user.dir") + "/report");

		File[] files = directory.listFiles();
		for (File file : files) {

			file.delete();

		}

	}

	public void scrollToElement(By elementLocator) {

		try {

			String script_string = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0); var elementTop = arguments[0].getBoundingClientRect().top; window.scrollBy(0, elementTop-(viewPortHeight/4));";
			((JavascriptExecutor) driver).executeScript(script_string, getElement(elementLocator));
			Thread.sleep(1000);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void scrollDownPage(int pixel) {

		try {

			String script_string = "window.scrollBy(0," + pixel + ")";
			((JavascriptExecutor) driver).executeScript(script_string);
			Thread.sleep(500);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void scrollToElementViewAndWait(By elementLocator, int seconds) {

		try {

			String script_string = "arguments[0].scrollIntoView();";
			((JavascriptExecutor) driver).executeScript(script_string, getElement(elementLocator));
			Thread.sleep(seconds * 1000);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void scrollDownInContainer(By scrollableContainerLocator) {

		try {

			String script_string = "arguments[0].scrollTo(0, arguments[0].scrollHeight);";
			((JavascriptExecutor) driver).executeScript(script_string, getElement(scrollableContainerLocator));
			Thread.sleep(500);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public String generatePhoneNumber() {

		Random rand = new Random();
		int num1 = (rand.nextInt(7) + 1) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
		int num2 = rand.nextInt(743);
		int num3 = rand.nextInt(10000);

		DecimalFormat df3 = new DecimalFormat("000"); // 3 zeros
		DecimalFormat df4 = new DecimalFormat("0000"); // 4 zeros

		String phoneNumber = df3.format(num1) + df3.format(num2) + df4.format(num3);

		return phoneNumber;

	}

	public String generateUniqueEmailIDForInsurance() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String currentTimeStamp = sdf.format(timestamp);
		String timeStampEmail = currentTimeStamp + "@eargoautomation.com";
		return timeStampEmail;
	}

	public String getCurrentURL() {

		return driver.getCurrentUrl();
	}

	public void moveToElement(By locator) {

		Actions action = new Actions(driver);
		action.moveToElement(getElement(locator)).build().perform();
		action.release().perform();
	}

	public void customSwitchToFrame(By locator, Duration seconds) {

		wait = new WebDriverWait(driver, seconds);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}

	public void switchToParentFrame() {

		driver.switchTo().defaultContent();
	}

	public void openNewWindowAndSwitchToIt() {

		((JavascriptExecutor) driver).executeScript("window.open()");

		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get((tabs.size() - 1)));
	}

	public void navigateToTab(int tabIndex) {

		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tabIndex));
	}

	public double getFormattedAmount(String amount, int decimalPlace) {

		double formattedAmount = Double.parseDouble(amount.replaceAll("[^.0-9]", ""));
		int scale = (int) Math.pow(10, decimalPlace);
		return (double) Math.round(formattedAmount * scale) / scale;
	}

	public WebElement waitForVisibilityOfElement(By locator, long seconds) {
		try {
			Thread.sleep(seconds * 1000); // converting to milliseconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
		return wait.until(ExpectedConditions.visibilityOf(getElement(locator)));
	}

	public boolean isElementPresent(By locator) {

		try {

			driver.findElement(locator);
			return true;

		} catch (Exception e) {

			return false;
		}
	}

	public void waitFor(long seconds) {

		try {

			Thread.sleep(seconds);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void scrollToPageTop() {

		try {

			String script_string = "window.scrollTo(0, -document.body.scrollHeight)";
			((JavascriptExecutor) driver).executeScript(script_string);
			Thread.sleep(500);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void clickElementByJS(By locator) {

		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("arguments[0].click();", getElement(locator));

	}

	public Boolean waitForURLtoMatch(String url, Duration seconds) {

		wait = new WebDriverWait(driver, seconds);
		return wait.until(ExpectedConditions.urlMatches(url));

	}

//	public void waitForVisibilityOfElementLocated(By locator, Duration seconds) {
//
//		wait = new WebDriverWait(driver, seconds);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
//
//	}

	public WebElement customWaitForElementToClick(By locator, Duration seconds) {

		wait = new WebDriverWait(driver, seconds);
		return wait.until(ExpectedConditions.elementToBeClickable(locator));

	}

	public static void waitForPageToLoad(WebDriver driver, Duration seconds) {

		wait = new WebDriverWait(driver, seconds);
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
				.equals("complete"));

	}

	public void refreshPage() {

		driver.navigate().refresh();

	}

	public String getElementAttributeValue(By locator, String attributeName) {

		try {

			return getElement(locator).getAttribute(attributeName);

		} catch (Exception e) {

			return "";
		}

	}

	public void customWaitForWindow(int windowIndex) {

		try {

			wait.until(ExpectedConditions.numberOfWindowsToBe(windowIndex));

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void selectOptionFromDropDown(By locator, String text) {

		Select selectDrpDwn = new Select(getElement(locator));
		selectDrpDwn.selectByVisibleText(text);

	}

	public void pressBackSpace(By locator) {

		getElement(locator).sendKeys(Keys.BACK_SPACE);

	}

	public void enterTextAndClickEnter(By locator, String value) {

		getElement(locator).sendKeys(value + Keys.ENTER);

	}

	public String customWaitToGetText(By locator, Duration seconds) {

		wait = new WebDriverWait(driver, seconds);
		wait.until(ExpectedConditions.elementToBeClickable(locator));
		return getElement(locator).getText();

	}

	public String getCurrentWindowId() {

		try {
			String mainWindow = driver.getWindowHandle();
			return mainWindow;
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}

	public void customSwitchToWindow() {

		try {
			// String parentWindow = driver.getWindowHandle();
			// customWaitForWindow(2);
			Set<String> allWindow = driver.getWindowHandles();
			for (String window : allWindow) {
				switchToWindow(window);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void switchToWindow(String window) {
		driver.switchTo().window(window);
	}

	public void waitForStalenessOfElementLocated(By locator, Duration seconds) {
		try {
			wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.stalenessOf(getElement(locator)));
		} catch (Exception e) {

		}

	}

	public void waitForInvisibilityOfElementLocated(By locator, Duration seconds) {
		try {
			wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.invisibilityOf(getElement(locator)));
		} catch (Exception e) {

		}

	}

	public void waitForVisibilityOfAllElementLocated(By locator, Duration seconds) {
		try {
			wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.visibilityOfAllElements(getElements(locator)));
		} catch (Exception e) {

		}

	}

	public static void waitForVisibilityOfElementLocated(WebElement element, Duration seconds) {

		wait = new WebDriverWait(driver, seconds);
		wait.until(ExpectedConditions.visibilityOf(element));

	}

	public static void waitForElementToBeClickable(WebElement element, Duration seconds) {
		wait = new WebDriverWait(driver, seconds);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public static void clickElement(WebElement element) {

		element.click();

	}

	public static void scrollIntoView(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
	}

	public static void safeClick(WebDriver driver, WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			element.click();
		} catch (ElementNotInteractableException e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		}
	}

}
