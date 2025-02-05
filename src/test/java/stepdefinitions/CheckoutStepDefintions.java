package stepdefinitions;

import domain.CartLineItem;
import domain.ProductSummary;
import fixtures.PlaywrightCucumberFixtures;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.qameta.allure.internal.shadowed.jackson.databind.node.DoubleNode;
import org.assertj.core.api.Assertions;
import pages.CheckOut;
import pages.NavBar;
import pages.ProductDetails;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CheckoutStepDefintions {

    ProductDetails productDetails;
    NavBar navBar;
    CheckOut checkOut;

    @Before
    public void setPageObjects() throws IOException {
        productDetails = new ProductDetails(PlaywrightCucumberFixtures.getPage());
        navBar = new NavBar(PlaywrightCucumberFixtures.getPage());
        checkOut = new CheckOut(PlaywrightCucumberFixtures.getPage());
    }

    @Then("should be able to add {int} items to cart")
    public void shouldBeAbleToAddItemsToCart(int maxQuantity) {
        productDetails.increaseQuantity(maxQuantity);
        productDetails.addToCart();
        navBar.clickCart();
    }

    @DataTableType
    public CartLineItem expCartOutput(Map<String, String> productData) {
        return new CartLineItem(productData.get("name"),
                Integer.parseInt(productData.get("quantity")),
                Double.parseDouble(productData.get("price")),
                Double.parseDouble(productData.get("total")
        ));
    }

    @And("the total should be calculated correctly as below")
    public void theTotalShouldBeCalculatedCorrectlyAsBelow(List<CartLineItem> expectedCartLineItems) {
        List<CartLineItem> actualCartLineItems = checkOut.getCartLineItems();
        Assertions.assertThat(actualCartLineItems).containsAll(expectedCartLineItems);
    }

    @Then("I should be able to checkout the product successfully")
    public void iShouldBeAbleToCheckoutTheProductSuccessfully() {

        checkOut.proceedToCheckout();
        checkOut.verifySignedIn();
        checkOut.verifyCheckoutNavBar();
        checkOut.proceedToBilling();
        checkOut.verifyBillingDetails();
        checkOut.proceedToPayment();
        checkOut.performPayments();
    }
}
