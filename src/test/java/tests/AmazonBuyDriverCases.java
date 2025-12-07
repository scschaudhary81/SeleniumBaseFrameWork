package tests;

import generics.BaseDriver;
import generics.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AmazonPage;

import java.util.List;

public class AmazonBuyDriverCases extends BaseTest {

    AmazonPage page;

    @BeforeMethod
    public void before_method(){
        page = new AmazonPage(this.getDriver());
    }

    @Test(priority = -1)
    public void navigateToAmazon() throws InterruptedException {
        this.getDriver().get("https://www.amazon.in/");
        this.wait_for_page_load();
        page.searchBoxHome.sendKeys("Iphone 17");
        page.searchBoxHome.submit();
        page.get_all_five_percent_product.forEach((element)->{
            this.log_step_info(element.getText());
        });
        this.wait_for_page_load();
    }
}
