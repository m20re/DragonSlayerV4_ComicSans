Feature: Customers
  Test functionality of orders in the customer and titles tabs
  Note: For best testing, uncomment emptyDB in setup and teardown
  Note 2: Does not currently work due to selection problems

  Scenario: Create, Edit, and Delete a Customer Order
    When I click on Customers tab
    When I add customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    When I click on Titles tab
    When I add titles
      | Title     | Product Id  | Price      | Notes     |
      | Superman  | 10          | 12.00      |           |
    Then I should see titles
      | Title     | Product Id  | Price      | Notes     |
      | Superman  | 10          | 12.00      |           |
    When I click on Customers tab
    When I add requests for last name: "Cart"
      | Title     | Issue   | Quantity  |
      | Superman  |         | 2         |
    Then I should see orders for last name: "Cart"
      | Title     | Issue   | Quantity  |
      | Superman  |         | 2         |
    When I click on Titles tab
    Then I should see orders for title: "Superman"
      | Last Name   | First Name    | Quantity  | Issue |
      | Cart        | Zach          | 2         |       |
    When I click on Customers tab
    When I edit requests for last name: "Cart"
      | Title     | Issue   | Quantity  |
      | Superman  |         | 3         |
    Then I should see orders for last name: "Cart"
      | Title     | Issue   | Quantity  |
      | Superman  |         | 3         |
    When I delete requests for last name: "Cart"
      | Title     | Issue   | Quantity  |
      | Superman  |         | 3         |
    Then I should see orders for last name: "Cart"
      | Title     | Issue   | Quantity  |
    When I delete customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
    When I click on Titles tab
    When I delete titles
      | Title     | Product Id  | Price      | Notes     |
      | Superman  | 10          | 12.00      |           |
    Then I should see titles
      | Title     | Product Id  | Price   | Notes     |

  Scenario: Delete customer with active orders
    When I click on Customers tab
    When I add customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    When I click on Titles tab
    When I add titles
      | Title     | Product Id  | Price      | Notes     |
      | Superman  | 10          | 12.00      |           |
    Then I should see titles
      | Title     | Product Id  | Price      | Notes     |
      | Superman  | 10          | 12.00      |           |
    When I click on Customers tab
    When I add requests for last name: "Cart"
      | Title     | Issue   | Quantity  |
      | Superman  |         | 2         |
    Then I should see orders for last name: "Cart"
      | Title     | Issue   | Quantity  |
      | Superman  |         | 2         |
    When I click on Titles tab
    Then I should see orders for title: "Superman"
      | Last Name   | First Name    | Quantity  | Issue |
      | Cart        | Zach          | 2         |       |
    When I click on Customers tab
    When I delete customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
    When I click on Titles tab
    Then I should see orders for title: "Superman"
      | Last Name   | First Name    | Quantity  | Issue |
    When I delete titles
      | Title     | Product Id  | Price      | Notes     |
      | Superman  | 10          | 12.00      |           |
    Then I should see titles
      | Title     | Product Id  | Price   | Notes     |

  Scenario: Delete title with active orders
    When I click on Customers tab
    When I add customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    When I click on Titles tab
    When I add titles
      | Title     | Product Id  | Price      | Notes     |
      | Superman  | 10          | 12.00      |           |
    Then I should see titles
      | Title     | Product Id  | Price      | Notes     |
      | Superman  | 10          | 12.00      |           |
    When I click on Customers tab
    When I add requests for last name: "Cart"
      | Title     | Issue   | Quantity  |
      | Superman  |         | 2         |
    Then I should see orders for last name: "Cart"
      | Title     | Issue   | Quantity  |
      | Superman  |         | 2         |
    When I click on Titles tab
    Then I should see orders for title: "Superman"
      | Last Name   | First Name    | Quantity  | Issue |
      | Cart        | Zach          | 2         |       |
    When I delete titles
      | Title     | Product Id  | Price      | Notes     |
      | Superman  | 10          | 12.00      |           |
    Then I should see titles
      | Title     | Product Id  | Price   | Notes     |
    When I click on Customers tab
    Then I should see orders for last name: "Cart"
      | Title     | Issue   | Quantity  |
    When I delete customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |

  Scenario: Verify Consistent Orders for each Title - Only worked with emptyDB()
    Given Customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
      | May          | Lee         |              |                   |          |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    Given Titles
      | Title     | Product Id  | Price       | Notes     | ID   |  Date Created  |
      | Superman  | 10          | 12.10       |           | 1    |  01/10/2023    |
      | Batman    | 11          | 13.50       |           | 2    |  10/22/2022    |
      | Joker     | 13          | 11.00       |           | 3    |                |
      | Robin     | 15          | 16.10       |           | 4    |  06/15/2023    |
      | Nightwing | 18          | 19.50       |           | 5    |  09/10/2023    |
      | Flash     | 20          | 10.00       |           | 6    |                |
    Given Request Table for title: "Superman"
      | Last Name   | First Name    | Quantity  | Issue |
      | Cart        | Zach          | 3         |       |
      | Lee         | May           | 2         |       |
    Given Request Table for title: "Joker"
      | Last Name   | First Name    | Quantity  | Issue |
      | Smith       | Mark          | 1         |       |
    Given Request Table for title: "Nightwing"
      | Last Name   | First Name    | Quantity  | Issue |
      | Cart        | Zach          | 3         |       |
      | Smith       | Mark          | 1         |       |
    Given Request Table for title: "Flash"
      | Last Name   | First Name    | Quantity  | Issue |
      | Lee         | May           | 2         |       |
    When I click on Titles tab
    Then I should see orders for title: "Superman"
      | Last Name   | First Name    | Quantity  | Issue |
      | Cart        | Zach          | 3         |       |
      | Lee         | May           | 2         |       |
    Then I should see orders for title: "Joker"
      | Last Name   | First Name    | Quantity  | Issue |
      | Smith       | Mark          | 1         |       |
    Then I should see orders for title: "Nightwing"
      | Last Name   | First Name    | Quantity  | Issue |
      | Cart        | Zach          | 3         |       |
      | Smith       | Mark          | 1         |       |
    Then I should see orders for title: "Flash"
      | Last Name   | First Name    | Quantity  | Issue |
      | Lee         | May           | 2         |       |
