package pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class ProductList {

    private final Page page;

    public ProductList(Page page) {
        this.page = page;
    }

    @Step("Navigate to a product detail page")
    public void viewProductDetails(String prodName) {
        page.locator(".card").getByText(prodName).click();
    }
}
