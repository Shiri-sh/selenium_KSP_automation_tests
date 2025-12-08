package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import java.time.Duration;

public class RegisterPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By fnameField=By.cssSelector("input[id=':R337bl6:']");
    private By lnameField=By.cssSelector("input[id=':R537bl6:']");
    private By emailField=By.cssSelector("input[id=':R57bl6:']");
    private By phoneField=By.cssSelector("input[id=':R77bl6:']");
    private By fPassField=By.cssSelector("input[id=':R97bl6:']");
    private By sPassField=By.cssSelector("input[id=':Rb7bl6:']");
    private By submitButt=By.cssSelector("[type='submit']");
    private By helperText=By.cssSelector("p[id$=-helper-text]");
    public RegisterPage(WebDriver driver){
        this.driver=driver;
        this.wait=new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    public RegisterPage fillAll(
            String fname, String lname,
            String email, String phone,
            String pass1, String pass2
    ) {
        System.out.println("fill All");
        return fillFirstNameField(fname)
                .fillLastNameField(lname)
                .fillEmailField(email)
                .fillPhoneField(phone)
                .fillFirstPasswordField(pass1)
                .fillSecondPasswordField(pass2);
    }
    public RegisterPage fillFirstNameField(String fname){
        System.out.println("Typing: " + fname);
        wait.until(ExpectedConditions.visibilityOfElementLocated(fnameField)).sendKeys(fname);
        return this;
    }
    public RegisterPage fillLastNameField(String lname){
        System.out.println("Typing: " + lname);
        wait.until(ExpectedConditions.visibilityOfElementLocated(lnameField)).sendKeys(lname);

        return this;

    }
    public RegisterPage fillEmailField(String mail){
        System.out.println("Typing: " + mail);
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(mail);

        return this;
    }
    public RegisterPage fillPhoneField(String phone){
        System.out.println("Typing: " + phone);
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneField)).sendKeys(phone);

        return this;
    }
    public RegisterPage fillFirstPasswordField(String pass){
        System.out.println("Typing: " + pass);
        wait.until(ExpectedConditions.visibilityOfElementLocated(fPassField)).sendKeys(pass);

        return this;
    }
    public RegisterPage fillSecondPasswordField(String pass){
        System.out.println("Typing: " + pass);
        wait.until(ExpectedConditions.visibilityOfElementLocated(sPassField)).sendKeys(pass);

        return this;
    }
    public boolean textHelperDisplayed(){
        try{
            return wait.until(ExpectedConditions.visibilityOfElementLocated(helperText)).isDisplayed();

        }catch (Exception e){
            return false;
        }
    }
    public void register(){
        wait.until(ExpectedConditions.elementToBeClickable(submitButt)).click();
    }

}
