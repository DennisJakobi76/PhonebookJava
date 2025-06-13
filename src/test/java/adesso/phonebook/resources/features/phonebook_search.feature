Feature: PhoneBook Search Operations

  Scenario: Filter phone book entries by first name
    When I search phone book entries with user input "an"
    Then the response should contain entries where first name contains "An"

  Scenario: Search phone book entries by phone prefix
    When I search phone book entries with user input "+49"
    Then the response should contain entries with phone prefix "+49"
