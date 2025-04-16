# SauceDemo Automation Framework

## 📄 Overview

This project is a Java-based Selenium automation framework designed to perform **end-to-end testing** and **automated reporting** for the [SauceDemo](https://www.saucedemo.com/v1/) web application. It includes:

- Automated login validation using multiple user credentials read from an Excel sheet.
- Product filtering and cart operations.
- Complete checkout process.
- Screenshot capturing for failed scenarios.
- Console-based test reporting.

There are two main classes in this project:
1. **SauceDemoEndToEndTest** – performs step-by-step test execution with console outputs.
2. **Automated_Test_Report** – automates login and checkout flows with status logging and screenshot on failure.

---

## 🛠 Tech Stack Used

- **Java** (Core Java)
- **Selenium WebDriver**
- **Apache POI** (Excel interaction)
- **Apache Commons IO** (FileUtils for screenshots)
- **Maven** (recommended for dependency management)
- **ChromeDriver** (for running tests in Chrome)

---

## 🔧 Prerequisites and How to Run

### Prerequisites

- Java JDK 8 or above
- Maven (optional but recommended)
- Chrome Browser installed
- ChromeDriver (matching your Chrome version)
- Eclipse / IntelliJ IDE (optional)
- Required JARs:
  - `selenium-java`
  - `poi`, `poi-ooxml`
  - `commons-io`

### Setup Instructions

1. **Clone the repository or add the source files** to your Java project.
2. Place the `saucedemo.xlsx` file inside `./projects/` directory.
3. Make sure `ChromeDriver` is added to system path or specified in code.
4. Run the `SauceDemoEndToEndTest.java` or `Automated_Test_Report.java` as a Java Application.

### Excel File Format (saucedemo.xlsx)
Ensure your Excel file has a sheet named `sheet1` and is structured like:

| Username        | Password      |
|----------------|---------------|
| standard_user  | secret_sauce  |
| locked_out_user| secret_sauce  |
| ...            | ...           |

---

## 📝 Notes

### Known Issues & Limitations

- **Hardcoded waits**: The framework uses `Thread.sleep()` which may cause flakiness. Use WebDriverWait for better stability.
- **Limited assertion logic**: Relies on basic checks; can be extended using test frameworks like TestNG or JUnit.
- **No external report integration**: Currently logs output and screenshots only via console and local file system.
- **Excel sheet must exist** at `./projects/saucedemo.xlsx`; invalid path will cause `FileNotFoundException`.

---

## 📸 Screenshots

Screenshots for failed test scenarios are saved under the `/screenshot/automatedreport<username>.png` path.

---

## ✅ Future Enhancements

- Integrate with TestNG for structured test management.
- Add WebDriverManager for dynamic driver handling.
- Replace `Thread.sleep()` with explicit waits.
- Generate HTML or PDF test reports using ExtentReports or Allure.
- Add cross-browser testing support.

---


