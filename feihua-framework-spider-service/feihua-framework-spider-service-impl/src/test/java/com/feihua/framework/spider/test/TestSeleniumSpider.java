package com.feihua.framework.spider.test;

import com.feihua.framework.spider.WebDriverFactory;
import com.feihua.framework.spider.impl.DefaultSpiderServiceImpl;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangwei
 * Created at 2018/12/15 17:24
 */
public class TestSeleniumSpider {

    private static final Logger logger = LoggerFactory.getLogger(TestSeleniumSpider.class);

    public static void main(String[] args) throws InterruptedException {
        String url = "https://tuchong.com";
        String domain = getDomain(url);
        System.out.println(domain);
        test1();
    }
    public static void test1() throws InterruptedException {


        WebDriver driver = null;
        try {
            WebDriverFactory webDriverFactory = new WebDriverFactory();
            webDriverFactory.setFirefoxWebDrivePath("D:\\program-files\\geckodriver-v0.23.0-win64\\geckodriver.exe");
            driver = webDriverFactory.newWebDrive(null);
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            driver.get("https://tuchong.com/rest/android/download");

            Thread.sleep(3000);
            Alert alert = driver.switchTo().alert();

            if (alert != null) {
                alert.dismiss();
            }
            driver.get("https://tuchong.com/info/service/");
        } catch (Exception e) {
            e.printStackTrace();            driver.get("https://tuchong.com/info/service/");

            System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }

        System.out.println("Page title is: " + driver.getTitle());

        WebElement myDynamicElement = (new WebDriverWait(driver, 3))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("html")));

        System.out.println(myDynamicElement.getText());
        System.out.println(myDynamicElement.toString());
        System.out.println(myDynamicElement.getAttribute("innerHTML"));
        System.out.println(myDynamicElement.getAttribute("outterHTML"));
        //driver.quit();
    }
    public static String getDomain(String url){
        URI uri = null;
        String r = null;
        try {
            uri = new URI(url);
            uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
            r =  uri.toString();
        } catch (URISyntaxException e) {
            logger.error("_getDomain error from url=" + url);
        }

        logger.info("spide domain=" + r);
        return r;
    }
}
