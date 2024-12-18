package com.test.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;
import domain.User;

public class UserApiClient {

    private final Page page;

    private static final String REGISTER_URI = "https://api.practicesoftwaretesting.com/users/register";

    public UserApiClient(Page page) {
        this.page = page;
    }

    public void registerUser(User user) {
       var response = page.request().post(
               REGISTER_URI,
               RequestOptions.create()
                       .setData(user)
                       .setHeader("Content-Type", " application/json")
                       .setHeader("Accept", "application/json"));
       if(response.status() != 201) {
           throw new IllegalStateException("Could not create user: " + response.text());
       }

    }
}
