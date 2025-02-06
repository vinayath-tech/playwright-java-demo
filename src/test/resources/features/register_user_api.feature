Feature: API - Register new user
    As a user
    I want to be able to register a new user via API
    So that I can login to the application

    Scenario: Successfully register a new user
        Given I have a valid user details and register a new user
        Then the user should be registered successfully
        And I should be able to login with the new user

    Scenario: Verify validation for password field
        Given I register a new user without password
        Then I should get below validation error as below
            | The password field is required. |
