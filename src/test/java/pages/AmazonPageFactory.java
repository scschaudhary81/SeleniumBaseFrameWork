package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AmazonPageFactory {

    @FindBy(xpath = "//input[@id='twotabsearchtextbox']")
    public WebElement searchBoxHome;

    @FindBy(xpath = "//span[text()='Up to 5% back with Amazon Pay ICICI card']/ancestor::div[@class='a-section a-spacing-small a-spacing-top-small']//h2")
    public List<WebElement> get_all_five_percent_product;

    public AmazonPageFactory(WebDriver driver){
        PageFactory.initElements(driver,this);
    }
}
