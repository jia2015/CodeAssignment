package com.shopkeepapp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.LoginPage;

public class TC_iPadButtonLayout {
  
	private WebDriver driver;
	private String baseUrl;
	LoginPage loginPage;
	
	@BeforeClass
	public void beforeClass() {
		
		System.setProperty("webdriver.gecko.driver","\\Users\\mrli\\Desktop\\software QA\\selenium\\geckodriver.exe");
		driver = new FirefoxDriver();
		
		baseUrl = "https://test-rl-1.shopkeepapp.com/ipad-layout";
				
		loginPage = new LoginPage(driver);
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.get(baseUrl);
		
		loginPage.typeUser("richard_li@shopkeep.com");
		loginPage.typePassword("RLpassword123");
		loginPage.clickOnLogin();	
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		
	    String currentURL = driver.getCurrentUrl();
	    System.out.println(currentURL);
	}
	
	//created 10 major cases to test the functionalities on the button layout page
	
	@Test (priority=1)
	public void searchBox_validText() throws InterruptedException {
		
		WebElement search = driver.findElement(By.id("item-search-input"));
		search.sendKeys("do");
		Thread.sleep(3000);
		WebElement itemName = driver.findElement(By.xpath("//ul[@id='results']/li[not(@style)]//span[@class='description']"));
		Thread.sleep(3000);
		System.out.println(itemName.getText());
		Assert.assertTrue(itemName.getText().contains("Donut"));
	}
	
	@Test (priority=2)
	public void changePageName() {
		
		WebElement pageName = driver.findElement(By.id("button-page-name"));
		pageName.clear();
		pageName.sendKeys("drink");
		pageName.sendKeys(Keys.ENTER);
		
		WebElement pageTab = driver.findElement(By.xpath("//div[@id='button-page-tabs']//a[@class='page-tab active']"));
		Assert.assertTrue(pageTab.getText().equals("drink"));
	}
	
	@Test (priority=3)
	public void addNewPage() throws InterruptedException {
		
		List<WebElement> pageTabs = driver.findElements(By.xpath("//div[@id='button-page-tabs']//a"));
		int tabNum = pageTabs.size();
		
		WebElement addButton = driver.findElement(By.xpath("//div[@id='button-page-control']//a[@id='add-button-page']"));
		addButton.click();
		Thread.sleep(3000);
		
		List<WebElement> pageTabs1 = driver.findElements(By.xpath("//div[@id='button-page-tabs']//a"));
		int currentTabNum = pageTabs1.size();
		Assert.assertEquals(currentTabNum, tabNum + 1);
	}
	
	@Test (priority=4)
	public void deleteCurrentPage() {
		
		WebElement pageName = driver.findElement(By.id("button-page-name"));
		pageName.clear();
		
		//create a unique name for the current page
		Long currentTime = System.currentTimeMillis();
		String uniqueName = Long.toString(currentTime);
		
		pageName.sendKeys(uniqueName);
		pageName.sendKeys(Keys.ENTER);
		
		WebElement deleteButton = driver.findElement(By.xpath("//div[@id='button-page-control']//a[@id='destroy-button-page']"));
		deleteButton.click();
		
		WebDriverWait wait = new WebDriverWait(driver, 3);
		WebElement confirmButton = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id='button_pages-index']//button[contains(text(), 'Confirm')]")));
		confirmButton.click();
		
