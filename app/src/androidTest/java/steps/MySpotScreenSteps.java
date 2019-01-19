package steps;

import android.support.test.espresso.ViewAction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.example.ecko.spots.R;
import com.example.ecko.spots.model.User;
import com.example.ecko.spots.model.UserManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static features.MySpotScreenFeatureTest.setRating;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MySpotScreenSteps extends GreenCoffeeSteps {
    @Given("^I see authenticated dashboard screen$")
    public void i_see_authenticated_dashboard_screen() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText("OK").click();
        onViewWithId(R.id.mapAuth).isDisplayed();
    }

    @When("^I dont have a current spot$")
    public void i_dont_have_a_current_spot() {
        // Write code here that turns the phrase above into concrete actions
        UserManager.INSTANCE.verifyDatabaseIfHasSpot();
        boolean aux = UserManager.INSTANCE.hasSpot();
        waitFor(4000);
       assertTrue(!aux);
    }

    @When("^I press the My Spot button$")
    public void i_press_the_My_Spot_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnMySpot).click();
    }

    @Then("^I see a message saying 'You dont have a saved spot'$")
    public void i_see_a_message_saying_You_dont_have_a_saved_spot() {
        // Write code here that turns the phrase above into concrete actions

        onViewWithText(R.string.notHaveSavedSpot).isDisplayed();
        onViewWithText("OK").click();
    }

    @When("^I have a current spot$")
    public void i_have_a_current_spot() {
        // Write code here that turns the phrase above into concrete actions
        UserManager.INSTANCE.verifyDatabaseIfHasSpot();
        waitFor(4000);
        assertTrue(UserManager.INSTANCE.hasSpot());
    }

    @Then("^I see the location in the map$")
    public void i_see_the_location_in_the_map() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.mapMySpot).isDisplayed();
    }

    @When("^i press the Add to Favourites checkBox$")
    public void i_press_the_Add_to_Favourites_checkBox() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.checkBoxPopUp).click();
    }

    @When("^I see the cancel button$")
    public void i_see_the_cancel_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnCancelMySpot).isDisplayed();
    }

    @Then("^I press the cancel button$")
    public void i_press_the_cancel_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnCancelMySpot).click();
    }

    @Then("^I go to autenticated dashboard$")
    public void i_go_to_autenticated_dashboard() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.mapAuth).isDisplayed();
    }

    @Then("^I press the Leave My Spot button$")
    public void i_press_the_Leave_My_Spot_button() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(2000);
        onViewWithId(R.id.btnLeaveMySpot).click();
    }

    @Then("^I rate the spot$")
    public void i_rate_the_spot() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.ratingBarPopUp).click();
    }

    @Then("^I click on save button$")
    public void i_click_on_save_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnSavePopUp).click();
    }

    @Then("^I see a message saying 'Thanks for using this spot\\. Have a nice trip!'$")
    public void i_see_a_message_saying_Thanks_for_using_this_spot_Have_a_nice_trip() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(R.string.thanksMSG).isDisplayed();
        onViewWithText("OK").click();
        waitFor(3000);
    }

    @Then("^I dont rate the spot$")
    public void i_dont_rate_the_spot() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnSavePopUp).click();
    }


    @Then("^i see a message saying 'Rate the spot please'$")
    public void i_see_a_message_saying_Rate_the_spot_please() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(R.string.ratePlease).isDisplayed();
        onViewWithText("OK").click();
    }

}
