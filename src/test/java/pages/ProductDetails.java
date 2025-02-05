package pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.util.stream.IntStream;

public class ProductDetails {

    private final Page page;

    public ProductDetails(Page page) {
        this.page = page;
    }


    @Step("Increase the quantity of the product to be 4")
    public void increaseQuantity(int maxQuantity) {
        IntStream stream = IntStream.range(1, maxQuantity);

        stream.forEach((i) -> page.getByTestId("increase-quantity").click());
    }

    @Step("Add the product to cart")
    public void addToCart() {
        page.getByTestId("add-to-cart").click();
    }
}
