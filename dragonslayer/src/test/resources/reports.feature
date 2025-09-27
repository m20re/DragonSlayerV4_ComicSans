Feature: Reports
  Test functionality of reports tab
  Note: For best testing, uncomment emptyDB in setup and teardown
  Note 2: Does not work currently due to db problems

  Scenario: Test Viewing Monthly Breakdown
    Given Customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
      | May          | Lee         |              |                   |          |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    Given Titles
      | Title     | Product Id  | Price       | Notes     | ID   |  Date Created  |
      | Superman  | 10          | 12.10       |  Stuff    | 1    |  01/10/2023    |
      | Batman    | 11          | 13.50       |           | 2    |  10/22/2022    |
      | Joker     | 13          | 11.00       |           | 3    |                |
      | Robin     | 15          | 16.10       |           | 4    |  06/15/2023    |
      | Nightwing | 18          | 19.50       |           | 5    |  09/10/2023    |
      | Flash     | 20          | 10.00       |           | 6    |                |
    Given Request Table for title: "Superman"
      | Last Name   | First Name    | Quantity  | Issue |
      | Cart        | Zach          | 3         |       |
      | Lee         | May           | 2         |       |
    When I click on Customers tab
    When I add customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Dan          | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    When I click on the Reports tab
    When I click on the Monthly Breakdown tab
    Then I should see Monthly Breakdown
    | numTitles | numCustomers | specialOrderNotes | issueNumberRequests | titlesNotFlagged | titlesNoRequests |
    |   6       |     4        |      1            |        1            |      4           |     5            |