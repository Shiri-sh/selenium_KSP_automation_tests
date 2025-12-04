package tests;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.CartPage;
import pages.CategoryPage;
import pages.ProductPage;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.FileOutputStream;

public class TestKSPshopping {
    public class Item {
        public String name;
        public int categoryIndex;
        public int productIndex;
        public int quantity;
        public int pricePerUnit;
        public int total;
        public Item(int quantity, int categoryIndex, int productIndex) {
            this.quantity = quantity;
            this.categoryIndex = categoryIndex;
            this.productIndex = productIndex;
        }
        public Item() {}
    }
    private List<Item> items = new ArrayList<>();

    {
        items.add(new Item(2, 0, 0));
        items.add(new Item(1, 1, 1));
        items.add(new Item(1, 2, 1));
    }
    private WebDriver driver;
    public static void main(String[] args){
        TestKSPshopping tests = new TestKSPshopping();
    }
    @BeforeMethod
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://ksp.co.il/web/world/5042");
    }
    public void takeScreenShots(String picName){
        try{
            File pic = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            Path dest = Paths.get(picName);
            Files.copy(pic.toPath(), dest);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void exportCartReportToExcel(List<WebElement> items, int displayedTotal,
                                        int calculatedTotal, boolean isTestPassed, CartPage cartPage) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Cart Report");

        int rowNum = 0;

        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("Product Name");
        header.createCell(1).setCellValue("Price");
        header.createCell(2).setCellValue("Quantity");
        header.createCell(3).setCellValue("Subtotal");

        for (WebElement item : items) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(cartPage.getProductName(item));
            row.createCell(1).setCellValue(cartPage.getPriceOfCartItem(item));
            row.createCell(2).setCellValue(cartPage.getProductQuantity(item));
            row.createCell(3).setCellValue(
                    cartPage.getPriceOfCartItem(item) * cartPage.getProductQuantity(item)
            );
        }

        // Adding summary
        Row summary1 = sheet.createRow(rowNum++);
        summary1.createCell(0).setCellValue("Displayed Total:");
        summary1.createCell(1).setCellValue(displayedTotal);

        Row summary2 = sheet.createRow(rowNum++);
        summary2.createCell(0).setCellValue("Calculated Total:");
        summary2.createCell(1).setCellValue(calculatedTotal);

        Row summary3 = sheet.createRow(rowNum++);
        summary3.createCell(0).setCellValue("Test Result:");
        summary3.createCell(1).setCellValue(isTestPassed ? "PASSED" : "FAILED");

        try (FileOutputStream fileOut = new FileOutputStream("CartReport.xlsx")) {
            workbook.write(fileOut);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItemIntoCart(Item item){

        CategoryPage categoryPage = new CategoryPage(driver);

        ProductPage productPage = categoryPage
                .getInCategoryByIndex(item.categoryIndex)
                .selectProduct(item.productIndex)
                .setQuantityForProduct(item.quantity);

        item.name = productPage.getName();
        item.pricePerUnit = productPage.getPrice();
        item.total = item.pricePerUnit * item.quantity;

        takeScreenShots("before addition.png");

        productPage.addToCart();

        takeScreenShots("after addition.png");
        Assert.assertTrue(productPage.addToCartPopUp());
    }
    @Test
    public void addItemsIntoCartTest(){
        for (Item i:items) {
            addItemIntoCart(i);
        }
        cartPriceAndDataValidation();
    }
    public void cartPriceAndDataValidation() {
        CartPage cartPage = new CartPage(driver);

        List<WebElement> cartElements = cartPage.getCartItems();
        List<Item> cartExtractedItems = new ArrayList<>();

        for (WebElement el : cartElements) {
            Item ci = new Item();
            ci.name = cartPage.getProductName(el);
            ci.pricePerUnit = cartPage.getPriceOfCartItem(el);
            ci.quantity = cartPage.getProductQuantity(el);
            ci.total = ci.pricePerUnit * ci.quantity;
            cartExtractedItems.add(ci);
        }

        // השוואת כמויות
        Assert.assertEquals(cartExtractedItems.size(), items.size());

        // השוואה על פי שם (כי סדר משתנה)
        for (Item expected : items) {
            Item found = cartExtractedItems.stream()
                    .filter(c -> c.name.contains(expected.name))
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull(found, "Missing item: " + expected.name);
            Assert.assertEquals(found.quantity, expected.quantity);
            Assert.assertEquals(found.pricePerUnit, expected.pricePerUnit);
        }

        // בדיקת מחיר כולל
        int cartDisplayed = cartPage.getCartTotal();
        int calc = cartExtractedItems.stream().mapToInt(i -> i.total).sum();

        Assert.assertEquals(cartDisplayed, calc);
    }
    // @AfterMethod
    public void tearDown(){
        if(driver!=null){
            driver.quit();
        }
    }
}
