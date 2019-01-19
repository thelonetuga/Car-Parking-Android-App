package steps;

import com.example.ecko.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static org.junit.Assert.assertTrue;

public class CreateAnAccountSteps extends GreenCoffeeSteps {

    @Given("^I have opened the application$")
    public void i_have_opened_the_application() {
        // Write code here that turns the phrase above into concrete actions
        assertTrue("Spots".equalsIgnoreCase(string(R.string.app_name)));
        onViewWithText(string(R.string.app_name)).isDisplayed();
    }

    @When("^I press the register button$")
    public void i_press_the_register_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText("Regist").click();
    }

    @Then("^I see the register screen$")
    public void i_see_the_register_screen() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputEmail).isDisplayed();
        onViewWithId(R.id.inputPassword).isDisplayed();
        onViewWithId(R.id.btnRegist).isDisplayed();
        onViewWithId(R.id.btnCancel).isDisplayed();
    }

    @Given("^I see an empty register form$")
    public void i_see_an_empty_register_form() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputEmail).isEmpty();
        onViewWithId(R.id.inputPassword).isEmpty();
    }

    @When("^introduce an invalid email$")
    public void introduce_an_invalid_email() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputEmail).type("invalid.com");
        closeKeyboard();
    }

    @When("^I introduce an invalid password$")
    public void i_introduce_an_invalid_password() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputPassword).type("1teste");
        closeKeyboard();
    }

    @When("^I press the regist button$")
    public void i_press_the_regist_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnRegist).click();
    }

    @Then("^I see an error message saying 'Invalid credentials!'$")
    public void i_see_an_error_message_saying_Invalid_credentials() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(R.string.invalidCredentials).isDisplayed();    }

    @When("^introduce a valid email$")
    public void introduce_a_valid_email() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputEmail).type("lol@gmail.com");
        closeKeyboard();
    }

    @Then("^I see an error message saying 'Please introduce a valid password'$")
    public void i_see_an_error_message_saying_Please_introduce_a_valid_password() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(R.string.invalidPassword).isDisplayed();    }

    @When("^introduce an existing email$")
    public void introduce_an_existing_email() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputEmail).type("grupol@mail.pt");
        closeKeyboard();
    }

    @When("^I introduce a valid password$")
    public void i_introduce_a_valid_password() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputPassword).type("Teste@123");
        onViewWithId(R.id.inputPasswordConfirmation).type("Teste@123");
        closeKeyboard();
    }

    @Then("^I see an error message saying 'This email already exists'$")
    public void i_see_an_error_message_saying_This_email_already_exists() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(4000);
        onViewWithText(R.string.alreadyEmailExist).isDisplayed();
    }

    @Then("^I see a message saying 'Account created!'$")
    public void i_see_a_message_saying_Account_created() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(4000);
        onViewWithText(R.string.accountCreated).isDisplayed();
        onViewWithText("OK").click();
    }

    @Then("^I see the login screen$")
    public void i_see_the_login_screen() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(1000);
        onViewWithId(R.id.btnLogin).isDisplayed();
        onViewWithId(R.id.btnCancel).isDisplayed();
    }


    @When("^introduce a wrong format email$")
    public void introduce_a_wrong_format_email() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputEmail).type("wrongFormat.com");
        closeKeyboard();
    }

    @Then("^I see an error message saying 'Please insert a valid e-mail e\\.g\\.: xxx@xxx\\.xx'$")
    public void i_see_an_error_message_saying_Please_insert_a_valid_e_mail_e_g_xxx_xxx_xx() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(R.string.wrongEmailFormat).isDisplayed();
    }

    @When("^I introduce an invalid password one or two times$")
    public void i_introduce_an_invalid_password_one_or_two_times() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputPassword).type("inv2t");
        onViewWithId(R.id.inputPasswordConfirmation).type("inv2t");
        closeKeyboard();
    }

    @When("^I introduce two different passwords$")
    public void i_introduce_two_different_passwords() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputPassword).type("Teste@123");
        onViewWithId(R.id.inputPasswordConfirmation).type("123@Teste");
        closeKeyboard();
    }

    @Then("^I see an error message saying 'Please introduce the same password'$")
    public void i_see_an_error_message_saying_Please_introduce_the_same_password() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(R.string.differentPasswords).isDisplayed();
    }

    @When("^I introduce a valid password two times$")
    public void i_introduce_a_valid_password_two_times() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputPassword).type("Teste@123");
        onViewWithId(R.id.inputPasswordConfirmation).type("Teste@123");
        closeKeyboard();
    }
}
