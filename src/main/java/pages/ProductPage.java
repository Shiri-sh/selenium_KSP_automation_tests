package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By quantityIncrease = By.cssSelector("svg[aria-label='לחץ להגדלת כמות']");
    private By quantityDisplay = By.cssSelector(".quantity-0-3-461");
    private By addToCartButton = By.cssSelector("div[class^='addToCart'] button");
    private By cartQuantityIcon= By.cssSelector(".MuiBadge-badge.MuiBadge-standard.MuiBadge-anchorOriginTopRight.MuiBadge-anchorOriginTopRightRectangular.MuiBadge-overlapRectangular.MuiBadge-colorSecondary.muirtl-yg8mj6");
    private By popUpAddToCart=By.cssSelector("span[tabindex='0']");
    private By goToCart=By.cssSelector("[aria-label='עגלה']");
    private String popUpAddToCartText="המוצר התווסף בהצלחה לעגלת הקניות";
    public ProductPage(WebDriver driver){
        this.driver=driver;
        this.wait=new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("enter product page");

    }
    public ProductPage setQuantityForProduct(int quantity) {
        System.out.println("set quantity of product");

        for (int j = 1; j < quantity; j++) {
            driver.findElement(quantityIncrease).click();
        }
        return this;
    }
    public ProductPage addToCart(){
        System.out.println("add to cart");

        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        return this;
    }
    public int cartQuantityIcon(){
        WebElement quantityIcon= wait.until(ExpectedConditions.elementToBeClickable(cartQuantityIcon));
        return Integer.parseInt(quantityIcon.getText());
    }
    public boolean addToCartPopUp(){
        try {
            WebElement popUpShown=wait.until(ExpectedConditions.visibilityOfElementLocated(popUpAddToCart));
            return popUpShown.getText()==popUpAddToCartText;
        }catch (Exception e){
            return false;
        }
    }
    public CartPage goToCart(){
        wait.until(ExpectedConditions.elementToBeClickable(goToCart)).click();
        return new CartPage(driver);
    }
}
