package pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class NavBar {

    private final Page page;

    public NavBar(Page page) {
        this.page = page;
    }

    @Step("Navigate to cart page")
    public void clickCart(){
        page.getByTestId("nav-cart").click();
    }

    @Step("Open the home page")
    public void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }
}
