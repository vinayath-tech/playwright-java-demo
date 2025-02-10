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
        //page.navigate("https://practicesoftwaretesting.com/");

        page.waitForResponse("**/products/search?**", () -> {
            page.getByPlaceholder("Search").fill(prodName);
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Search")).click();
            try {
                threadSleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void threadSleep(int i) throws InterruptedException {
        Thread.sleep(i);
    }

    public void sortBy(String sortCriteria){
        page.waitForResponse("**/products?sort=**", () -> {
            page.getByTestId("sort").selectOption(sortCriteria);
        });
    }

    public void filterByCategory(String category){
        page.waitForResponse("**/products?**by_category=**", () -> {
            page.getByText(category).click();
        });
    }

}
