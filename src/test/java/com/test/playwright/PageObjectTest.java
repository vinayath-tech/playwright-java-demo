package com.test.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import pages.*;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
@Feature("Search a product")
public class PageObjectTest implements TakeFinalScreenshot {

    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    CheckOut checkOut;

    @BeforeEach
    void setUp(Page page) {

        searchComponent =  new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkOut = new CheckOut(page);
    }

    @BeforeEach
    void setUpTrace(BrowserContext browserContext) {
        browserContext.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }

    @AfterEach
    void recordTrace(TestInfo testInfo, BrowserContext browserContext) {

        var traceName = testInfo.getDisplayName().replace(" ", "");
        browserContext.tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("target/traces/trace-" + traceName + ".zip"))
        );
    }

    @DisplayName("Checkout a product")
    @Story("Search and checkout for Pliers")
    @Test
    public void checkOutProductTest() {

        searchComponent.searchBy("Pliers");
        productList.viewProductDetails("Combination Pliers");
        productDetails.increaseQuantity();
        productDetails.addToCart();
        navBar.clickCart();

        List<CheckOut.CartLineItem> lineItems = checkOut.getLineItems();
        Assertions.assertThat(lineItems)
                .hasSize(1)
                .first()
                .satisfies(item -> {
                            Assertions.assertThat(item.title()).contains("Combination Pliers");
                            Assertions.assertThat(item.qty()).isEqualTo(4);
                            Assertions.assertThat(item.total()).isEqualTo(item.price() * item.qty());
                });
    }

}
