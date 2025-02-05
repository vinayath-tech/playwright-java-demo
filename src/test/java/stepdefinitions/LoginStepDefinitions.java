package stepdefinitions;

//import io.cucumber.java.Given;

import domain.LoginUser;
import fixtures.PlaywrightCucumberFixtures;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import pages.LoginPage;

public class LoginStepDefinitions {

    LoginPage loginPage;

    @Before
    public void setPageObjects() {
        loginPage = new LoginPage(PlaywrightCucumberFixtures.getPage());
    }


    @Given("I navigate to sauce demo login page")
    public void i_navigate_to_sauce_demo_login_page() {
        loginPage.open();

    }

    @When("I submit valid credentials")
    public void iSubmitValidCredentials() {
        loginPage.loginAs(new LoginUser("test@test.com", "S2per-secret"));
    }


    @Then("should be logged in successfully")
    public void shouldBeLoggedInSuccessfully() {
        Assertions.assertThat(loginPage.title()).isEqualTo("My account");
    }

    @When("I submit In-valid credentials")
    public void iSubmitInValidCredentials() {
        loginPage.loginAs(new LoginUser("user-not-exist@test.com", "invalid-password"));
    }

    @Then("should see an error message")
    public void shouldSeeAnErrorMessage() {
        Assertions.assertThat(loginPage.errorMsg()).isEqualTo("Invalid email or password");
    }
}
