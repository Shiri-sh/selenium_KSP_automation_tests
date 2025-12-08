package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v85.domstorage.model.Item;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.StaleElementReferenceException;
public class CategoryPage {
     private WebDriver driver;
     private WebDriverWait wait;

    private By categoryList=By.cssSelector("div[class^='categories'] a");
    private By productsList=By.cssSelector("div[class^='products'] div[class^='product']");
    private By productLinksList=By.cssSelector("a[class^='productTitle']");
    private By productLink=By.cssSelector("a[class^='productTitle']");

    private By cartIcon = By.cssSelector("#cart-toggle");
    private By cartCount = By.cssSelector("#cart-count");

    public CategoryPage(WebDriver driver){
         this.driver=driver;
         this.wait=new WebDriverWait(driver,Duration.ofSeconds(15));
     }
     public CategoryPage getInCategoryByIndex(int category){
        // WebElement c=
                 wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(categoryList)).get(category).click();
//         wait.until(ExpectedConditions.stalenessOf(c));
//         c.click();
         System.out.println("enter category page");

        return this;
     }
    public ProductPage selectProduct(int prodIndex) {
        long stableRequired = 30_000; // 30 seconds
        long lastStableTime = System.currentTimeMillis();

        WebElement product = null;

        while (true) {
            try {
                List<WebElement> products =
                        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productLinksList));

                product = products.get(prodIndex);

                // ננסה לגעת בו – אם הוא stale נקבל שגיאה
                product.isDisplayed();

                // אם הגענו לפה – האלמנט יציב כרגע
                if (System.currentTimeMillis() - lastStableTime >= stableRequired) {
                    break; // יציב 30 שניות – אפשר לצאת
                }

            } catch (StaleElementReferenceException e) {
                // האלמנט נהיה stale – מאפסים את הזמן
                lastStableTime = System.currentTimeMillis();
            }

            // השהייה קצרה כדי לא לרוץ חזק מדי
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
        }

        // עכשיו האלמנט יציב 30 שניות ברצף – לוחצים
        wait.until(ExpectedConditions.elementToBeClickable(product)).click();

        return new ProductPage(driver);
    }
}
