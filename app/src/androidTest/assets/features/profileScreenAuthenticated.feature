Feature: As an autenticated user
  I want to access profile screen
  So that I can read and edit my informations

  Background:
    Given I am loged
    And  I press the profile button

  Scenario: Opening the profile screen
    Then I can see the profile screen

  Scenario: I see the info
    Given I see the profile screen
    Then I see a input label with email
    And input label with old password
    And input label with password
    And input label with password confirmation
    And a checkbox with choosen preferences
    And my favorite Spots

  Scenario: Editing e-mail with wrong format
    Given I see a profile screen
    When introduce an invalid email format
    And I press the save button
    Then I see an error message saying 'Please insert a valid email. e.g.: xxx@xxx.xx'

  Scenario: Editing the password of my profile wrong
    Given I see a profile screen
    When introduce a new password
    And the new password confirmation different
    And I press the save button
    Then I see a message saying 'Please insert the same password'

  Scenario: Editing email with an email already used
    Given I see a profile screen
    When introduce an existing e-mail
    And I press the save button
    Then I see a message saying 'This e-mail already exists'

  Scenario: Editing password without confirmation
    Given I see a profile screen
    When introduce a new password
    And I don't introduce the new password confirmation
    And I press the save button
    Then I see a message saying 'Please insert password confirmation'

  Scenario: Editing password with wrong parameters
    Given I see a profile screen
    When introduce a new password with insufficient characters
    And the new password confirmation with insufficient characters
    And I press the save button
    Then I see a message saying 'Password with wrong format'

  Scenario: Deleting a favorite
    Given I see a profile screen
    When I click in a favorite spot
    Then I see a message saying 'Spot deleted!'

  Scenario: Editing the preferences
    Given I see a profile screen
    When I check a new prefference
    And I press the save button
    Then I see a message saying 'Profile updated!'

  Scenario: Editing the e-mail of my profile
    Given I see a profile screen
    When introduce a new e-mail
    And I press the save button
    Then I see a message saying 'Profile updated!'


  Scenario: Editing the password of my profile
    Given I see a profile screen
    When introduce a new password
    And the new password confirmation
    And I press the save button
    Then I see a message saying 'Profile updated!'

  Scenario: Logout
    Given I see a profile screen
    When I press the logout button
    Then I see a message saying 'Thanks for using our app'
    And I see the guest's dashboard