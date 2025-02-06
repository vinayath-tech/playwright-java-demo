Feature: Checkout a product

  As a Customer
  I want to be able to easily checkout a product
  So that I can buy it

  Background:
    Given I navigate to sauce demo login page
    When I submit valid credentials
    Then should be logged in successfully
    And I navigate to home page

  Scenario: Sally buys adjustable wrench through website
    Given Sally is on home page
    When she searches for "Adjustable wrench"
    Then the "Adjustable Wrench" product should be displayed
    When she navigates to "Adjustable Wrench" product details page
    Then should be able to add 3 items to cart
    And the total should be calculated correctly as below
      | name              | quantity | price  | total |
      | Adjustable Wrench | 3        | 20.33  | 60.99 |
    Then I should be able to checkout the product successfully