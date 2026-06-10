# Royal Caribbean Alaska Cruise - Selenium Test Automation

## Project Overview

This project is an end-to-end automated test suite for the Royal Caribbean website, specifically targeting the Alaska Cruises section. It validates the core user journey from landing on the Alaska cruises page, searching for a specific ship, browsing cruise listings, applying filters, and verifying results.

---

## Tech Stack

| Tool | Version |
|------|---------|
| Java | 21 |
| Selenium WebDriver | 4.21.0 |
| TestNG | 7.x |
| ChromeDriver | 146.x |
| IDE | IntelliJ IDEA |
| Build Tool | Maven |

---

## Project Structure
Royal_Caribbean_Alaska/
├── src/
│   └── main/
│       └── java/
│           └── com/selenium/miniproject/
│               └── AlaskaCruiseTests.java
├── screenshots/
│   └── (auto-generated on test run)
├── testng.xml
├── pom.xml
└── README.md


---

## Test Cases

| Priority | Test Method | Description |
|----------|-------------|-------------|
| 1 | `openHomePage` | Navigates to Alaska Cruises landing page and verifies header loads |
| 2 | `searchShip` | Navigates to Rhapsody of the Seas ship page and verifies page title |
| 3 | `clickShopNow` | Clicks the Shop Now CTA button and verifies navigation to cruise listings |
| 4 | `applyFilters` | Applies departure, destination, duration and month filters on cruise search |
| 5 | `validateResults` | Validates that cruise results are visible on the page |

---

## Prerequisites

- Java JDK 21 installed
- Maven installed
- Google Chrome installed
- ChromeDriver matching your Chrome version
- IntelliJ IDEA (recommended)

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/Royal_Caribbean_Alaska.git
cd Royal_Caribbean_Alaska
```

### 2. Install Dependencies

```bash
mvn clean install
```

### 3. Configure Edge Driver (Optional)

If running on Edge browser, update the driver path in `AlaskaCruiseTests.java`:

```java
System.setProperty("webdriver.edge.driver", "YOUR_PATH\\msedgedriver.exe");
```

---

## Running Tests

### Run with Maven

```bash
mvn test
```

### Run with TestNG XML

```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run on a Specific Browser

```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

---

## testng.xml Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="RoyalCaribbean Alaska Cruises Suite">
    <test name="Alaska Cruise Tests">
        <parameter name="browser" value="chrome"/>
        <classes>
            <class name="com.selenium.miniproject.AlaskaCruiseTests"/>
        </classes>
    </test>
</suite>
```

---

## Screenshots

Screenshots are automatically captured after every test and saved in the `screenshots/` folder.

- **PASS** screenshots are saved as: `AlaskaCruiseTest_<testName>_PASS_<timestamp>.png`
- **FAIL** screenshots are saved as: `AlaskaCruiseTest_<testName>_FAIL_<timestamp>.png`

Example: