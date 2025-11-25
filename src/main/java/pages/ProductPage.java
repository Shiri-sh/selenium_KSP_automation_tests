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
    private By quantityDecrease = By.cssSelector(".sign-0-3-460.sign-d6-0-3-589.null");
    private By quantityIncrease = By.cssSelector(".sign-0-3-460.sign-d6-0-3-589.disabled-0-3-462");
    private By quantityDisplay = By.cssSelector(".quantity-0-3-461");
    private By addToCartButton = By.cssSelector(".MuiButtonBase-root.MuiButton-root.MuiButton-contained.MuiButton-containedSecondary.MuiButton-sizeMedium.MuiButton-containedSizeMedium.MuiButton-colorSecondary.MuiButton-root.MuiButton-contained.MuiButton-containedSecondary.MuiButton-sizeMedium.MuiButton-containedSizeMedium.MuiButton-colorSecondary.muirtl-4w0j7a");
    private By cartQuantityIcon= By.cssSelector(".MuiBadge-badge.MuiBadge-standard.MuiBadge-anchorOriginTopRight.MuiBadge-anchorOriginTopRightRectangular.MuiBadge-overlapRectangular.MuiBadge-colorSecondary.muirtl-yg8mj6");
    private By popUpAddToCart=By.cssSelector("span[tabindex='0']");
    private By goToCart=By.cssSelector(".MuiButtonBase-root.MuiButton-root.MuiButton-text.MuiButton-textPrimary.MuiButton-sizeMedium.MuiButton-textSizeMedium.MuiButton-colorPrimary.MuiButton-root.MuiButton-text.MuiButton-textPrimary.MuiButton-sizeMedium.MuiButton-textSizeMedium.MuiButton-colorPrimary.button-0-3-82.muirtl-f9j014");
    private String popUpAddToCartText="המוצר התווסף בהצלחה לעגלת הקניות";
    public ProductPage(WebDriver driver){
        this.driver=driver;
        this.wait=new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public ProductPage setQuantityForProduct(int quantity) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(quantityDisplay));

        WebElement quantityText = driver.findElement(quantityDisplay);
        int currentQuantity = Integer.parseInt(quantityText.getText());

        while (currentQuantity < quantity) {
            driver.findElement(quantityIncrease).click();
            currentQuantity++;
        }

        while (currentQuantity > quantity) {
            driver.findElement(quantityDecrease).click();
            currentQuantity--;
        }
        return this;
    }
    public ProductPage addToCart(){
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
