package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class SearchComponent {

    private final Page page;

    public SearchComponent(Page page) {
        this.page = page;
    }

    @Step("Search for a specific product")
    public void searchBy(String prodName) {
        page.navigate("https://practicesoftwaretesting.com/");
        page.getByPlaceholder("Search").fill(prodName);
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Search")).click();
    }

}
