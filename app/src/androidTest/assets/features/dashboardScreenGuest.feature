Feature: As guest
  I want to access my dashboard
  So that i can see the default park, the default park in the map, a schema of the ocupated and non-ocupated spots, the login button, the regist button and the data of last update

  Scenario: Opening the dashboard screen
    And I have default park
    Then I see the default park
    And a map with the location of the default park
    And a schema of the ocupated and non-ocupated spots
    And the login button
    And the regist button
    And the data of the last update

  Scenario: Opening empty dashboard
    And I have no default park
    Then I see a message saying 'You do not have a default park!'

  Scenario: Looking for available spots
    And there are available spots
    Then I see pins in the map with the spots

  Scenario: Looking for available spots without spots available
    And there are not available spots
    Then I see an empty map