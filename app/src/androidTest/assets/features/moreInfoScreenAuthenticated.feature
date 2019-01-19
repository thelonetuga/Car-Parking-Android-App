Feature: As an autenticated user
    I want to access more info screen
    So that I can read all the information available

Background:
    Given I am loged
    And  I press the more info button

Scenario: Opening the more info screen
    Then I can see the more info screen


Scenario: I see the info
    Given I see the more info screen
    Then I see the product owner
    And the scrum master
    And the development team
    And the school
    And the class
    And the scholar year

Scenario: I press Cancel
    Given I see the more info screen
    When I press the cancel button
    Then I see Authenticated dashboard