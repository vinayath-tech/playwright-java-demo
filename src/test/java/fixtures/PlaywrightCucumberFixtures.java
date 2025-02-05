package fixtures;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;

import java.util.Arrays;

public class PlaywrightCucumberFixtures {

    protected static ThreadLocal<Playwright> playwright =
            ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            });

    protected static ThreadLocal<Browser> browser =
            ThreadLocal.withInitial(() ->
               playwright.get().chromium().launch(
                       new BrowserType.LaunchOptions().setHeadless(true)
                               .setArgs(Arrays.asList("--no-sandbox", "--disable-gpu"))
            ));

    protected static ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();
    protected static ThreadLocal<Page> page = new ThreadLocal<>();

    @Before(order = 100)
    public void setUpBrowserContext() {
        System.out.println("Set up code");
        browserContext.set(browser.get().newContext());
        page.set(browserContext.get().newPage());
    }

    @After
    public void tearDownBrowserContext() {
//        ScreenShotManager.takeScreenShot(page,"End of test");
        browserContext.get().close();
    }


    @AfterAll
    public static void tearDownBrowser() {
        System.out.println("Tear down code");
        browser.get().close();
        playwright.get().close();
    }

    public static Page getPage(){
        return page.get();
    }

    public static BrowserContext getBrowserContext(){
        return browserContext.get();
    }
}
