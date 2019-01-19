Feature: As an autenticated user
  I can move the car to a spot and add a new spot mannualy

  Background:
    Given I am logged in application

  Scenario: Found a spot manually
    Given i don't have a spot defined
    When i see the grid
    And i press an empty spot
    Then i see a message saying 'Spot added'

  Scenario: Trying to add a spot manually with one already have a 'My spot'
    Given i have a spot in 'My Spot'
    When i see the grid of my actual park
    And i press the empty spot
    Then i see a message saying 'You already have a spot!'