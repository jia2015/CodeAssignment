package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
	
	WebDriver driver;
	
	@FindBy(id = "login")
	@CacheLookup
	WebElement username;
	
	@FindBy(id = "password")
	@CacheLookup
	WebElement password;
	
	@FindBy(id = "submit")
	@CacheLookup
	WebElement loginButton;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void typeUser(String name) {
		username.sendKeys(name);
	}

	public void typePassword(String passwd) {
		password.sendKeys(passwd);
	}

	public void clickOnLogin()
	{
		loginButton.click();
	}
}
