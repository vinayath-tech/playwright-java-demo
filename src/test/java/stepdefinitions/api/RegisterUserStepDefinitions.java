package stepdefinitions.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import domain.LoginUser;
import domain.User;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.Json;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegisterUserStepDefinitions {

    private Playwright playwright;
    private APIRequestContext request;
    public User user;
    public APIResponse registerResponse, loginResponse, invalidRegisterResponse;
    public String username, password;
    private Gson gson;

    @Before
    public void setUp() {

        playwright = Playwright.create();
        gson = new Gson();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
                        .setExtraHTTPHeaders(headers));
    }

    @After
    public void tearDown() {
        if(request != null) {
            request.dispose();
            request = null;
        }
    }

    @Given("I have a valid user details and register a new user")
    public void iHaveAValidUserDetailsAndRegisterANewUser() {

        user = User.randomUser();
        registerResponse = request.post("/users/register",
                                        RequestOptions.create()
                                                .setData(user));
    }

    @Then("the user should be registered successfully")
    public void theUserShouldBeRegisteredSuccessfully() {


        JsonObject responseObject = gson.fromJson(registerResponse.text(), JsonObject.class);

        assertThat(registerResponse.status()).isEqualTo(201);
        assertThat(responseObject.get("first_name").getAsString()).isEqualTo(user.first_name());

        username = responseObject.get("email").getAsString();
        password = user.password();
    }


    @Then("I should be able to login with the new user")
    public void i_should_be_able_to_login_with_the_new_user() {

        LoginUser loginUser = new LoginUser(username, password);
        loginResponse = request.post("/users/login",
                RequestOptions.create()
                        .setData(loginUser));

        JsonObject responseObject = gson.fromJson(loginResponse.text(), JsonObject.class);

        assertThat(loginResponse.status()).isEqualTo(200);
        assertThat(responseObject.get("token_type").getAsString()).isEqualTo("bearer");
    }

    @Given("I register a new user without password")
    public void iRegisterANewUserWithoutPassword() {

        User invalidUser = User.noPasswordUser();
        invalidRegisterResponse = request.post("/users/register",
                RequestOptions.create()
                        .setData(invalidUser));

        assertThat(invalidRegisterResponse.status()).isEqualTo(422);
    }


    @Then("I should get below validation error as below")
    public void iShouldGetBelowValidationErrorAsBelow(DataTable expErrorMsg) {

        List<String> errorMessage = expErrorMsg.asList(String.class);

        JsonObject responseObject = gson.fromJson(invalidRegisterResponse.text(), JsonObject.class);
        assertThat(responseObject.get("password").getAsString()).isEqualTo(errorMessage.get(0));
    }
}
