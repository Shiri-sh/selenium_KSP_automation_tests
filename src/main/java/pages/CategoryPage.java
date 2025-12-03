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
    private By productsList=By.cssSelector("div[class^='products'] div[class^='product']");
    private By productLink=By.cssSelector("a[class^='productTitle']");
    private By productName=By.cssSelector("h1[aria-label] span");
    private By productPrice=By.cssSelector("div[class^='currentPrice']");
    private By cartIcon = By.cssSelector("#cart-toggle");
    private By cartCount = By.cssSelector("#cart-count");

    public CategoryPage(WebDriver driver){
         this.driver=driver;
         this.wait=new WebDriverWait(driver,Duration.ofSeconds(15));
     }
     public CategoryPage getInCategoryByIndex(int category){
         //List<WebElement> categories =
         wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(categoryList)).get(category).click();
        //categories.get(category).click();
         System.out.println("enter category page");

        return this;
     }
     public ProductPage selectProduct(int prodIndex){
         System.out.println("enter selectProduct");

        //List<WebElement> products =
         wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productsList)).get(prodIndex).click();
       // WebElement product = products.get(prodIndex);

//        wait.until(ExpectedConditions.elementToBeClickable(product));
//        product.click();
        return new ProductPage(driver);
     }
}
