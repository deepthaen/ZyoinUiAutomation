package steps;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.AmazonHomePage;
import pages.CartPage;
import pages.ProductPage;
import utils.ConfigReader;
import utils.WaitUtils;
import utils.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class AddItemToCartSteps {

    private WebDriver driver;
    private AmazonHomePage amazonHomePage;
    private ProductPage productPage;
    private CartPage cartPage;
    private String productPrice;
    private String firstProductPrice;
    private String secondProductPrice;
    private List<Integer> price = new ArrayList<>();
    private ConfigReader configReader = new ConfigReader();
    private static final Logger logger = LoggerFactory.getLogger(AddItemToCartSteps.class);

    @Before
    public void setUp() {
        driver = WebDriverManager.getDriver();
        amazonHomePage = new AmazonHomePage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
    }

    @Given("I open Amazon website")
    public void iOpenAmazonWebsite() {
        String url = configReader.getProperty("amazon.url");
        logger.info("Navigating to URL: " + url);
        driver.get(url);
        driver.manage().window().maximize();
        originalWindowHandle = driver.getWindowHandle();
    }

    @When("I search for {string}")
    public void iSearchFor(String item) {
        amazonHomePage.enterSearchTerm(item);
        amazonHomePage.clickSearchButton();
    }

    @And("I select the first item in the list")
    public void iSelectTheFirstItemInTheList() {
        firstProductPrice = selectItemFromList(1).replace(",","");
        price.add(Integer.parseInt(firstProductPrice));
        System.out.println(price);
    }

    @And("I add the item to cart")
    public void iAddTheItemToCart() {
        productPage.addToCart();
        //driver.navigate().refresh();
    }

    @And("I refresh page")
    public void iRefreshPage() {
        driver.navigate().refresh();
    }

    @And("I open the cart")
    public void iOpenTheCart() {
        cartPage.openCart();
        logger.info("Opened the cart");
    }

    @Then("I verify that the price is identical to the product page")
    public void iVerifyThatThePriceIsIdenticalToTheProductPage() {
        if(!firstProductPrice.contains(".")){
            firstProductPrice+=".00";
        }
        assertEquals(firstProductPrice, cartPage.getCartSubtotal().replace(",",""));
        logger.info("Verified that the price is identical to the product page");
    }

    @Then("I verify that the second product price is identical to the product page")
    public void iVerifyThatThesecondPriceIsIdenticalToTheProductPage() {
        if(!secondProductPrice.contains(".")){
            secondProductPrice+=".00";
        }
        assertEquals(secondProductPrice, cartPage.getCartSubtotal().replace(",",""));
        logger.info("Verified that the price is identical to the product page");
    }


    @Then("I verify each item's total price is correct")
    public void i_verify_each_item_s_total_price_is_correct() {
        for (int i = 0; i < price.size(); i++) {
            String individualPrice = cartPage.getIndividualPrice(i+1).replace(".00","").replaceAll("[^\\d]", "");
            int size = price.size()-i-1;
            int actual = price.get(size);
            assertEquals(actual,Integer.parseInt(individualPrice),"");
        }
    }

    @Then("I verify that the subtotal is calculated correctly")
    public void i_verify_that_the_subtotal_is_calculated_correctly() {
        int total=0;
        for (int i = 0; i < price.size(); i++) {
            total+= price.get(i);
        }
        String finalprice = cartPage.getCartSubtotal().replace(".00","").replaceAll("[^\\d]", "");
        assertEquals(total, Integer.parseInt(finalprice));
    }



    @And("I select the second item in the list")
    public void iSelectTheSecondItemInTheList() {
        secondProductPrice = selectItemFromList(2).replace(",","");
        price.add(Integer.parseInt(secondProductPrice));
    }

    @When("I switch to original window")
    public void i_switch_to_original_window() {

        if (newWindowHandle != null && !newWindowHandle.isEmpty()) {
            driver.close();
            driver.switchTo().window(originalWindowHandle);
            logger.info("Switched back to the original window: " + originalWindowHandle);
        }
    }

    private String selectItemFromList(int index) {
        WebElement firstItem = WaitUtils.waitForElementToBeClickable(driver,By.xpath("(//h2/a/span)["+index+"]"));
        firstItem.click();
        logger.info("Selected the first item in the list");
        switchToNewWindow();
        // Wait for the product price to be visible
        List<WebElement> list = driver.findElements(By.xpath("//span[text()='Without Exchange']/../../../../div[2]/div/span"));
        if(list.size()>0){
            productPrice = list.get(0).getText().replaceAll("\\s+", "").replace(".00","");
        }else {
            WebElement priceElement = WaitUtils.waitForElementToBeClickable(driver, By.xpath("//span[@class='a-price-whole']"));
            productPrice = priceElement.getText().replaceAll("\\s+", "");
        }
        logger.info("Product price: " + productPrice);
        return productPrice;
    }

    private String originalWindowHandle;
    private String newWindowHandle;

    private void switchToNewWindow() {
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(originalWindowHandle)) {
                driver.switchTo().window(window);
                newWindowHandle = window;
                logger.info("Switched to new window: " + window);
                break;
            }
        }
    }

    @Then("I close the browser")
    public void iCloseTheBrowser() {
        tearDown();
    }


    @After
    public void tearDown() {
        logger.info("Tear down: Closing the browser");
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        logger.info("Browser closed successfully");
    }
}
