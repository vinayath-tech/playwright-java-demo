package stepdefinitions;

import fixtures.PlaywrightCucumberFixtures;
import domain.ProductSummary;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import pages.NavBar;
import pages.ProductList;
import pages.SearchComponent;

import java.util.List;
import java.util.Map;

public class ProductSearchStepDefinitions {

    NavBar navBar;
    SearchComponent searchComponent;
    ProductList productList;

    @Before
    public void setPageObjects() {
        navBar = new NavBar(PlaywrightCucumberFixtures.getPage());
        searchComponent = new SearchComponent(PlaywrightCucumberFixtures.getPage());
        productList = new ProductList(PlaywrightCucumberFixtures.getPage());
    }

    @Given("Sally is on home page")
    public void sally_is_on_home_page() {
        navBar.openHomePage();
    }

    @When("she searches for {string}")
    public void she_searches_for(String prodName) {
        searchComponent.searchBy(prodName);
    }

    @Then("the {string} product should be displayed")
    public void the_product_should_be_displayed(String prodName) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).contains(prodName);
    }

    @DataTableType
    public ProductSummary productSummaryRow(Map<String, String> productData) {
        return new ProductSummary(productData.get("Product"), productData.get("Price"));
    }

    @Then("the following products are returned in results:")
    public void theFollowingProductsAreReturnedInResults(List<ProductSummary> expectedProds) {
        List<ProductSummary> matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).containsAll(expectedProds);
    }


    @Then("no product should be displayed")
    public void no_product_should_be_displayed() {
        List<ProductSummary> matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).isEmpty();
    }

    @Then("the message {string} should be displayed")
    public void the_message_should_be_displayed(String expCompletionText) {
        String actCompletedText = productList.getSearchCompletedText();
        Assertions.assertThat(actCompletedText).isEqualTo(expCompletionText);
    }

    @When("she sorts products by {string}")
    public void sheSortsProductsBy(String sortFilter) {
        searchComponent.sortBy(sortFilter);
    }


    @Then("the first product to be displayed in results should be {string}")
    public void theFirstProductToBeDisplayedInResultsShouldBe(String firstProduct) {

        List<String> matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).startsWith(firstProduct);
    }

    @And("she filters by category {string}")
    public void sheFiltersByCategory(String filterCategory) {
        searchComponent.filterByCategory(filterCategory);
    }

    @And("I navigate to home page")
    public void iNavigateToHomePage() {
        navBar.openHomePage();
    }

    @And("she navigates to {string} product details page")
    public void sheNavigatesToProductDetailsPage(String prodName) {
        productList.viewProductDetails(prodName);
    }
}
