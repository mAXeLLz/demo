package com.example.demo;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.time.Duration;

public class MainPageTest {
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach        public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\HP\\.cache\\selenium\\chromedriver\\win32\\107.0.5304.62\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                driver.get("https://acceptance.mgthost.com/login");

        mainPage = new MainPage(driver);
    }

    @AfterEach        public void tearDown() {
        driver.quit();
    }
    
    @Test
    public void LeaveRequestConfirmation() throws InterruptedException {
        mainPage.email.sendKeys("demo+AB-01277@mercans.com");
        mainPage.password.sendKeys("Employee1!");
        mainPage.loginButton.click();

        WebElement LeavesMenu = driver.findElement (By.xpath ("//*[contains(text(),'Leaves')]"));
        LeavesMenu.click();

        WebElement MyLeaves = driver.findElement(By.cssSelector("[href='/ess/leaves/720/employee/my-leaves']"));
        MyLeaves.click();

        WebElement NewLeaveRequest = driver.findElement (By.xpath ("//*[contains(text(),'Request new leave')]"));
        NewLeaveRequest.click();

        TimeUnit.SECONDS.sleep(1);
        WebElement StartDate = driver.findElement(By.xpath("//div[@class='calendar__wrapper']//div[@class='current__month calendar__grid-cell']/div[.='23']"));
        StartDate.click();
        WebElement endDate = driver.findElement(By.xpath("//div[@class='calendar__wrapper']//div[@class='current__month calendar__grid-cell']/div[.='24']"));
        endDate.click();

        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Request leave')]")));
        WebElement LeaveRequestButton = driver.findElement(By.xpath("//*[contains(text(),'Request leave')]"));
        LeaveRequestButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(By.id("confirmAction")));
        WebElement confirmButton = driver.findElement(By.id("confirmAction"));
        confirmButton.click();

        WebElement confirmation = driver.findElement (By.xpath ("//*[contains(text(),'Request has been submitted')]"));


        assertTrue(confirmation.isDisplayed());

    }
}
