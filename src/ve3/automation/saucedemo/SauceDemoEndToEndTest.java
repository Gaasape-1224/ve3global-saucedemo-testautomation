package ve3.automation.saucedemo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class SauceDemoEndToEndTest {

	public static void main(String[] args) throws EncryptedDocumentException, IOException, InterruptedException {

		// 1. Homepage Load Verification after Login (With different credentials)
		FileInputStream fis = new FileInputStream("./projects/saucedemo.xlsx");
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sheet = wb.getSheet("sheet1");

		int rowCount = sheet.getLastRowNum();
		for (int i = 1; i <= rowCount; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				String username = row.getCell(0).getStringCellValue();
				String password = row.getCell(1).getStringCellValue();

				System.out.println("Trying login with Username: " + username + " | Password: " + password);

				WebDriver driver = new ChromeDriver();
				driver.manage().window().maximize();
				driver.get("https://www.saucedemo.com/v1/");

				driver.findElement(By.id("user-name")).sendKeys(username);
				driver.findElement(By.id("password")).sendKeys(password);
				driver.findElement(By.id("login-button")).click();

				boolean isHomePage = driver.findElements(By.className("inventory_list")).size() > 0;
				if (isHomePage) {
					System.out.println("Login successful for: " + username);
				} else {
					System.out.println("Login failed for: " + username);
				}

				Thread.sleep(2000);
				driver.quit();
			}
		}

		System.out.println("Logging in with first user for full flow...");
		Row firstRow = sheet.getRow(1);
		if (firstRow != null) {
			String firstUsername = firstRow.getCell(0).getStringCellValue();
			String firstPassword = firstRow.getCell(1).getStringCellValue();

			// 2. Product Filtering
			// Login with any one credential and product filtering Low to High & printing
			// filtering products
			WebDriver driver = new ChromeDriver();
			driver.manage().window().maximize();
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

				// 3. Cart Operations
				// Selecting one product and added to cart
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
				} else {
					System.out.println("Product not found in cart.");
				}

				// 4.Checkout Process
				// Placing the produt with valid credentials
				driver.findElement(By.xpath("//a[text()='CHECKOUT']")).click();
				driver.findElement(By.id("first-name")).sendKeys("Gauri");
				driver.findElement(By.id("last-name")).sendKeys("Aasape");
				driver.findElement(By.id("postal-code")).sendKeys("412308");

				driver.findElement(By.xpath("//input[@value='CONTINUE']")).click();
				driver.findElement(By.xpath("//a[text()='FINISH']")).click();

				WebElement successMsg = driver.findElement(By.className("complete-header"));
				if (successMsg.getText().equalsIgnoreCase("THANK YOU FOR YOUR ORDER")) {
					System.out.println("Order placed successfully.");
				} else {
					System.out.println("Order placement failed.");
				}
			}
			Thread.sleep(2000);
			driver.quit();
		}
	}
}
