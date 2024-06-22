package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AmazonHomePage {
    WebDriver driver;

    private By searchBox = By.id("twotabsearchtextbox");
    private By searchButton = By.id("nav-search-submit-button");
    private By dismissButton = By.xpath("//input[@class='a-button-input' and @type='submit']");


    public AmazonHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterSearchTerm(String searchTerm) {
        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(searchTerm);
    }

    public void clickSearchButton() {
        //clickDismissButton();
        driver.findElement(searchButton).click();
    }

    public void clickDismissButton() {
        List<WebElement> elements = driver.findElements(dismissButton);
        if (!elements.isEmpty()) {
            elements.get(0).click();
        }
    }
}
