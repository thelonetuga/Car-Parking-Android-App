Feature: As a authenticated user
  I want to report an incident, indicate the type (wrong use of spot or natural incident) and upload a photo

  Scenario: Opening the Incident Report screen
    Given i see authenticated dashboard screen
    When i press the 'Incident' Report button
    Then I see the incident report screen

  Scenario: Reporting an incident
    Given i see authenticated dashboard screen
    When i press the 'Incident' Report button
    And i see an empty incident report form
    And i introduce a new incident
    And i press report button
    Then i see a message saying 'Thanks for your help'

  Scenario: Reporting an incident without description
    Given i see authenticated dashboard screen
    When i press the 'Incident' Report button
    And i see an empty incident report form
    And i don't write a description
    And i press report button
    Then i see a message saying 'Please insert a description'