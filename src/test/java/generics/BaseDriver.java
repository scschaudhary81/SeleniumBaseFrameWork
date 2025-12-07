package generics;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.annotations.*;
import java.io.FileInputStream;
import java.net.URI;
import java.time.Duration;
import java.util.Properties;


public class BaseDriver {
    



    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<FluentWait<WebDriver>> wait = new ThreadLocal<>();
    private final ThreadLocal<Actions> actions = new ThreadLocal<>();
    private final ThreadLocal<DevTools> devtools = new ThreadLocal<>();
    private final Properties properties  = new Properties();



    public WebDriver getDriver(){
        return driver.get();
    }

    public FluentWait<WebDriver> getWait(){
        return wait.get();
    }


    public void log_step_info(String s){
        System.out.println(s);
    }

    public void log_step_error(String s){
        System.out.println(s);
    }


    private void set_and_load_properties(){
        try{
            properties.load(new FileInputStream("Config.properties"));
        }catch (Exception e){
            this.log_step_error(String.format("Error in before test %s",e));
        }
    }

    public String getProperty(String name){
        return this.properties.getProperty(name);
    }

    public Actions get_actions(){
        return actions.get();
    }

    private void set_actions(){
          if(getDriver() != null) this.actions.set(new Actions(this.getDriver()));
          else this.log_step_error("Error: Not able to set actions object as driver is null");
    }


    private void set_dev_tools(){
        DevTools devTools = ((ChromeDriver)this.getDriver()).getDevTools();
        this.devtools.set(devTools);
    }

    public DevTools get_dev_tools(){
        return this.devtools.get();
    }



    private MutableCapabilities get_options_object(String browser_type) throws Exception{
        switch (browser_type.toUpperCase()){
            case "CHROME":
                ChromeOptions options =  new ChromeOptions();
                String chrome_arguments = this.getProperty("CHROME_ARGUMENTS");
                if(chrome_arguments != null && !chrome_arguments.isEmpty()){
                    for(String chrome_argument : chrome_arguments.split(",")){
                        options.addArguments(chrome_argument);
                    }
                }
                return options;
            default:
                throw new Exception("Error: Invalid or Unsupported Browser Type : " + browser_type);
        }
    }

    private void set_up_grid_driver(String browser_type) throws Exception {
        MutableCapabilities options = this.get_options_object(browser_type);
        this.log_step_info("Starting Grid Driver");
        String hub_port = System.getenv("SEL_HUB_PORT");
        WebDriver grid_driver = new RemoteWebDriver(new URI(String.format("http://127.0.0.1:%s",hub_port)).toURL(),options);
        this.driver.set(grid_driver);
    }

    private void setup_local_driver(String browser_type) throws Exception {
        this.log_step_info("Starting Driver Locally");
        MutableCapabilities options = this.get_options_object(browser_type);
        WebDriver local_driver;
        switch (browser_type.toUpperCase()){
            case "CHROME":
                local_driver = new ChromeDriver((ChromeOptions) options);
                this.driver.set(local_driver);
                break;
            default:
                throw new Exception("Error: Invalid or Unsupported Browser Type : " + browser_type);
        }
    }


    private void start_driver(String browser_type,String run_env) throws Exception{
        if(run_env != null && run_env.equalsIgnoreCase("grid")){
            this.set_up_grid_driver(browser_type);
        }else{
            this.setup_local_driver(browser_type);
        }
        if(this.getDriver() == null) throw new Exception("Error: Driver not found");
    }

    private void set_up_wait(){
        FluentWait<WebDriver> fluentWait = new FluentWait<>(this.getDriver())
                .withTimeout(Duration.ofSeconds(Integer.parseInt(this.getProperty("WAIT_TIMEOUT"))))
                .pollingEvery(Duration.ofSeconds(Integer.parseInt(this.getProperty("WAIT_POLLING"))));
        wait.set(fluentWait);
    }


    @BeforeTest
    public void before_test(){
        try{
            this.set_and_load_properties();
        }catch (Exception e){
            this.log_step_error(String.format("Error in before test %s",e));
        }
    }


    @Parameters({"BROWSER_TYPE","RUN_ENV"})
    @BeforeMethod
    public void before_method(@Optional String browser_type,@Optional String run_env){
        try{
            if(browser_type == null) browser_type = this.getProperty("BROWSER_TYPE");
            if(run_env == null) run_env = this.getProperty("RUN_ENV");
            this.start_driver(browser_type,run_env);
            this.set_up_wait();
            this.set_actions();
            this.set_dev_tools();
        }catch (Exception e){
            this.log_step_error(String.format("Failed to start driver due to %s",e));
        }
    }


    @AfterMethod
    public void stop_driver(){
        try{
            if(this.getDriver() != null){
                this.getDriver().quit();
                this.log_step_info("Stopped Chrome Driver Successfully");
            }else{
                this.log_step_error("Failed to stop driver as driver is set null");
            }
        }catch (Exception e){
            this.log_step_error(String.format("Failed to stop driver due to %s",e));
        }
    }
}
