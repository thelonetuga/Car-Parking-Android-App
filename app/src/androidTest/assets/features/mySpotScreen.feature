Feature: As an autenticated user
  I want to access my spot screen and see my current spot
  I can leave my spot and on this moment i have the option to rate it and i can add it to my favorite spots

  Scenario: Looking for my spot
    Given I see authenticated dashboard screen
    When I have a current spot
    And I press the My Spot button
    Then I see the location in the map


  Scenario: Adding spot to My Favourite Spots
    Given I see authenticated dashboard screen
    When I have a current spot
    And I press the My Spot button
    Then I press the Leave My Spot button
    And i press the Add to Favourites checkBox


  Scenario: Cancel Button
    Given I see authenticated dashboard screen
    When I have a current spot
    And I press the My Spot button
    And I see the cancel button
    Then I press the cancel button
    And I go to autenticated dashboard

  Scenario: Leaving my spot whithout rating
    Given I see authenticated dashboard screen
    When I have a current spot
    And I press the My Spot button
    Then I press the Leave My Spot button
    And I dont rate the spot
    And i see a message saying 'Rate the spot please'

    Scenario: Leaving my spot
      Given I see authenticated dashboard screen
      When I have a current spot
      And I press the My Spot button
      Then I press the Leave My Spot button
      And I rate the spot
      Then I click on save button
      And I see a message saying 'Thanks for using this spot. Have a nice trip!'

  Scenario: I dont have a spot
    Given I see authenticated dashboard screen
    When I dont have a current spot
    And I press the My Spot button
    Then I see a message saying 'You dont have a saved spot'

