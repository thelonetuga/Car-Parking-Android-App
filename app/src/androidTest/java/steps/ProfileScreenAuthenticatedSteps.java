package steps;

import com.example.ecko.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static android.support.test.espresso.action.ViewActions.click;

public class ProfileScreenAuthenticatedSteps extends GreenCoffeeSteps {

    @Given("^I am loged$")
    public void i_am_loged() {
        onViewWithText(R.string.OK).click();
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }

    @Given("^I press the profile button$")
    public void i_press_the_profile_button() {
        onViewWithId(R.id.btnProfile).click();
    }

    @Then("^I can see the profile screen$")
    public void i_can_see_the_profile_screen() {
        onViewWithId(R.id.profile).isDisplayed();
    }

    @Given("^I see the profile screen$")
    public void i_see_the_profile_screen() {
        onViewWithId(R.id.profile).isDisplayed();
    }

    @Then("^I see a input label with email$")
    public void i_see_a_input_label_with_email() {
        onViewWithId(R.id.inputMyEmail).isDisplayed();
    }

    @Then("^input label with old password$")
    public void input_label_with_old_password() {
        onViewWithId(R.id.inputOldPassword).isDisplayed();
    }

    @Then("^input label with password$")
    public void input_label_with_password() {
        onViewWithId(R.id.inputNewPassword).isDisplayed();
    }

    @Then("^input label with password confirmation$")
    public void input_label_with_password_confirmation() {
        onViewWithId(R.id.inputNewPasswordConfirmation).isDisplayed();
    }

    @Then("^a checkbox with choosen preferences$")
    public void a_checkbox_with_choosen_preferences() {
        onViewWithId(R.id.checkboxPreferences).isDisplayed();
    }

    @Then("^my favorite Spots$")
    public void my_favorite_Spots() {
        //onViewWithId(R.id.favouriteSpots).isDisplayed();
        onViewWithId(R.id.favouriteSpots).isDisplayed();
    }

    @Given("^I see a profile screen$")
    public void i_see_a_profile_screen() {
        onViewWithId(R.id.profile).isDisplayed();
    }

    @When("^introduce an invalid email format$")
    public void introduce_an_invalid_email_format() {
        onViewWithId(R.id.inputMyEmail).clearText();
        onViewWithId(R.id.inputMyEmail).type("invalid.com");
        closeKeyboard();
    }

    @When("^I press the save button$")
    public void i_press_the_save_button() {
        waitFor(3000);
        onViewWithId(R.id.saveProfile).click();
    }

    @Then("^I see an error message saying 'Please insert a valid email\\. e\\.g\\.: xxx@xxx\\.xx'$")
    public void i_see_an_error_message_saying_Please_insert_a_valid_email_e_g_xxx_xxx_xx() {
        onViewWithText(R.string.wrongEmailFormat).isDisplayed();
    }

    @When("^introduce a new e-mail$")
    public void introduce_a_new_e_mail() {
        onViewWithId(R.id.inputMyEmail).clearText();
        onViewWithId(R.id.inputMyEmail).type("novoteste@mail.com");
        closeKeyboard();
    }

    @Then("^I see a message saying 'Profile updated!'$")
    public void i_see_a_message_saying_Profile_updated() {
        waitFor(2000);
        onViewWithText(R.string.profileUpdated).isDisplayed();
        onViewWithText(R.string.OK).click();
    }

    @When("^introduce a new password$")
    public void introduce_a_new_password() {
        onViewWithId(R.id.inputNewPassword).type("newpass123");
        closeKeyboard();
    }

    @When("^the new password confirmation$")
    public void the_new_password_confirmation() {
        onViewWithId(R.id.inputNewPasswordConfirmation).type("newpass123");
        closeKeyboard();
    }

    @When("^the new password confirmation different$")
    public void the_new_password_confirmation_different() {
        onViewWithId(R.id.inputNewPasswordConfirmation).type("algodiferente");
        closeKeyboard();
    }

    @Then("^I see a message saying 'Please insert the same password'$")
    public void i_see_a_message_saying_Please_insert_the_same_password() {
        onViewWithText(R.string.insertSamePass).isDisplayed();
    }

    @When("^I check a new prefference$")
    public void i_check_a_new_prefference() {
        onViewWithId(R.id.favs).click();
    }

    @When("^I press the logout button$")
    public void i_press_the_logout_button() {
        closeKeyboard();
        onViewWithId(R.id.logout).click();
    }

    @Then("^I see a message saying 'Thanks for using our app'$")
    public void i_see_a_message_saying_Thanks_for_using_our_app() {
        onViewWithText(R.string.goodByeMessage).isDisplayed();
    }

    @Then("^I see the guest's dashboard$")
    public void i_see_the_guest_s_dashboard() {
        onViewWithText("OK").click();
        onViewWithId(R.id.gridviewParque).isDisplayed();
    }

    @When("^introduce an existing e-mail$")
    public void introduce_an_existing_e_mail() {
        onViewWithId(R.id.inputMyEmail).clearText();
        onViewWithId(R.id.inputMyEmail).type("grupol@mail.pt");
        closeKeyboard();
    }

    @Then("^I see a message saying 'This e-mail already exists'$")
    public void i_see_a_message_saying_This_e_mail_already_exists() {
        waitFor(1000);
        onViewWithText(R.string.alreadyEmailExist).isDisplayed();
    }

    @When("^I don't introduce the new password confirmation$")
    public void i_don_t_introduce_the_new_password_confirmation() {
        onViewWithId(R.id.inputNewPasswordConfirmation).clearText();
        onViewWithId(R.id.inputNewPasswordConfirmation).isEmpty();
        closeKeyboard();
    }

    @Then("^I see a message saying 'Please insert password confirmation'$")
    public void i_see_a_message_saying_Please_insert_password_confirmation() {
        waitFor(2000);
        onViewWithText(R.string.insertPassConf).isDisplayed();
    }

    @When("^introduce a new password with insufficient characters$")
    public void introduce_a_new_password_with_insufficient_characters() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputNewPassword).clearText();
        onViewWithId(R.id.inputNewPassword).type("123");
        closeKeyboard();
    }

    @When("^the new password confirmation with insufficient characters$")
    public void the_new_password_confirmation_with_insufficient_characters() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputNewPasswordConfirmation).clearText();
        onViewWithId(R.id.inputNewPasswordConfirmation).type("123");
        closeKeyboard();
    }

    @Then("^I see a message saying 'Password with wrong format'$")
    public void i_see_a_message_saying_Password_with_wrong_format() {
        onViewWithText(R.string.wrongFormatPass).isDisplayed();
    }

    @When("^I click in a favorite spot$")
    public void i_click_in_a_favorite_spot() {
        onViewWithId(R.id.favouriteSpots, 0).click();
        //   onViewWithText(R.string.favspot).click();
    }

    @Then("^I see a message saying 'Spot deleted!'$")
    public void i_see_a_message_saying_Spot_deleted() {
        onViewWithText(R.string.spotDeleted).isDisplayed();
    }


}