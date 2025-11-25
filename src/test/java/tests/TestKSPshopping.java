package tests;
import java.io.IOException;
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

    public void openCategoryPageTest(){
        CategoryPage categoryPage=new CategoryPage(driver);
        categoryPage.getInCategoryByIndex(2);
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

    public void addItemIntoCart(int categoryIndex,int prodIndex,int quantity){

        CategoryPage categoryPage=new CategoryPage(driver);
        ProductPage productPage=categoryPage
                .getInCategoryByIndex(categoryIndex)
                .selectProduct(prodIndex)
                .setQuantityForProduct(quantity);

        int cartQuantityIconBeforeAdd=productPage.cartQuantityIcon();

        takeScreenShots("before addition.png");
        productPage.addToCart();
        takeScreenShots("after addition.png");

        Assert.assertTrue(productPage.addToCartPopUp());

        int cartQuantityIconAfterAdd= productPage.cartQuantityIcon();

        Assert.assertTrue(cartQuantityIconAfterAdd==cartQuantityIconBeforeAdd+1);
    }
    @Test
    public void addItemsIntoCartTest(){
        for (int i=0;i<3;i++){
            addItemIntoCart(i,i,i);
        }
        cartPriceCalculate();
    }
    public void cartPriceCalculate(){
        CartPage cartPage=new CartPage(driver);
        int cartTotalDisplay=cartPage.getCartTotal();
        int totalItemsPrice=0;
        List<WebElement> itemsList=cartPage.getCartItems();
        for(WebElement w:itemsList){
            totalItemsPrice+=cartPage.getPriceOfCartItem(w);
        }
        boolean testPassed=cartTotalDisplay==totalItemsPrice;
        Assert.assertTrue(testPassed);
        exportCartReportToExcel(itemsList,cartTotalDisplay,totalItemsPrice,testPassed,cartPage);

    }
    @AfterMethod
    public void tearDown(){
        if(driver!=null){
            driver.quit();
        }
    }
}
