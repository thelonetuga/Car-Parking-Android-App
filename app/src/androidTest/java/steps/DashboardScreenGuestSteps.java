package steps;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.example.ecko.spots.R;
import com.example.ecko.spots.model.Parque;
import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.Spot;
import com.google.android.gms.maps.model.Marker;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;

import java.util.LinkedList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

public class DashboardScreenGuestSteps extends GreenCoffeeSteps {


    @Given("^I have default park$")
    public void i_have_default_park() {
        assertTrue(ParquesManager.INSTANCE.hasDefaultParque());
    }

    @Then("^I see the default park$")
    public void i_see_the_default_park() {
        onViewWithId(R.id.txtViewNomeParque).contains("Parque");
    }

    @Then("^a map with the location of the default park$")
    public void a_map_with_the_location_of_the_default_park() {
        onViewWithId(R.id.map).isDisplayed();
    }

    @Then("^a schema of the ocupated and non-ocupated spots$")
    public void a_schema_of_the_ocupated_and_non_ocupated_spots() {
        onViewWithId(R.id.gridviewParque).isDisplayed();
    }

    @Then("^the login button$")
    public void the_login_button() {
        onViewWithId(R.id.btnLogin).isDisplayed();
    }

    @Then("^the regist button$")
    public void the_regist_button() {
        onViewWithId(R.id.btnRegist).isDisplayed();
    }

    @Then("^the data of the last update$")
    public void the_data_of_the_last_update() {
        onViewWithId(R.id.txtData).contains("Last update:");
    }

    @Given("^I have no default park$")
    public void i_have_no_default_park() {
        ParquesManager.INSTANCE.hasNoDefaultParque().equals("no");
    }

    @Then("^I see a message saying 'You do not have a default park!'$")
    public void i_see_a_message_saying_You_do_not_have_a_default_park() {
        onViewWithId(R.id.status_message).contains(string(R.string.noparque));
    }

    @Given("^there are available spots$")
    public void there_are_available_spots() {
        List<Spot> listaSpots = ParquesManager.INSTANCE.getListaSpotsDefaultParque();
        Parque parque = ParquesManager.INSTANCE.getDefaultParque();
        ParquesManager.INSTANCE.hasAvailableSpots(listaSpots,parque).equals("yes");
    }

    @Then("^I see pins in the map with the spots$")
    public void i_see_pins_in_the_map_with_the_spots() {
        // ver pins no mapa
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("DEFAULT PARK"));
        try {
            marker.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Given("^there are not available spots$")
    public void there_are_not_available_spots() {
        List<Spot> listaSpots = ParquesManager.INSTANCE.getListaSpotsDefaultParque();
        Parque parque = ParquesManager.INSTANCE.getDefaultParque();
        ParquesManager.INSTANCE.hasAvailableSpots(listaSpots,parque).equals("no");
    }

    @Then("^I see an empty map$")
    public void i_see_an_empty_map() {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionStartsWith("PARQUE"));
        marker.exists();
    }
}
