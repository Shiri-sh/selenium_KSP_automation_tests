package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.RegisterPage;

public class TestKSPRegister {
    private WebDriver driver;
    public static void main(String[] args){
        TestKSPRegister tests = new TestKSPRegister();
    }

    @BeforeMethod
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://auth.ksp.co.il/register?redirect_url=https%3A%2F%2Fksp.co.il%2Fweb%2Faccount");
    }
    @Test
    public void fillFormWithInvalidEmail(){
        RegisterPage registerPage=new RegisterPage(driver);
        registerPage.fillAll("shiri","shachor","shiri","0548483430","!Shiri123","!Shiri123");
        registerPage.register();
        Assert.assertTrue(registerPage.textHelperDisplayed());
    }
    @Test
    public void fillFormWithInvalidPhone(){
        RegisterPage registerPage=new RegisterPage(driver);
        registerPage.fillAll("shiri","shachor","shiril@gmail.com","0548483","!Shiri123","!Shiri123");

        registerPage.register();
        Assert.assertTrue(registerPage.textHelperDisplayed());
    }
    @Test
    public void fillFormWithNoFields(){
        RegisterPage registerPage=new RegisterPage(driver);

        registerPage.register();
        Assert.assertTrue(registerPage.textHelperDisplayed());
    }
    @Test
    public void fillFormWithWrongSecondPass(){
        RegisterPage registerPage=new RegisterPage(driver);
        registerPage.fillAll("shiri","shachor","shirim@gmail.com","0548483430","!Shiri1234","!Shiri123");
        registerPage.register();
        Assert.assertTrue(registerPage.textHelperDisplayed());
    }
    @Test
    public void fillFormWithInvalidFName(){
        RegisterPage registerPage=new RegisterPage(driver);
        registerPage.fillAll("s","shachor","shirik@gmail.com","0548483430","!Shiri123","!Shiri123");
        registerPage.register();
        Assert.assertTrue(registerPage.textHelperDisplayed());

    }
    @Test
    public void fillFormWithInvalidLName(){
        RegisterPage registerPage=new RegisterPage(driver);
        registerPage.fillAll("shiri","s","shirij@gmail.com","0548483430","!Shiri123","!Shiri123");
        registerPage.register();
        Assert.assertTrue(registerPage.textHelperDisplayed());
    }
    @Test
    public void fillFormWithValidData(){
        RegisterPage registerPage=new RegisterPage(driver);
        registerPage.fillAll("shiri","shachor","shirish@gmail.com","0548483430","!Shiri123","!Shiri123");
        registerPage.register();
        Assert.assertFalse(registerPage.textHelperDisplayed());
    }
    @AfterMethod
    public void tearDown(){
        if(driver!=null){
            driver.quit();
        }
    }
}
