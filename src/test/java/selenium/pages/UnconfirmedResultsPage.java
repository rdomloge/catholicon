package selenium.pages;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UnconfirmedResultsPage {

    private WebDriver driver;

    JavascriptExecutor js;

    private WebDriverWait wait;

    private Map<String, Object> vars;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\apps\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }
    
    @Test
    public void unconfirmedresults() {
        //Load the front page and navigate to the unconfirmed results page
        driver.get("http://rdomloge.entrydns.org:81/static/html/index.html");
        driver.manage().window().setSize(new Dimension(1296, 708));
        driver.findElement(By.linkText("Unconfirmed results")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".ng-scope:nth-child(1) > .ng-binding:nth-child(1)")));

        // Check the page layout
        assertThat(driver.findElement(By.cssSelector(".w3-xxlarge")).getText(), is("Unconfirmed results"));
        List<WebElement> elements = driver.findElements(By.cssSelector(".ng-scope:nth-child(1) > .ng-binding:nth-child(1)"));
        assert(elements.size() > 0);

        //Check the first line
        // Check the first column
        String text = driver.findElement(By.cssSelector(".ng-scope:nth-child(1) > .ng-binding:nth-child(1)")).getText();
        assertTrue("Cannot find matching unconfirmed result", text.matches("\\d\\d [A-Z][a-z]{2}"));

        //  Check the 2nd column
        
        driver.findElement(By.xpath("//td[4]/a")).click();
        driver.findElement(By.linkText("Unconfirmed results")).click();
    }
}
