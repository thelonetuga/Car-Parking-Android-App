package steps;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.example.ecko.spots.R;
import com.example.ecko.spots.model.Parque;
import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.Spot;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

public class DashboardScreenAutenticatedSteps extends GreenCoffeeSteps {

    @Given("^I am logged in application$")
    public void i_am_logged_in_application() {
        onViewWithText("OK").click();
        onViewWithId(R.id.btnLogout).isDisplayed();
    }

    @Given("^I have a spot$")
    public void i_have_a_spot() {
        assertTrue(ParquesManager.INSTANCE.hasSpot("Parque D"));
    }

    @Then("^I can see the spot$")
    public void i_can_see_the_spot() {
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }

    @Then("^a map with the location of the park$")
    public void a_map_with_the_location_of_the_park() {
        //no mapa
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionStartsWith("SPOT"));
        marker.exists();
    }

    @Then("^a schema of the ocupated and non-ocupated spots$")
    public void a_schema_of_the_ocupated_and_non_ocupated_spots() {
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }

    @Then("^the profile button$")
    public void the_profile_button() {
        onViewWithId(R.id.txtViewProfile).isDisplayed();
    }

    @Then("^the statistics button$")
    public void the_statistics_button() {
        onViewWithId(R.id.btnStatistics).isDisplayed();
    }

    @Then("^the find my spot button$")
    public void the_find_my_spot_button() {
        onViewWithId(R.id.btnFindMeASpot).isDisplayed();
    }

    @Then("^the available spots button$")
    public void the_available_spots_button() {
        onViewWithId(R.id.btnMySpot).isDisplayed();
    }

    @Then("^the more info button$")
    public void the_more_info_button() {
        onViewWithId(R.id.btnMoreInfo).isDisplayed();
    }

    @Then("^the spinner of available parks$")
    public void the_spinner_of_available_parks() {
        onViewWithId(R.id.availableSpotsSpinner).isDisplayed();
    }

    @Then("^the data of the last update$")
    public void the_data_of_the_last_update() {
        onViewWithId(R.id.txtData).contains("Last update:");
    }

    @Given("^I see the dashboard screen$")
    public void i_see_the_dashboard_screen() {
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }

    @When("^I am loged$")
    public void i_am_loged() {
        // estou autenticado: vejo o botao de perfil
        onViewWithId(R.id.txtViewProfile).isDisplayed();
    }

    @Then("^I see the logout button$")
    public void i_see_the_logout_button() {

        onViewWithId(R.id.btnLogout).isDisplayed();
    }

    @Given("^there are available spots$")
    public void there_are_available_spots() {
        List<Spot> listaSpots = ParquesManager.INSTANCE.getListaSpotsNomeParque("Parque D");
        Parque parque = ParquesManager.INSTANCE.getParqueByName("Parque D");
        ParquesManager.INSTANCE.hasAvailableSpots(listaSpots,parque).equals("yes");
    }

    @Then("^I see pins in the map with the spots$")
    public void i_see_pins_in_the_map_with_the_spots() {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("DAKAR"));

        try {
            marker.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Given("^there are not available spots$")
    public void there_are_not_available_spots() {
        List<Spot> listaSpots = ParquesManager.INSTANCE.getListaSpotsNomeParque("Parque D");
        Parque parque = ParquesManager.INSTANCE.getParqueByName("Parque D");
        ParquesManager.INSTANCE.hasAvailableSpots(listaSpots,parque).equals("no");
    }

    @Then("^I see an empty map$")
    public void i_see_an_empty_map() {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionStartsWith("PARQUE"));
        marker.exists();
    }

    @Then("^a message saying 'There are no available spots'$")
    public void a_message_saying_There_are_no_available_spots() {
        onViewWithId(R.id.status_message).contains(string(R.string.noavailablespots));
    }

    @Given("^I see the Authenticated Dashboard screen$")
    public void i_see_the_Authenticated_Dashboard_screen() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnLogout).isDisplayed();
        onViewWithId(R.id.txtViewProfile).isDisplayed();
    }

    @When("^I click the logout button$")
    public void i_click_the_logout_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnLogout).click();
    }

    @Then("^I see a message saying 'Thanks for using our app'$")
    public void i_see_a_message_saying_Thanks_for_using_our_app() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText(R.string.goodByeMessage).isDisplayed();
        onViewWithText("OK").click();

    }

    @Then("^I see the guest Dashboard$")
    public void i_see_the_guest_Dashboard() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.gridviewParque).isDisplayed();
    }
}