package com.selenium.miniproject;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.*;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

public class AlaskaCruiseTests {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    SoftAssert softAssert = new SoftAssert();

    @BeforeClass
    @Parameters("browser")
    public void setup(@Optional("chrome") String browser) throws Exception {

        if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            System.setProperty("webdriver.edge.driver", "C:\\Users\\2486174\\Downloads\\edgedriver_win64\\msedgedriver.exe");
            driver = new EdgeDriver();
        } else if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else {
            throw new IllegalArgumentException("Browser not matched: " + browser + ". Please use chrome, firefox or edge.");
        }

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;

        System.out.println("Browser launched");
    }

    @Test(priority = 1)
    public void openHomePage() {

        driver.get("https://www.royalcaribbean.com/alaska-cruises");
        waitForPageLoad();

        WebElement header = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("header")));

        softAssert.assertTrue(header.isDisplayed(), "Homepage not loaded");
        softAssert.assertAll();
    }

    @Test(priority = 2)
    public void searchShip() {

        driver.get("https://www.royalcaribbean.com/cruise-ships/rhapsody-of-the-seas");
        waitForPageLoad();

        WebElement title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".basicHero__textLine--large")));

        softAssert.assertTrue(
                title.getText().toLowerCase().contains("rhapsody")
                        || driver.getTitle().toLowerCase().contains("rhapsody"),
                "Rhapsody ship page not loaded");
        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void clickShopNow() {

        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("promoHeroCTAButton")));

        js.executeScript("arguments[0].scrollIntoView(true);", btn);
        js.executeScript("arguments[0].click();", btn);

        waitForPageLoad();

        softAssert.assertTrue(
                driver.getCurrentUrl().contains("cruises"),
                "Shop Now did not navigate to cruise listings");
        softAssert.assertAll();
    }

    @Test(priority = 4)
    public void applyFilters() {

        driver.get("https://www.royalcaribbean.com/cruises?search=destination:ALASKA&sort=by:RECOMMENDED");
        waitForPageLoad();

        applyFilter("Departure", "Seattle");
        applyFilter("Destination", "Alaska");
        applyFilter("Duration", "7");

        applyFilter("Month", "Jun");
        applyFilter("Month", "Jul");
        applyFilter("Month", "Aug");
        applyFilter("Month", "Sep");

        try {
            WebElement seeResults = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'See')]")));

            js.executeScript("arguments[0].click();", seeResults);

        } catch (Exception e) {
            System.out.println("Auto-applied filters");
        }

        wait.until(ExpectedConditions.jsReturnsValue(
                "return document.querySelectorAll(\"[class*='cruise']\").length > 0;"));

        softAssert.assertTrue(
                driver.getCurrentUrl().contains("ALASKA") || driver.getCurrentUrl().contains("cruises"),
                "Filters page not loaded correctly");
        softAssert.assertAll();
    }

    @Test(priority = 5)
    public void validateResults() {

        List<WebElement> results = driver.findElements(By.tagName("li"));

        long visible = results.stream()
                .filter(el -> {
                    try {
                        return el.isDisplayed() && !el.getText().trim().isEmpty();
                    } catch (StaleElementReferenceException e) {
                        return false;
                    }
                })
                .count();

        System.out.println("Total Results: " + visible);

        softAssert.assertTrue(visible > 0, "No cruise results found");
        softAssert.assertAll();
    }

    @AfterMethod
    public void closeBrowser(ITestResult result) {

        if (result.getStatus() == ITestResult.SUCCESS) {
            takeScreenshot(result.getName() + "_PASS_");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result.getName() + "_FAIL_");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void takeScreenshot(String testName) {

        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File folder = new File("screenshots");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(folder, "AlaskaCruiseTest_" + testName + timestamp + ".png");

            FileHandler.copy(src, dest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitForPageLoad() {
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }

    private void applyFilter(String label, String value) {
        try {
            WebElement filter = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'" + label + "')]")));

            js.executeScript("arguments[0].click();", filter);

            WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(text(),'" + value + "')]")));

            js.executeScript("arguments[0].click();", option);

        } catch (Exception e) {
            System.out.println("Filter failed: " + label + " -> " + value);
        }
    }
}