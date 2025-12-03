package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;
public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By cartItems = By.cssSelector(".rtl-ydxmko div");
    private By cartTotal = By.cssSelector(".rtl-or331j");
    private By priceOfProduct=By.cssSelector(".rtl-1xvycce");
    private By nameOfProduct=By.cssSelector("");
    private By quantityOfProduct=By.cssSelector("");
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public List<WebElement> getCartItems() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItems));
    }
    public int getPriceOfCartItem(WebElement w){
        String textPrice=w.findElement(priceOfProduct).getText();
        return Integer.parseInt(textPrice);
    }
    public String getProductName(WebElement w){
        String name=w.findElement(nameOfProduct).getText();
        return name;
    }
    public int getProductQuantity(WebElement w){
        String quantity=w.findElement(quantityOfProduct).getText();
        return Integer.parseInt(quantity);
    }
    public int getCartItemCount() {
        return getCartItems().size();
    }

    public int getCartTotal() {
        WebElement totalElement = driver.findElement(cartTotal);
        return Integer.parseInt(totalElement.getText());
    }
    public boolean isCartEmpty() {
        try {
            WebElement emptyMessage = driver.findElement(By.xpath("//*[contains(text(), 'Cart is empty')]"));
            return emptyMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}
