package com.test.playwright;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.ScreenShotManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public abstract class PlaywrightTest {

//    protected static ThreadLocal<Playwright> playwright =
//            ThreadLocal.withInitial(() -> {
//                Playwright playwright = Playwright.create();
//                playwright.selectors().setTestIdAttribute("data-test");
//                return playwright;
//            });

    protected static Playwright playwright;
    protected static Browser browser;


//    protected static ThreadLocal<Browser> browser =
//            ThreadLocal.withInitial(() ->
//               playwright.get().chromium().launch(
//                        new BrowserType.LaunchOptions().setHeadless(false)
//                                .setArgs(Arrays.asList("--no-sandbox", "--disable-gpu"))
//            ));



    protected BrowserContext browserContext;
    protected Page page;

    @BeforeAll
    public static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-gpu"))
        );
    }

    @BeforeEach
    public void setUpBrowserContext() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    public void tearDownBrowserContext() {
        ScreenShotManager.takeScreenShot(page,"End of test");
        browserContext.close();
    }

//    protected  void takeScreenShot(String name){
//        var screenshot = page.screenshot(
//                new Page.ScreenshotOptions().setFullPage(true)
//        );
//        Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
//    }

    @AfterAll
    public static void tearDownBrowser() {
        browser.close();
        playwright.close();
    }
}
