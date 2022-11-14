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
import java.time.Duration;

public class MainPageTest {
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach        public void setUp() {

        //Change chromedriver path to be able to run tests
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\HP\\.cache\\selenium\\chromedriver\\win32\\107.0.5304.62\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(12));
                driver.get("https://acceptance.mgthost.com/login");

        mainPage = new MainPage(driver);
    }

    @AfterEach        public void tearDown() {
        driver.quit();
    }


    @Test
    //Test to confirm that submitted leave requests confirmation pop-up
    public void LeaveRequestConfirmation() throws InterruptedException {
        mainPage.email.sendKeys("demo+AB-01277@mercans.com");
        mainPage.password.sendKeys("Employee1!");
        mainPage.loginButton.click();

        WebElement leavesMenu = driver.findElement (By.xpath ("//*[contains(text(),'Leaves')]"));
        leavesMenu.click();

        WebElement myLeaves = driver.findElement(By.cssSelector("[href='/ess/leaves/720/employee/my-leaves']"));
        myLeaves.click();

        WebElement newLeaveRequest = driver.findElement (By.xpath ("//*[contains(text(),'Request new leave')]"));
        newLeaveRequest.click();

        //time for webpage to catchup
        TimeUnit.SECONDS.sleep(1);
        WebElement startDate = driver.findElement(By.xpath("//div[@class='calendar__wrapper']//div[@class='current__month calendar__grid-cell']/div[.='23']"));
        startDate.click();
        WebElement endDate = driver.findElement(By.xpath("//div[@class='calendar__wrapper']//div[@class='current__month calendar__grid-cell']/div[.='24']"));
        endDate.click();

        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Request leave')]")));
        WebElement LeaveRequestButton = driver.findElement(By.xpath("//*[contains(text(),'Request leave')]"));
        LeaveRequestButton.click();

        TimeUnit.SECONDS.sleep(1);

        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(By.id("confirmAction")));
        WebElement confirmButton = driver.findElement(By.id("confirmAction"));
        confirmButton.click();

        WebElement confirmation = driver.findElement (By.xpath ("//*[contains(text(),'Request has been submitted')]"));


        assertTrue(confirmation.isDisplayed());

    }



    @Test
    //Test to confirm that newly requested leave is also added into 'My requests'
    public void NewRequestAddedUnderMyrequests() throws InterruptedException {
        mainPage.email.sendKeys("demo+AB-01277@mercans.com");
        mainPage.password.sendKeys("Employee1!");
        mainPage.loginButton.click();

        WebElement leavesMenu = driver.findElement (By.xpath ("//*[contains(text(),'Leaves')]"));
        leavesMenu.click();

        WebElement myLeaves = driver.findElement(By.cssSelector("[href='/ess/leaves/720/employee/my-leaves']"));
        myLeaves.click();

        WebElement leaveCountBefore = driver.findElement(By.cssSelector(".leave-tiles__wrapper > div:nth-of-type(1) > .custom-card__header .custom-badge"));
        int countBefore = Integer.parseInt(leaveCountBefore.getText());

        WebElement newLeaveRequest = driver.findElement (By.xpath ("//*[contains(text(),'Request new leave')]"));
        newLeaveRequest.click();

        TimeUnit.MILLISECONDS.sleep(500);
        WebElement startDate = driver.findElement(By.xpath("//div[@class='calendar__wrapper']//div[@class='current__month calendar__grid-cell']/div[.='25']"));
        startDate.click();
        WebElement endDate = driver.findElement(By.xpath("//div[@class='calendar__wrapper']//div[@class='current__month calendar__grid-cell']/div[.='28']"));
        endDate.click();

        TimeUnit.MILLISECONDS.sleep(200);
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Request leave')]")));
        WebElement leaveRequestButton = driver.findElement(By.xpath("//*[contains(text(),'Request leave')]"));
        leaveRequestButton.click();

        TimeUnit.SECONDS.sleep(1);
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(By.id("confirmAction")));
        WebElement confirmButton = driver.findElement(By.id("confirmAction"));
        confirmButton.click();

        TimeUnit.SECONDS.sleep(1);
        int countAfter = Integer.parseInt(leaveCountBefore.getText());

        assertTrue(countBefore < countAfter);
    }



    @Test
    //Test to validate that overlapping  displays error
    public void ValidateOverlappingLeaves() throws InterruptedException {
        mainPage.email.sendKeys("demo+AB-01277@mercans.com");
        mainPage.password.sendKeys("Employee1!");
        mainPage.loginButton.click();

        WebElement leavesMenu = driver.findElement (By.xpath ("//*[contains(text(),'Leaves')]"));
        leavesMenu.click();

        WebElement myLeaves = driver.findElement(By.cssSelector("[href='/ess/leaves/720/employee/my-leaves']"));
        myLeaves.click();


        WebElement newLeaveRequest = driver.findElement (By.xpath ("//*[contains(text(),'Request new leave')]"));
        newLeaveRequest.click();

        TimeUnit.MILLISECONDS.sleep(500);
        WebElement startDate = driver.findElement(By.xpath("//div[@class='calendar__wrapper']//div[@class='current__month calendar__grid-cell']/div[.='25']"));
        startDate.click();
        WebElement endDate = driver.findElement(By.xpath("//div[@class='calendar__wrapper']//div[@class='current__month calendar__grid-cell']/div[.='28']"));
        endDate.click();

        WebElement alert = driver.findElement(By.cssSelector(".red-alert > span"));

        assertTrue(alert.isDisplayed());


    }


    @Test
    //Test to validate the managers abiliity to approve requests made by workers
    public void ApprovalOfSubmittedRequest() throws InterruptedException {
        mainPage.email.sendKeys("demo+SH-00477@mercans.com\n");
        mainPage.password.sendKeys("Manager1!");
        mainPage.loginButton.click();

        WebElement leavesMenu = driver.findElement (By.xpath ("//*[contains(text(),'Leaves')]"));
        leavesMenu.click();

        WebElement approvals = driver.findElement(By.cssSelector("[href='/ess/leaves/720/reviewer/listing']"));
        approvals.click();

        TimeUnit.MILLISECONDS.sleep(500);
        WebElement leaveRequest = driver.findElement(By.cssSelector("tbody > tr:nth-of-type(1) .user-photo-name"));
        leaveRequest.click();

        TimeUnit.MILLISECONDS.sleep(1000);
        WebElement approveButton = driver.findElement(By.cssSelector(".button-action--save"));
        approveButton.click();

        TimeUnit.MILLISECONDS.sleep(500);
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(By.id("confirmAction")));
        WebElement confirmButton = driver.findElement(By.id("confirmAction"));
        confirmButton.click();

        WebElement confirmation = driver.findElement (By.xpath ("//*[contains(text(),'Review step has been approved. Request new status: approved')]"));

        assertTrue(confirmation.isDisplayed());

    }


    @Test
    //Test to validate the managers abiliity to reject requests made by workers
    public void RejectionOfSubmittedRequest() throws InterruptedException {
        mainPage.email.sendKeys("demo+SH-00477@mercans.com\n");
        mainPage.password.sendKeys("Manager1!");
        mainPage.loginButton.click();

        WebElement leavesMenu = driver.findElement (By.xpath ("//*[contains(text(),'Leaves')]"));
        leavesMenu.click();

        WebElement approvals = driver.findElement(By.cssSelector("[href='/ess/leaves/720/reviewer/listing']"));
        approvals.click();

        TimeUnit.MILLISECONDS.sleep(500);
        WebElement leaveRequest = driver.findElement(By.cssSelector("tbody > tr:nth-of-type(1) .user-photo-name"));
        leaveRequest.click();

        TimeUnit.MILLISECONDS.sleep(1000);
        WebElement rejectButton = driver.findElement(By.cssSelector(".button-action--custom > span"));
        rejectButton.click();

        WebElement reasonTextBox = driver.findElement(By.cssSelector("#rejected_notes"));
        reasonTextBox.sendKeys("yes");

        WebElement rejectConfirm = driver.findElement(By.cssSelector(".button-action--delete > span"));
        rejectConfirm.click();

        WebElement confirmation = driver.findElement (By.xpath ("//*[contains(text(),'Review step has been rejected. Request new status: rejected')]"));

        assertTrue(confirmation.isDisplayed());

    }
}
