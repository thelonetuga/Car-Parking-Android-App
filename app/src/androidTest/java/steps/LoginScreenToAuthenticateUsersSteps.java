package steps;

import android.support.test.rule.ActivityTestRule;

import com.example.ecko.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class LoginScreenToAuthenticateUsersSteps extends GreenCoffeeSteps {


    @Given("^I see an empty login form$")
    public void i_see_an_empty_login_form() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputEmailLogin).isEmpty();
        onViewWithId(R.id.inputPasswordLogin).isEmpty();
    }

    @When("^introduce an invalid email$")
    public void introduce_an_invalid_email() {
        onViewWithId(R.id.inputEmailLogin).type("invalid@gmail.com");
        closeKeyboard();

    }

    @When("^I introduce an invalid password$")
    public void i_introduce_an_invalid_password() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputPasswordLogin).type("123546");
        closeKeyboard();
    }

    @When("^I press the login button$")
    public void i_press_the_login_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnLogin).click();
    }

    @Then("^I see an error message saying 'Invalid credentials!'$")
    public void i_see_an_error_message_saying_Invalid_credentials() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(1500);
        onViewWithText(R.string.invalidCredentials).isDisplayed();
    }

    @When("^introduce a valid email$")
    public void introduce_a_valid_email() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputEmailLogin).type("debug@mail.pt");
        closeKeyboard();
    }

    @When("^I introduce a valid password$")
    public void i_introduce_a_valid_password() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputPasswordLogin).type("pass1234");
        closeKeyboard();
    }

    @When("^introduce an invalid email format$")
    public void introduce_an_invalid_email_format() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.inputEmailLogin).type("badformat.pt@");
        closeKeyboard();
    }

    @Then("^I see an error message saying 'Please introduce a valid email\\. e\\.g\\.: xxx@xxx\\.xx'$")
    public void i_see_an_error_message_saying_Please_introduce_a_valid_email_e_g_xxx_xxx_xx() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(R.string.wrongEmailFormat).isDisplayed();
    }

    @Then("^I see a message saying 'Welcome to Spots'$")
    public void i_see_a_message_saying_Welcome_to_Spots() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(2000);
        onViewWithText(R.string.welcomeMessage).isDisplayed();
        onViewWithText("OK").click();
    }

    @Then("^I see the dashboard screen$")
    public void i_see_the_dashboard_screen() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(1000);
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }

    @When("^I press the keep me signed in button$")
    public void i_press_the_keep_me_signed_in_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.cbKeepMeSigned).click();
    }


}
