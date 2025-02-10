package pages;

import com.microsoft.playwright.Page;
import domain.ProductSummary;
import io.qameta.allure.Step;

import java.util.List;

public class ProductList {

    private final Page page;

    public ProductList(Page page) {
        this.page = page;
    }

    @Step("Navigate to a product detail page")
    public void viewProductDetails(String prodName) {
        page.locator(".card").getByText(prodName).click();
    }

    public List<String> getProductNames() {
        return page.getByTestId("product-name").allInnerTexts();
    }

    public String getSearchCompletedText() {
        return page.getByTestId("search_completed").textContent();
    }

    public List<ProductSummary> getProductSummaries() {
       return page.locator(".card").all().stream()
               .map(productCard -> {
                    String name = productCard.getByTestId("product-name").textContent().strip();
                    String price = productCard.getByTestId("product-price").textContent();
                    return new ProductSummary(name, price);
               }).toList();
    }

}
