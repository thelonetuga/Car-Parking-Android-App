Feature: As a authenticated user 
  I want to see statistics data about the ranking about ocuppated and non ocuppated spots

  Scenario: Opening the statistics screen
    Given I see authenticated dashboard screen
    When I see a statistics button
    And I click on statististics button
    Then I see the statistics screen

  Scenario:  Opening statistics screen i choose see general raking statistics
    Given I see authenticated dashboard screen
    When I see a statistics button
    And I click on statististics button
    Then I choose by ranking of occupated
    And   I click general radio button
    And I see all general statistics charts about ranking of occupated

  Scenario: Opening statistics screen i choose see by hour raking statistics
    Given I see authenticated dashboard screen
    When I see a statistics button
    And I click on statististics button
    Then I choose by ranking of occupated
    And   I click by hour radio button
    And  I choose an interval time
    And I see statistics charts about ranking of occupated by hour


  Scenario: Opening statistics screen i choose see by Park choosen raking statistics
    Given I see authenticated dashboard screen
    When I see a statistics button
    And I click on statististics button
    Then I choose by ranking of occupated
    And   I click by Park radio button
    And   I choose the park on combobox
    And I see statistics charts about ranking of occupated by Park choosen