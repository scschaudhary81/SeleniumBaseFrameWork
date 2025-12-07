package generics;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.devtools.Event;
import org.openqa.selenium.devtools.v138.network.Network;

import java.io.File;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class BaseTest extends BaseDriver {


    /**
     * Mouse Hover
     */
    public void mouse_hover(String xpath){
        this.get_actions()
                .moveToElement(this.find_element_by_xpath(xpath))
                .build()
                .perform();
    }


    /**
     * Drag And Drop Elements
     */
    public void drag_and_drop(String from,String to){
        this.get_actions()
                .clickAndHold(this.find_element_by_xpath(from))
                .moveToElement(this.find_element_by_xpath(to))
                .release()
                .build()
                .perform();
    }

    /**
     * Drag and drop element by location
     */
    public void drag_and_drop_location(String from,int x,int y){
        this.get_actions()
                .clickAndHold(this.find_element_by_xpath(from))
                .moveToLocation(x,y)
                .release()
                .build()
                .perform();
    }

    /**
    Find Element By Xpath
    * */
    public WebElement find_element_by_xpath(String xpath){
        return this.getDriver().findElement(By.xpath(xpath));
    }


    /**
     *
     * Scroll to element by actions class
     */
    public void scroll_to_element_action(String xpath){
        this.get_actions()
                .scrollToElement(this.find_element_by_xpath(xpath))
                .build()
                .perform();
    }


    /**
     *
     * Click By Actions Class
     */
    public void click_by_action(String xpath){
        this.get_actions().click(this.find_element_by_xpath(xpath)).build().perform();
    }

    /**
     *
     *  Double Click
     */
    public void double_click(String xpath){
        this.get_actions().doubleClick(this.find_element_by_xpath(xpath));
    }


    /**
     *
     * Right Click
     */
    public void right_click(String xpath){
        this.get_actions().contextClick(this.find_element_by_xpath(xpath)).build().perform();
    }

    /**
     *
     * Send Keys Via Actions Class
     */
    public void send_keys_action(String xpath,String input){
        this.get_actions().sendKeys(xpath,input).build().perform();
    }


    /**
     * Usage Of Chrome Devtools
     */


    public void start_network_recorder(){
        this.get_dev_tools().createSession();
        this.get_dev_tools().send(Network.enable(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty()));
    }

    public <T> void add_network_listener(Event<T> event, Consumer<T> consumer){
        this.get_dev_tools().addListener(event,consumer);
    }


    public void log_all_network_calls(){
        this.add_network_listener(Network.requestIntercepted(),(request)->{
            this.log_step_info(request.getRequest().getUrl());
        });
    }

    public void log_all_network_call_failure(){
        this.add_network_listener(Network.responseReceived(),(response)->{
            if(response.getResponse().getStatus() == StatusCode.INTERNAL_SERVER_ERROR.get()){
                this.log_step_error(response.getResponse().getUrl());
            }
        });
    }

    public void reset_network_listeners(){
        this.get_dev_tools().clearListeners();
    }

    public void stop_network_recoder(){
        this.get_dev_tools().clearListeners();
        this.get_dev_tools().close();
    }

    /**
     *
     * Usage Of Java Script Executor
     */


    public void scroll_to_element(String xpath){
        WebElement element = this.find_element_by_xpath(xpath);
        JavascriptExecutor js  = ((JavascriptExecutor) this.getDriver());
        js.executeScript("arguments[0].scrollIntoView(true);",element);
    }

    public void wait_for_page_load(){
        this.getWait().until((driver)->{
           JavascriptExecutor js = (JavascriptExecutor) driver;
           return Objects.equals(js.executeScript("return document.readyState"), "complete");
        });
    }

    public void click_by_js(String xpath){
        WebElement webElement = this.find_element_by_xpath(xpath);
        JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
        js.executeScript("arguments[0].click()",webElement);
    }

    /**
     * Window Management
     */

    public void create_new_tab(){
        this.getDriver().switchTo().newWindow(WindowType.TAB);
    }

    public String switch_to_new_tab(){
        String current_window = this.getDriver().getWindowHandle();
        for(String window : this.getDriver().getWindowHandles()){
            this.getDriver().switchTo().window(window);
        }
        return current_window;
    }


    public void maximize_window(){
        this.getDriver().manage().window().maximize();
    }

    public void minimize_window(){
        this.getDriver().manage().window().minimize();
    }

    public void full_screen_window(){
        this.getDriver().manage().window().fullscreen();
    }

    /**
     *
     * Alerts
     */

    public Alert switch_to_alert(){
        return this.getDriver().switchTo().alert();
    }

    public void accept_alert(){
        this.switch_to_alert().accept();
    }


    public void accept_alert(String message){
        Alert alert = this.switch_to_alert();
        alert.sendKeys(message);
        alert.accept();
    }

    public void dismiss_alert(){
        this.switch_to_alert().dismiss();
    }

    public String get_alert_message(){
        return this.switch_to_alert().getText();
    }


    /**
     * Cookies
     */


    public void delete_all_cookie(){
        this.getDriver().manage().deleteAllCookies();
    }

    public void delete_cookie_name(String name){
        this.getDriver().manage().deleteCookieNamed(name);
    }

    public Set<Cookie> get_all_cookies(){
        return this.getDriver().manage().getCookies();
    }

    public Cookie get_cookie_name(String name){
        return this.getDriver().manage().getCookieNamed(name);
    }


    public void add_cookie(Cookie cookie){
        this.getDriver().manage().addCookie(cookie);
    }

    /**
     * Implicit Wait
     */


    public void set_implicit_wait_in_seconds(int seconds){
        this.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));

        WebElement element;
        File base64 = new File("");
    }



    public boolean take_screenshot(String path){
        try{
            File from_file = ((TakesScreenshot)this.getDriver()).getScreenshotAs(OutputType.FILE);
            File to_file = new File(path);
            FileUtils.copyFile(from_file,to_file);
            return true;
        }catch (Exception e){
            this.log_step_error("Failed to save screenshot due to :" + e.getMessage());
            return false;
        }
    }








}
