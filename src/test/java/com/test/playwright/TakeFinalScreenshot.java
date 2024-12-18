package com.test.playwright;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import utils.ScreenShotManager;

public interface TakeFinalScreenshot {

    @AfterEach
    default void takeScreenshot(Page page){
        ScreenShotManager.takeScreenShot(page, "Final screenshot");
    }
}
