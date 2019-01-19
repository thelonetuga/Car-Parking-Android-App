Feature: As an autenticated user
  I want to access my dashboard
  So that i can see the spot, a map with the location of the park, a schema of the ocupated and non-ocupated spots, the profile button, the statistics button, the find my spot button, the leave my spot button, the available spots button, the more info button, the spinner of available parks and the data of the last update

  Background:
    Given I am logged in application

  Scenario: Opening the dashboard screen
    And I have a spot
    Then I can see the spot
    And a map with the location of the park
    And a schema of the ocupated and non-ocupated spots
    And the profile button
    And the statistics button
    And the find my spot button
    And the available spots button
    And the more info button
    And the spinner of available parks
    And the data of the last update

  Scenario: Looking for available spots
    And there are available spots
    Then I see pins in the map with the spots

  Scenario: Looking for available spots without spots available
    And there are not available spots
    Then I see an empty map
    And a message saying 'There are no available spots'

  Scenario: Logout
    Given I see the Authenticated Dashboard screen
    When I see the logout button
    And I click the logout button
    Then I see a message saying 'Thanks for using our app'
    And I see the guest Dashboard