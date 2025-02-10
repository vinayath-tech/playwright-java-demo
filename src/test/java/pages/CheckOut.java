package pages;

import com.microsoft.playwright.Page;
import domain.CartLineItem;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class CheckOut {

    private final Page page;
    private static JsonNode testData;

    public CheckOut(Page page) throws IOException {
        this.page = page;
        ObjectMapper objectMapper = new ObjectMapper();
        testData = objectMapper.readTree(new File("src/test/java/userData/testData.json"));
    }

    public List<CartLineItem> getCartLineItems() {
        page.locator("table tbody tr").waitFor();
        return page.locator("table tbody tr").all().stream()
                .map(row -> {
                    String name = trimmed(row.locator(".product-title").innerText());
                    int quantity = Integer.parseInt(row.locator("[data-test='product-quantity']").inputValue());
                    double price = Double.parseDouble(stripChar(row.locator("[data-test='product-price']").textContent()));
                    double total = Double.parseDouble(stripChar(row.locator("[data-test='line-price']").textContent()));
                    return new CartLineItem(name, quantity, price, total);
                }).toList();
    }

    public String stripChar(String value){
        return value.replace("$", "");
    }

    private String trimmed(String value) {
        return value.strip().replaceAll("\u00A0", "");
    }

    public void proceedToCheckout() {
        page.locator("[data-test='proceed-1']").click();
    }

    public void verifySignedIn() {
        String signInMsg = "Hello QA Tester, you are already logged in. You can proceed to checkout.";
        page.locator("div.login-form-1 p").waitFor();
        page.locator("div.login-form-1 p")
                .textContent()
                .contains(signInMsg);
    }

    public void proceedToBilling() {
        page.locator("[data-test='proceed-2']").click();
    }

    public void verifyCheckoutNavBar() {
        List<String> actNavItems = page.locator("ul.steps-indicator li a div.label").all().stream()
                .map(navItem -> navItem.textContent())
                .collect(Collectors.toList());

        List<String> expNavItems = List.of("Cart", "Sign in", "Billing Address", "Payment");

        Assertions.assertThat(actNavItems).containsAll(expNavItems);
    }

    public void verifyBillingDetails() {

        List<String> billingData = List.of("address", "city", "state", "country", "postcode");

        billingData.stream()
                .filter(data -> page.getByTestId(data).inputValue().isEmpty())
                .forEach(data -> page.getByTestId(data).fill(testData.get(data).asText()));
    }

    public void proceedToPayment() {
        page.locator("[data-test='proceed-3']").click();
    }

    public void performPayments() {
        page.getByTestId("payment-method").selectOption("Bank Transfer");
        assertThat(page.getByTestId("bank_name")).isVisible();

        page.getByTestId("bank_name").fill("Test bank");
        page.getByTestId("account_name").fill("Test account");
        page.getByTestId("account_number").fill("1234567890");
        page.getByTestId("finish").click();

        assertThat(page.locator(".help-block")).hasText("Payment was successful");
    }
}
