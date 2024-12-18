package com.test.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class SimplePWTest implements TakeFinalScreenshot{

//    static Playwright playwright;
//    Page page;

    @Test
    public void shouldSeePageTitle(Page page) {

        page.navigate("https://practicesoftwaretesting.com/");
        String title = page.title();

        Assertions.assertTrue(title.contains("Practice Software Testing - Toolshop"));
    }

    @Test
    public void shouldSearchByKeyword(Page page) {

        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder='Search']").fill("Pliers");
        page.click("[data-test='search-submit']");

        int totSearchResults = page.locator(".container .card").count();

        Assertions.assertTrue(totSearchResults > 0);
    }

    @DisplayName("Search for pliers")
    @Test
    public void searchForPliers(Page page) {

        page.navigate("https://practicesoftwaretesting.com/");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Search")).click();

        assertThat(page.locator(".card")).hasCount(4);

       List<String> searchRes = page.getByTestId("product-name").allTextContents();
       assertThat(searchRes).allMatch(res -> res.contains("Pliers"));

        Locator outOfStockItem = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");

        assertThat(outOfStockItem).hasCount(1);
        assertThat(outOfStockItem).hasText("Long Nose Pliers");
    }

    @DisplayName("Complete Contact form")
    @Test
    public void completeContactForm(Page page){

        page.navigate("https://practicesoftwaretesting.com/contact");

        var firstNameField = page.getByLabel("First name");
        var lastNameField = page.getByLabel("Last name");
        var emailField = page.getByLabel("Email address");
        var msgField = page.getByLabel("Message");
        var subField = page.getByLabel("Subject");

        firstNameField.fill("gokul");
        lastNameField.fill("sri");
        emailField.fill("test@test.com");
        msgField.fill("Another test");
        subField.selectOption("Warranty");

        assertThat(firstNameField).hasValue("gokul");
        assertThat(lastNameField).hasValue("sri");
        assertThat(emailField).hasValue("test@test.com");
        assertThat(msgField).hasValue("Another test");
        assertThat(subField).hasValue("warranty");
    }

    @DisplayName("Mandatory fields verification")
    @ParameterizedTest
    @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
    public void mandatoryFields(String fieldName, Page page) {

        page.navigate("https://practicesoftwaretesting.com/contact");

        var firstNameField = page.getByLabel("First name");
        var lastNameField = page.getByLabel("Last name");
        var emailField = page.getByLabel("Email address");
        var msgField = page.getByLabel("Message");
        var subField = page.getByLabel("Subject");
        var sendField = page.getByText("Send");

        //Enter values
        firstNameField.fill("gokul");
        lastNameField.fill("sri");
        emailField.fill("test@test.com");
        msgField.fill("Another test");
        subField.selectOption("Warranty");

        //Clear one field
        page.getByLabel(fieldName).clear();

        sendField.click();

        //Check error message
        var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");

        assertThat(errorMessage).isVisible();
    }

    @Disabled("This test is disabled due to flakiness")
    @DisplayName("check product price")
    @Test
    public void checkProdPrice(Page page){

        page.navigate("https://practicesoftwaretesting.com/");

        List<Double> prices = page.getByTestId("product-price")
                .allTextContents()
                .stream()
                .map(price -> Double.parseDouble(price.replace("$","")))
                .toList();

        assertThat(prices).isNotEmpty()
                .allMatch(price -> price > 0)
                .doesNotContain(0.0)
                .allMatch(price -> price < 1000);
    }

    @Disabled("This test is disabled due to flakiness")
    @DisplayName("Wait for elements to appear")
    @Test
    public void showAllProducts(Page page) {

        page.navigate("https://practicesoftwaretesting.com/");

        List<String> prodElements = page.getByTestId("product-name").allInnerTexts()
                .stream()
                .toList();

        assertThat(prodElements).contains("Pliers", "Bolt Cutters", "Long Nose Pliers");

        List<String> prodImg = page.locator(".card-img-top").all()
                .stream()
                .map(img -> img.getAttribute("alt"))
                .toList();

        assertThat(prodElements).contains("Pliers", "Bolt Cutters", "Long Nose Pliers");
    }

    @Test
    public void sortPriceByHighToLow(Page page) {

        page.navigate("https://practicesoftwaretesting.com/");

        page.waitForResponse("**/products?sort**",
                () -> {
                    page.getByTestId("sort").selectOption("Price (High - Low)");
                }
        );

        List<Double> prodPrice = page.getByTestId("product-price")
                .allInnerTexts()
                .stream()
                .map(SimplePWTest::extractPrice)
                .toList();

        assertThat(prodPrice).isNotEmpty()
                        .isSortedAccordingTo(Comparator.reverseOrder());
    }

    public static Double extractPrice(String price){
        return Double.parseDouble(price.replace("$", ""));
    }

    @DisplayName("Mock search feature returning valid result")
    @Test
    public void searchForSingleProd(Page page){

       // products/search?q=Pliers
        page.route("**/products/search?q=Pliers", route ->
        {
            route.fulfill(
                    new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY)
                            .setStatus(200)
            );
        });

        page.navigate("https://practicesoftwaretesting.com/");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Search")).click();

        assertThat(page.getByTestId("product-name")).hasCount(1);
        assertThat(page.getByTestId("product-name")).hasText("Super Pliers");

    }

    @DisplayName("Mock search feature returning no result")
    @Test
    public void searchForNoResults(Page page) {

        page.route("**/products/search?q=Pliers", route ->
        {
            route.fulfill(
                    new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_NO_ENTRIES)
                            .setStatus(200)
            );
        });

        page.navigate("https://practicesoftwaretesting.com/");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Search")).click();

        assertThat(page.getByTestId("product-name")).hasCount(0);
        assertThat(page.getByTestId("search_completed")).hasText("There are no products found.");
    }


