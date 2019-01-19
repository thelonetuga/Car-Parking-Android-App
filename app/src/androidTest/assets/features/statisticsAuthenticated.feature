Feature: As an autenticated user
  I want to access statistics screen
  So that I can read all informations

  Scenario: Opening the statistics screen
    Given I am logged
    When  I press the statistics button
    Then I can see the statistics screen

#  Scenario: Opening rate statistics in general
#    Given I am logged
#    And  I press the statistics button
#    And I see the statistics screen
#    When I choose by rate
#    And I click general radio button
#    And I click ver button
#    Then I see general statistics in charts about the best and the worst spot with rate
#
#  Scenario: Opening rate statistics by park
#    Given I am logged
#    And  I press the statistics button
#    And I see the statistics screen
#    When I choose by rate
#    And I click park radio button
#    And I select one park
#    And I click ver button
#    Then I see by park statistics in charts about the best and the worst spot with rate
#
#  Scenario: Opening rate statistics between hours with start hour in wrong format
#    Given I am logged
#    And  I press the statistics button
#    And I see the statistics screen
#    When I choose by rate
#    And I click hours radio button
#    And I insert the start hour with wrong format
#    And I insert the end hour
#    And I click ver button
#    Then I see a message saying 'Please insert the hour correctly'
#
#  Scenario: Opening rate statistics between hours with end hour in wrong format
#    Given I am logged
#    And  I press the statistics button
#    And I see the statistics screen
#    When I choose by rate
#    And I click hours radio button
#    And I insert the start hour
#    And I insert the end hour with wrong format
#    And I click ver button
#    Then I see a message saying 'Please insert the hour correctly'
#
#  Scenario: Opening rate statistics between hours with start hour empty
#    Given I am logged
#    And  I press the statistics button
#    And I see the statistics screen
#    When I choose by rate
#    And I click hours radio button
#    And I don't insert the start hour
#    And I insert the end hour
#    And I click ver button
#    Then I see a message saying 'Please insert the start hour'
#
#  Scenario: Opening rate statistics between hours with end hour empty
#    Given I am logged
#    And  I press the statistics button
#    And I see the statistics screen
#    When I choose by rate
#    And I click hours radio button
#    And I insert the start hour
#    And I don't insert the end hour
#    And I click ver button
#    Then I see a message saying 'Please insert the end hour'

#  Scenario: Opening app statistics in general
#    Given I am logged
#    And  I press the statistics button
#    And I see the statistics screen
#    When I choose by app
#    And I click general radio button
#    And I click ver button
#    Then I see general statatistics in charts about the time that the app take to sugest a spot and the number of registed users 

  Scenario: Opening app statistics by park
    Given I am logged
    And  I press the statistics button
    And I see the statistics screen
    When I choose by app
    And I click park radio button
    And I select one park
    And I click ver button
    Then I see a message saying 'Este filtro não se aplica!'

  Scenario: Opening app statistics by hour
    Given I am logged
    And  I press the statistics button
    And I see the statistics screen
    When I choose by app
    And I click hours radio button
    And I insert the start hour
    And I insert the end hour
    And I click ver button
    Then I see a message saying 'Este filtro não se aplica!'