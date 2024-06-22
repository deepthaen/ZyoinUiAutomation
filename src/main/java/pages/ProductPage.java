package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.List;

public class ProductPage {
    WebDriver driver;

    private By addToCartButton = By.xpath("//input[@name='submit.add-to-cart']");
    private By productPrice = By.cssSelector("#priceblock_ourprice, #priceblock_dealprice, #priceblock_saleprice");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addToCart(){
        List<WebElement> list = driver.findElements(addToCartButton);
        if(list.size()>1){
            list.get(1).click();
        }else {
            WebElement addToCart = WaitUtils.waitForElementToBeClickable(driver, addToCartButton);
            addToCart.click();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getProductPrice() {
        return driver.findElement(productPrice).getText();
    }
}
