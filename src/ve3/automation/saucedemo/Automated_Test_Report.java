package ve3.automation.saucedemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class Automated_Test_Report {

    public static void main(String[] args) throws EncryptedDocumentException, IOException, InterruptedException {

        //FileInputStream fis = new FileInputStream("./projects/saucedemo.xlsx");
        FileInputStream fis = new FileInputStream("./projects/saucedemo.xlsx");
        System.out.println("Debuging 27" + fis.toString());
        Workbook wb = WorkbookFactory.create(fis);
        System.out.println("Line 29");
        Sheet sheet = wb.getSheet("sheet1");
        System.out.println("Line 31");

        int rowCount = sheet.getLastRowNum();

        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                String username = row.getCell(0).getStringCellValue();
                String password = row.getCell(1).getStringCellValue();

                String testName = "Login Test for user: " + username;
                WebDriver driver = new ChromeDriver();
                driver.manage().window().maximize();

                try {
                    driver.get("https://www.saucedemo.com/v1/");
                    driver.findElement(By.id("user-name")).sendKeys(username);
                    driver.findElement(By.id("password")).sendKeys(password);
                    driver.findElement(By.id("login-button")).click();

                    boolean isHomePage = driver.findElements(By.className("inventory_list")).size() > 0;

                    if (isHomePage) {
                        logResult(testName, "PASS", null);
                    } else {
                        captureScreenshot(driver, username);
                        logResult(testName, "FAIL", username + ".png");
                    }

                } catch (Exception e) {
                    captureScreenshot(driver, username);
                    logResult(testName, "FAIL", username + ".png");
                    e.printStackTrace();
                } finally {
                    driver.quit();
                }
            }
        }

        System.out.println("\n🔁 Logging in with first user for full flow...");

        Row firstRow = sheet.getRow(1);
        if (firstRow != null) {
            String firstUsername = firstRow.getCell(0).getStringCellValue();
            String firstPassword = firstRow.getCell(1).getStringCellValue();

            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();

            try {
                driver.get("https://www.saucedemo.com/v1/");
                driver.findElement(By.id("user-name")).sendKeys(firstUsername);
                driver.findElement(By.id("password")).sendKeys(firstPassword);
                driver.findElement(By.id("login-button")).click();

                boolean isHomePage = driver.findElements(By.className("inventory_list")).size() > 0;

                if (isHomePage) {
                    System.out.println("✅ Logged in with first user: " + firstUsername);

                    WebElement sortDropdown = driver.findElement(By.className("product_sort_container"));
                    Select select = new Select(sortDropdown);
                    select.selectByVisibleText("Price (low to high)");
                    Thread.sleep(2000);

                    List<WebElement> productNames = driver.findElements(By.className("inventory_item_name"));
                    System.out.println("Products after filtering (Low to High):");
                    for (WebElement product : productNames) {
                        System.out.println(" - " + product.getText());
                    }

                    WebElement firstAddToCartBtn = driver.findElement(By.xpath("(//button[text()='ADD TO CART'])[1]"));
                    String selectedProductName = productNames.get(0).getText();
                    firstAddToCartBtn.click();
                    System.out.println("Added product to cart: " + selectedProductName);

                    driver.findElement(By.className("shopping_cart_link")).click();
                    Thread.sleep(1000);

                    WebElement cartItem = driver.findElement(By.className("inventory_item_name"));
                    if (cartItem.getText().equals(selectedProductName)) {
                        System.out.println("Product is present in cart: " + selectedProductName);
                    }

                    driver.findElement(By.xpath("//a[text()='CHECKOUT']")).click();
                    driver.findElement(By.id("first-name")).sendKeys("Gauri");
                    driver.findElement(By.id("last-name")).sendKeys("Aasape");
                    driver.findElement(By.id("postal-code")).sendKeys("412308");
                    driver.findElement(By.xpath("//input[@value='CONTINUE']")).click();
                    driver.findElement(By.xpath("//a[text()='FINISH']")).click();

                    WebElement successMsg = driver.findElement(By.className("complete-header"));
                    if (successMsg.getText().equalsIgnoreCase("THANK YOU FOR YOUR ORDER")) {
                        logResult("Checkout Flow", "PASS", null);
                    } else {
                        captureScreenshot(driver, "checkout");
                        logResult("Checkout Flow", "FAIL", "checkout.png");
                    }
                }

            } catch (Exception e) {
                captureScreenshot(driver, "checkout");
                logResult("Checkout Flow", "FAIL", "checkout.png");
                e.printStackTrace();
            } finally {
                Thread.sleep(2000);
                driver.quit();
            }
        }
    }

    public static void captureScreenshot(WebDriver driver, String filename) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File("./screenshot/automatedreport" + filename + ".png");
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logResult(String testName, String status, String screenshot) {
        System.out.println("\n----------------------------------");
        System.out.println("Test Name: " + testName);
        System.out.println("Status: " + status);
        if (screenshot != null) {
            System.out.println("Screenshot: ./screenshots/" + screenshot);
        }
        System.out.println("----------------------------------");
    }
}
