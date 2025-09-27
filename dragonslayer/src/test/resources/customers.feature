Feature: Customers
  Test functionality of customers tab
  Note: For best testing, uncomment emptyDB in setup and teardown

  Scenario: Create, Edit, and Delete One Customer
    When I click on Customers tab
    When I add customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    When I edit customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-222-2222 | mSmith@yahoo.com  |          |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-222-2222 | mSmith@yahoo.com  |          |
    When I delete customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-222-2222 | mSmith@yahoo.com  |          |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |

  Scenario: Create, Edit, and Delete Multiple Customers
    When I click on Customers tab
    When I add customers
    |  firstName   |  lastName   |   phone      |  email            |  notes   |
    | May          | Lee         |              |                   |          |
    | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
      | May          | Lee         |              |                   |          |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    When I edit customers
      |  firstName   |  lastName   |   phone      |  email              |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@yahoo.com  | Weirdo   |
      | May          | Lee         | 123-456-7890 |                     |          |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com    | Stuff    |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email              |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@yahoo.com  | Weirdo   |
      | May          | Lee         | 123-456-7890 |                     |          |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com    | Stuff    |
    When I delete customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Zach         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
      | May          | Lee         |              |                   |          |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |

  Scenario: Creating Duplicate Customers
    When I click on Customers tab
    When I add customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | May          | Lee         |              |                   |          |
      | May          | Lee         |              |                   |          |
    Then I should see and close message: "Duplicate Customer Entry"
    When I update add customer with phone: "402-818-9203"
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | May          | Lee         | 402-818-9203 |                   |          |
      | May          | Lee         |              |                   |          |
    When I delete customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | May          | Lee         | 402-818-9203 |                   |          |
      | May          | Lee         |              |                   |          |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |

  Scenario: Search for Customers
    When I click on Customers tab
    When I add customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | May          | Lee         |              |                   |          |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
      | Matt         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Matt         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
      | May          | Lee         |              |                   |          |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    When I search customers: "Mark"
    Then I should only see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    When I delete customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Matt         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
      | May          | Lee         |              |                   |          |
    When I delete customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Matt         | Cart        | 581-237-9918 | zachCart@max.com  | Weirdo   |
      | May          | Lee         |              |                   |          |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |

  Scenario: Mark Customer Delinquent and Non-Delinquent
    When I click on Customers tab
    When I add customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    When I mark customer delinquent: "Mark"
    Then I should see delinquent on customer: "Mark"
    When I mark customer delinquent: "Mark"
    Then I should not see delinquent on customer: "Mark"
    When I delete customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-222-2222 | mSmith@yahoo.com  |          |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |

  Scenario: Create Invalid Customers
    When I click on Customers tab
    When I add customers
      |  firstName    |  lastName   |   phone       |  email              |  notes    |
      | Mark          | Smith       | 402-111-1111  | mSmith@yahoo.com    |           |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    When I add customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      |              |             | 111-222-3333 | silly@yahoo.com   |          |
    Then I should see and close message: "No Name Entered"
    When I update add customer with full name: "John Carter"
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | John         | Carter      | 111-222-3333 | silly@yahoo.com   |          |
      | Mark         | Smith       | 402-111-1111 | mSmith@yahoo.com  |          |
    When I delete customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |
      | John         | Carter      | 111-222-3333 | silly@yahoo.com   |          |
      | Mark         | Smith       | 402-222-2222 | mSmith@yahoo.com  |          |
    Then I should see customers
      |  firstName   |  lastName   |   phone      |  email            |  notes   |