//    @Nested
//    @UsePlaywright(HeadlessChromeOptions.class)
//    class MakeApiCalls {
//
//        static Playwright playwright;
//
//        record Product(String name, Double price) {}
//
//        private static APIRequestContext requestContext;
//
//        @BeforeAll
//        public static void setUpRequestContext() {
//
//            requestContext = Playwright.create().request().newContext(
//                                new APIRequest.NewContextOptions()
//                                        .setBaseURL("https://api.practicesoftwaretesting.com/")
//                                        .setExtraHTTPHeaders(new HashMap<>() {{
//                                            put("Accept", "application/json");
//                                        }}
//                                )
//            );
//        }
//
//        @DisplayName("Test inherited with api calls")
//        @ParameterizedTest(name = "checking product {0}")
//        @MethodSource("products")
//        public void checkProducts(Product product, Page page) {
//
//            page.navigate("https://practicesoftwaretesting.com/");
//            page.getByPlaceholder("Search").fill(product.name);
//            page.getByRole(AriaRole.BUTTON,
//                    new Page.GetByRoleOptions().setName("Search")).click();
//
//
//            Locator productCard = page.locator(".card")
//                    .filter(
//                            new Locator.FilterOptions()
//                                    .setHasText(product.name)
//                                    .setHasText(Double.toString(product.price))
//                    );
//
//            assertThat(productCard).hasCount(1);
//            assertThat(productCard).isVisible();
//        }
//
//        static Stream<Product> products() {
//
//            APIResponse response =  requestContext.get("/products?page=1");
//            assertThat(response).isOK();
//
//            JsonObject jsonObject = new Gson().fromJson(response.text(), JsonObject.class);
//            JsonArray data = jsonObject.getAsJsonArray("data");
//
//            return data.asList()
//                    .stream()
//                    .map(jsonElement -> {
//                        JsonObject productJson = jsonElement.getAsJsonObject();
//                        return new Product(
//                                productJson.get("name").getAsString(),
//                                productJson.get("price").getAsDouble()
//                        );
//            });
//
//
//        }
//
//    }
}
