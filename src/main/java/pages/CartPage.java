package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.List;

public class CartPage {
    WebDriver driver;

    private By cartSubtotal = By.id("sc-subtotal-amount-activecart");
    private By individualPrice = By.id("(//div[@class='sc-badge-price'])");
    private By cartIcon = By.id("nav-cart-text-container");
    private By skipButton = By.xpath("//span[text()=' Skip ']/../..");
    private By close = By.id("attach-close_sideSheet-link");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getCartSubtotal() {
        return driver.findElement(cartSubtotal).getText().replaceAll("\\s+", "");
    }

    public void openCart() {
        clickSkipButton();
        WebElement cart= WaitUtils.waitForElementToBeClickable(driver,cartIcon);
        cart.click();
    }
    public String getIndividualPrice(int index) {
        return driver.findElement(By.xpath("(//div[@class='sc-badge-price'])["+index+"]/div/div")).getText();
    }

    public void clickSkipButton() {
        List<WebElement> closeButton = driver.findElements(close);
        List<WebElement> skip = driver.findElements(skipButton);

        if(!closeButton.isEmpty()){
            closeButton.get(0).click();
        } else if (!skip.isEmpty()) {
            skip.get(0).click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            driver.navigate().refresh();
        }
    }
}
