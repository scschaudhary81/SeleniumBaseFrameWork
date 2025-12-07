package tests;

import generics.BaseTest;
import org.testng.annotations.Test;

public class AmazonBuyTestCases extends BaseTest {



    @Test
    public void navigateToAmazon(){
        this.getDriver().get("https://www.amazon.in/");
    }
}
