package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class CustomRetryAnalyser implements IRetryAnalyzer {
    int retry_count = 2;

    @Override
    public boolean retry(ITestResult result){
        if(retry_count>0){
            retry_count--;
            return true;
        }else{
            return false;
        }
    }
}
