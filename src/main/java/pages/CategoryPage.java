package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;
public class CategoryPage {
     private WebDriver driver;
     private WebDriverWait wait;

    private By categoryList=By.cssSelector("div[class^='categories'] a");
    private By productsList=By.cssSelector("div[class^='products'] div");

    // Cart locators
    private By cartIcon = By.cssSelector("#cart-toggle");
    private By cartCount = By.cssSelector("#cart-count");

    public CategoryPage(WebDriver driver){
         this.driver=driver;
         this.wait=new WebDriverWait(driver,Duration.ofSeconds(15));
     }
     public CategoryPage getInCategoryByIndex(int category){
        for (int i=0;i<3;i++) {
            try {
                List<WebElement> categories = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(categoryList));
                ;
                categories.get(category).click();
                return this;
            } catch (Exception e) {

            }
        }
        throw new RuntimeException("Category list keep going stale");
     }
     public ProductPage selectProduct(int prodIndex){
        for(int i=0; i<3;i++) {
            try {
                List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productsList));
                WebElement product = products.get(prodIndex);

                wait.until(ExpectedConditions.elementToBeClickable(product));
                product.click();
                return new ProductPage(driver);
            } catch (Exception e) {

            }
        }
        throw new RuntimeException("Product list keeps going stale");
     }
}
