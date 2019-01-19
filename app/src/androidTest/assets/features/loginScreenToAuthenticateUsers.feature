Feature: Login screen to authenticate users
  As a user
  I want to enter a email and a password
  So that I can login in the application

  Scenario: Invalid email and password
    Given I see an empty login form
    When introduce an invalid email
    And I introduce an invalid password
    And I press the login button
    Then I see an error message saying 'Invalid credentials!'

  Scenario: Invalid password
    Given I see an empty login form
    When introduce a valid email
    And I introduce an invalid password
    And I press the login button
    Then I see an error message saying 'Invalid credentials!'

  Scenario: Invalid email
    Given I see an empty login form
    When introduce an invalid email
    And I introduce a valid password
    And I press the login button
    Then I see an error message saying 'Invalid credentials!'

  Scenario: Invalid email format
    Given I see an empty login form
    When introduce an invalid email format
    And I introduce a valid password
    And I press the login button
    Then I see an error message saying 'Please introduce a valid email. e.g.: xxx@xxx.xx'

  Scenario: Valid email and password
    Given I see an empty login form
    When introduce a valid email
    And I introduce a valid password
    And I press the login button
    Then I see a message saying 'Welcome to Spots'
    And I see the dashboard screen

  Scenario: Keep me signed
    Given I see an empty login form
    When introduce a valid email
    And I introduce a valid password
    And I press the keep me signed in button
    And I press the login button
    Then I see a message saying 'Welcome to Spots'
    And I see the dashboard screen