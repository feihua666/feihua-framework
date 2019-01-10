package com.feihua.framework.spider;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Created by yangwei
 * Created at 2018/12/17 16:19
 */
public class WebDriverFactory {
    public static String driverType_firefox = "firefox";

    private String firefoxWebDrivePath;

    public WebDriver newWebDrive(String driverType) throws Exception {
        WebDriver webDriver = null;
        String _driverType = driverType;
        if(StringUtils.isEmpty(_driverType)){
            _driverType = driverType_firefox;
        }
        if(driverType_firefox.equals(_driverType)){
            System.setProperty("webdriver.gecko.driver", firefoxWebDrivePath);

            FirefoxBinary firefoxBinary = new FirefoxBinary();
            //firefoxBinary.addCommandLineOptions("--headless");

            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setBinary(firefoxBinary);
            firefoxOptions.setLogLevel(FirefoxDriverLogLevel.ERROR);
            //去掉css
            firefoxOptions.addPreference("permissions.default.stylesheet",2);
            //去掉图片
            firefoxOptions.addPreference("permissions.default.image", 2);
            //去掉flash
            firefoxOptions.addPreference("dom.ipc.plugins.enabled.libflashplayer.so",false);
            firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk","application/octet-stream");

            webDriver = new FirefoxDriver(firefoxOptions);

        }
        return webDriver;
    }

    public String getFirefoxWebDrivePath() {
        return firefoxWebDrivePath;
    }

    public void setFirefoxWebDrivePath(String firefoxWebDrivePath) {
        this.firefoxWebDrivePath = firefoxWebDrivePath;
    }
}
