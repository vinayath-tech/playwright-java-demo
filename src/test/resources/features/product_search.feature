Feature: Product catalog

  As a Customer
  I want to easily search, filter and sort products in catalog
  So that I can find what I need quickly

  Scenario: Sally searches for adjustable wrench
      Given Sally is on home page
      When she searches for "Adjustable wrench"
      Then the "Adjustable Wrench" product should be displayed

  Scenario: Sally search for a more generic term
      Given Sally is on home page
      When she searches for "Saw"
      Then the following products are returned in results:
          | Product      |  Price   |
          | Wood Saw     |  $12.18  |
          | Circular Saw |  $80.19  |

  Scenario: Sally search for a product which is not available
      Given Sally is on home page
      When she searches for "XYZ"
      Then no product should be displayed
      And the message "There are no products found." should be displayed

  Scenario Outline: Sally should be able to sort products by various criteria
        Given Sally is on home page
        When she sorts products by "<Sort>"
        Then the first product to be displayed in results should be "<First Product>"

        Examples:
            | Sort               | First Product       |
            | Name (A - Z)       | Adjustable Wrench   |
            | Name (Z - A)       | Wood Saw            |
            | Price (High - Low) | Drawer Tool Cabinet |
            | Price (Low - High) | Washers             |

    @wip
    Scenario: Sally search for a product and filter by category
        Given Sally is on home page
        When she searches for "Saw"
        And she filters by category "Hand Saw"
        Then the following products are returned in results:
            | Product      |  Price   |
            | Wood Saw     |  $12.18  |