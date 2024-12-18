package com.test.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class LoginWithRegisteredUserTest implements TakeFinalScreenshot {

//        Page page;

        @DisplayName("Login with valid credentials")
        @Test
        public void loginWithValidCreds(Page page) {

            User user = User.randomUser();
            UserApiClient userApiClient = new UserApiClient(page);
            userApiClient.registerUser(user);

            LoginPage loginPage = new LoginPage(page);
            loginPage.open();
            loginPage.loginAs(user);

            assertThat(loginPage.title()).isEqualTo("My account");
        }

    @DisplayName("Login with Invalid credentials")
    @Test
    public void loginWithInValidcreds(Page page) {

        User user = User.randomUser();
        UserApiClient userApiClient = new UserApiClient(page);
        userApiClient.registerUser(user);

        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user.withPassword("password"));

        assertThat(loginPage.errorMsg()).isEqualTo("Invalid email or password");
    }
}