		WebElement pageTab = driver.findElement(By.xpath("//div[@id='button-page-tabs']//a[@class='page-tab active']"));
		Assert.assertTrue(pageTab.getText() != uniqueName);
		
	}
	
	@Test (priority=5)
	public void dragItem_and_dropIntoPage() throws InterruptedException {
		
		WebElement search = driver.findElement(By.id("item-search-input"));
		search.clear();
		search.sendKeys(Keys.ENTER);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		
		WebElement fromElement = driver.findElement(By.xpath("//ul[@id='results']//span[contains(text(), 'Latte')]"));
		WebElement toElement = driver.findElement(By.xpath("//div[@id='button-pages']/div[@class='button_page'][@style='display: block;']/div[12]"));		
		
		// Drag Latte and drop at the 2x2 position
		Actions action = new Actions(driver);
		//action.dragAndDrop(fromElement, toElement).build().perform();
		//Thread.sleep(3000);
		
		action.clickAndHold(fromElement).perform();
		Thread.sleep(2000);
		action.moveToElement(toElement).perform();
		Thread.sleep(2000);
		action.release(toElement).perform();
		Thread.sleep(3000);
		
		WebDriverWait wait = new WebDriverWait(driver, 5);
		WebElement itemButton = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[@id='button-pages']/div[@class='button_page'][@style='display: block;']/div[34]//p")));
		
		//WebElement itemButton = driver.findElement(
		//		By.xpath("//div[@id='button-pages']/div[@class='button_page'][@style='display: block;']/div[34]/div/div[2]/p"));
		Assert.assertEquals(itemButton.getText(), "Latte");
		
	}
	
	@Test (priority=6)
	public void dragButton_and_dropIntoList() throws InterruptedException {
		
		WebElement fromElement = driver.findElement(
				By.xpath("//div[@id='button-pages']/div[@class='button_page'][@style='display: block;']/div[34]/div"));
		WebElement toElement = driver.findElement(By.xpath("//div[@id='item-list']"));		
		
		// Drag the Latte button and drop into the list
		Actions action = new Actions(driver);
		action.dragAndDrop(fromElement, toElement).build().perform();
		Thread.sleep(3000);
		
		WebElement search = driver.findElement(By.id("item-search-input"));
		search.sendKeys("Latte");
		Thread.sleep(3000);
		WebElement itemName = driver.findElement(By.xpath("//ul[@id='results']/li[not(@style)]//span[@class='description']"));
		Thread.sleep(3000);
		System.out.println(itemName.getText());
		
		Assert.assertTrue(itemName.getText().contains("Latte"));
	}
	
	@Test (priority=7)
	public void verify_TheRightNavigationArrow() {
		
		WebElement rightArrow = driver.findElement(By.id("button-tab-next"));
		rightArrow.click();
		rightArrow.click();
		
		WebElement pageTab = driver.findElement(By.xpath("//div[@id='button-page-tabs']//a[@class='page-tab active']"));
		Assert.assertEquals(pageTab.getText(), "998");
	}
	
	@Test (priority=8)
	public void verify_TheLeftNavigationArrow() {
		
		WebElement leftArrow = driver.findElement(By.id("button-tab-prev"));
		leftArrow.click();
		leftArrow.click();
		
		WebElement pageTab = driver.findElement(By.xpath("//div[@id='button-page-tabs']//a[@class='page-tab active']"));
		Assert.assertEquals(pageTab.getText(), "drink");
	}
	
	@Test (priority=9)
	public void verify_changingButtonText() throws InterruptedException {
		
		WebElement button = driver.findElement(
				By.xpath("//div[@id='button-pages']/div[@class='button_page'][@style='display: block;']/div[32]/div"));	
		button.click();
		Thread.sleep(3000);
		
		//$x("//p[.='ice tea3243']/ancestor::div[contains(@class,'si-button color-')][1]")
		WebElement buttonNameInput = driver.findElement(
				By.xpath("//div[@id='button-pages']/div[@class='button_page'][@style='display: block;']/div[32]/div//input"));
		Thread.sleep(2000);
		//JavascriptExecutor js = (JavascriptExecutor) driver;
		//js.executeScript("arguments[0].setAttribute('value', 'ice tea')", buttonNameInput);
		
		if (buttonNameInput.isDisplayed()) {
			System.out.println("input displayed");
		}
		
		buttonNameInput.click();
		buttonNameInput.clear();
		buttonNameInput.sendKeys("ice tea" + Keys.ENTER);
		
		WebElement buttonText = driver.findElement(
				By.xpath("//div[@id='button-pages']/div[@class='button_page'][@style='display: block;']/div[32]/div//p"));
		Assert.assertEquals(buttonText.getText(), "ice tea");
	}
	
	@Test (priority=10)
	public void verify_changingButtonColor() throws InterruptedException {
		
		WebElement search = driver.findElement(By.id("item-search-input"));
		search.click();
		
		WebElement button = driver.findElement(
				By.xpath("//div[@id='button-pages']/div[@class='button_page'][@style='display: block;']/div[31]/div"));
		button.click();
		Thread.sleep(3000);
		
		//color-195b86
		WebElement darkBlue = driver.findElement(
				By.xpath("//div[@id='button-pages']/div[@class='button_page'][@style='display: block;']/div[31]/div//a[5]"));
		//WebElement darkBlue = driver.findElement(By.cssSelector("div.button-text form-v2>div.color-palette > a[@data-color='195b86']"));
		darkBlue.click();
		
		WebElement buttonWithNewColor = driver.findElement(
				By.xpath("//div[@id='button-pages']/div[@class='button_page'][@style='display: block;']/div[31]/div"));
		String class_name = buttonWithNewColor.getAttribute("class");
		
		Assert.assertTrue(class_name.contains("color-195b86"));
	}
	
	@AfterClass
	public void afterClass() {
		driver.quit();
	}
}
