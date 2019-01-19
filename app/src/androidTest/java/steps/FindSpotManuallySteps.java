package steps;

import com.example.ecko.spots.R;
import com.example.ecko.spots.model.Parque;
import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.Spot;
import com.example.ecko.spots.model.User;
import com.example.ecko.spots.model.UserManager;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FindSpotManuallySteps extends GreenCoffeeSteps {

    @Given("^I am logged in application$")
    public void i_am_logged_in_application() {
        onViewWithText("OK").click();
        onViewWithText("OK").click();
        onViewWithId(R.id.mapAuth).isDisplayed();
    }

    @Given("^i have a spot in 'My Spot'$")
    public void i_have_a_spot_in_My_Spot() {
        UserManager.INSTANCE.verifyDatabaseIfHasSpot();
        //boolean aux = UserManager.INSTANCE.hasSpot();
        waitFor(5000);
        //assertTrue(aux);
    }

    @When("^i see the grid of my actual park$")
    public void i_see_the_grid_of_my_actual_park() {
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }

    @When("^i press the empty spot$")
    public void i_press_the_empty_spot() {
        onViewChildOf(R.id.gridviewAuthParque, 1).click();
    }

    @Then("^i see a message saying 'You already have a spot!'$")
    public void i_see_a_message_saying_You_already_have_a_spot() {
        onViewWithText(R.string.alreadyHaveSpot).isDisplayed();
    }

    @Given("^i don't have a spot defined$")
    public void i_don_t_have_a_spot_defined() {
        UserManager.INSTANCE.verifyDatabaseIfHasSpot();
      ///  boolean aux = UserManager.INSTANCE.hasSpot();
        waitFor(5000);
        //assertFalse(aux);
    }

    @When("^i see the grid$")
    public void i_see_the_grid() {
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }

    @When("^i press an empty spot$")
    public void i_press_an_empty_spot() {
        onViewChildOf(R.id.gridviewAuthParque, 1).click();
    }

    @Then("^i see a message saying 'Spot added'$")
    public void i_see_a_message_saying_Spot_added() {
        onViewWithText(R.string.spotAdded).isDisplayed();
    }

}
