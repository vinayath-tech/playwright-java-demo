package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import domain.LoginUser;
import domain.User;
import io.github.cdimascio.dotenv.Dotenv;

public class LoginPage {

    private final Page page;
    public String baseUrl;

    public LoginPage(Page page) {

        this.page = page;
//        Dotenv dotenv = Dotenv.load();
//        baseUrl = dotenv.get("BASE_URL");
        baseUrl = "https://practicesoftwaretesting.com";
    }


    public void open() {
        page.navigate(baseUrl+"/auth/login");
    }

    public void loginAs(LoginUser loginUser) {
        page.getByPlaceholder("Your email").fill(loginUser.email());
        page.getByPlaceholder("Your password").fill(loginUser.password());
        page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Login")).click();
    }

    public String title(){
        return page.getByTestId("page-title").textContent();
    }

    public String errorMsg() {
        return page.getByTestId("login-error").textContent();
    }
}
