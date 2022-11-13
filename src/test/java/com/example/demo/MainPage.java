package com.example.demo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// page_url = https://www.jetbrains.com/
public class MainPage {
    @FindBy(css = "a.wt-button_mode_primary")
    public WebElement seeAllToolsButton;

    @FindBy(xpath = "//div[@data-test='main-menu-item' and @data-test-marker = 'Developer Tools']")
    public WebElement toolsMenu;

    @FindBy(css = "[data-test='site-header-search-action']")
    public WebElement searchButton;

    @FindBy(css = "[name='email']")
    public WebElement email;

    @FindBy(css = "[name='password']")
    public WebElement password;

    @FindBy(css = ".button-action > span")
    public WebElement loginButton;


    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
