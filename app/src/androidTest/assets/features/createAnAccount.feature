Feature: As guest
I want to create an account
So that i can login in the application

Background:
    Given I have opened the application

Scenario: Opening the register screen
    When I press the register button
    Then I see the register screen


Scenario: Invalid email and password
    Given I see an empty register form
    When introduce an invalid email
    And I introduce an invalid password one or two times
    And I press the regist button
    Then I see an error message saying 'Invalid credentials!'

Scenario: Invalid password
    Given I see an empty register form
    When introduce a valid email
    And I introduce two different passwords
    And I press the regist button
    Then I see an error message saying 'Please introduce the same password'

Scenario: Invalid email
    Given I see an empty register form
    When introduce an existing email
    And I introduce a valid password
    And I press the regist button
    Then I see an error message saying 'This email already exists'

Scenario: Valid email and password
    Given I see an empty register form
    When introduce a valid email
    And I introduce a valid password two times
    And I press the regist button
    Then I see a message saying 'Account created!'
    And I see the login screen


Scenario: Invalid email format
    Given I see an empty register form
    When introduce a wrong format email
    And I introduce a valid password
    And I press the regist button
    Then I see an error message saying 'Please insert a valid e-mail e.g.: xxx@xxx.xx'
