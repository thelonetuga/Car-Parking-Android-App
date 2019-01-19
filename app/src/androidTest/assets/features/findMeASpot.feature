Feature: As an autenticated user
  I want to find a spot and receive directions to it

  Background:
    Given i see authenticated dashboard screen

  Scenario: Click on find me a spot
    When i press the find me a spot button
    Then I see the find me a spot screen

  Scenario: No Empty Spot Available
    When theres no empty spots available
    And i press the find me a spot button
    Then i see a message saying 'There's no empty spots available in this park'

  Scenario: Get the path to selected Spot
    When i press the find me a spot button
    And there's one or more empty spots available
    And i press a spot button
    And i see the path to that spot
    Then i click cancel button
