Feature: Add item to cart and verify price and subtotal

  Scenario: Adding a Monitor item in the cart and verifying subtotal
    Given I open Amazon website
    When I search for "Monitor"
    And I select the first item in the list
    And I add the item to cart
    And I open the cart
    Then I verify that the price is identical to the product page
    Then I close the browser


  Scenario: Adding a Laptop item in the cart and verifying subtotal
    Given I open Amazon website
    When I search for "Laptop"
    And I select the second item in the list
    And I add the item to cart
    And I open the cart
    Then I verify that the second product price is identical to the product page
    Then I close the browser

  Scenario: Adding two items in the cart and verifying subtotal
    Given I open Amazon website
    When I search for "Headphones"
    And I select the first item in the list
    And I add the item to cart
    When I switch to original window
    When I search for "Keyboard"
    And I select the first item in the list
    And I add the item to cart
    And I open the cart
    Then I verify each item's total price is correct
    And I verify that the subtotal is calculated correctly
    Then I close the browser
