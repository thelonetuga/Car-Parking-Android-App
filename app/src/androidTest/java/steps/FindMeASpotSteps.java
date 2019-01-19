package steps;

import com.example.ecko.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class FindMeASpotSteps extends GreenCoffeeSteps {
    @Given("^i see authenticated dashboard screen$")
    public void i_see_authenticated_dashboard_screen() {
        onViewWithText(R.string.OK).click();
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }

    @When("^i press the find me a spot button$")
    public void i_press_the_find_me_a_spot_button() {
        onViewWithId(R.id.btnFindMeASpot).click();
    }

    @Then("^I see the find me a spot screen$")
    public void i_see_the_find_me_a_spot_screen() {
        onViewWithId(R.id.btnSpot1).isDisplayed();
        onViewWithId(R.id.btnSpot2).isDisplayed();
        onViewWithId(R.id.btnSpot3).isDisplayed();
    }

    @When("^theres no empty spots available$")
    public void theres_no_empty_spots_available() {
        onViewWithId(R.id.availableSpotsSpinner).click();
        onViewWithText("Parque A").click();
    }

    @Then("^i see a message saying 'There's no empty spots available in this park'$")
    public void i_see_a_message_saying_There_s_no_empty_spots_available_in_this_park() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(R.string.noavailablespots_inpark);
    }

    @When("^there's one or more empty spots available$")
    public void there_s_one_or_more_empty_spots_available() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnSpot1).isDisplayed();
    }

    @When("^i press a spot button$")
    public void i_press_a_spot_button() {
        // Write code here that turns the phrase above into concrete actions
        waitFor(2000);
        onViewWithId(R.id.btnSpot1).click();
    }

    @When("^i see the path to that spot$")
    public void i_see_the_path_to_that_spot() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.navigation).isDisplayed();
        onViewWithId(R.id.btnCancelNav).isDisplayed();
    }

    @Then("^i click cancel button$")
    public void i_click_cancel_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnCancelNav).click();
    }

}
