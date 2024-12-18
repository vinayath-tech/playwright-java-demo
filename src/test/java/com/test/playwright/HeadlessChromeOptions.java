package com.test.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public class HeadlessChromeOptions implements OptionsFactory {

    Playwright playwright;
    Browser browser;
    BrowserContext browserContext;

    @Override
    public Options getOptions(){
            return new Options()
                    .setHeadless(false)
                    .setTestIdAttribute("data-test")
                    .setLaunchOptions(
                            new BrowserType.LaunchOptions()
                                    .setArgs(Arrays.asList("--no-sandbox", "--disable-gpu"))
                    );
    }

    @BeforeEach
    public void setUpBrowserContext() {
        browserContext = browser.newContext();
    }
}
