Feature: PhoneBook CRUD Operations

  Scenario: Get all phone book entries
    When I request all phone book entries
    Then the response should contain a list of entries

  Scenario: Get a phone book entry by existing ID
    Given a phone book entry with ID 1 exists
    When I request the phone book entry with ID 1
    Then the response should contain the entry with ID 1

  Scenario: Get a phone book entry by non-existing ID
    When I request the phone book entry with ID 999
    Then the response status should be 404 Not Found

  Scenario: Create a phone book entry
    When I create a phone book entry with first name "Test", last name "User", phone prefix "+49" and phone number "1234567890"
    Then the response should contain the created entry with first name "Test"

  Scenario: Update a phone book entry by existing ID
    Given a phone book entry with ID 100 exists
    When I update the phone book entry with ID 100 to have first name "Updated", last name "User", phone prefix "+49" and phone number "9876543210"
    Then the phone book entry with ID 100 should have first name "Updated"

  Scenario: Update a phone book entry by non-existing ID
    When I update the phone book entry with ID 999 to have first name "Test", last name "User", phone prefix "+49" and phone number "1234567890"
    Then the response status should be 404 Not Found

  Scenario: Delete a phone book entry by existing ID
    Given a phone book entry with first name "ToDelete", last name "User", phone prefix "+49" and phone number "1234567890" exists
    When I delete the phone book entry
    Then the phone book entry should no longer exist

  Scenario: Delete a phone book entry by non-existing ID
    When I delete the phone book entry with ID 1999
    Then the response status should be 404 Not Found
