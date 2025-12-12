package tests;

import generics.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AmazonPageFactory;

public class AmazonBuyDriverCases extends BaseTest {

    AmazonPageFactory page;

    @BeforeMethod
    public void before_method(){
        page = new AmazonPageFactory(this.getDriver());
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
