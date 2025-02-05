Feature: Login to application

  Scenario: Valid login
    Given I navigate to sauce demo login page
    When I submit valid credentials
    Then should be logged in successfully

  Scenario: In-Valid login
    Given I navigate to sauce demo login page
    When I submit In-valid credentials
    Then should see an error message
