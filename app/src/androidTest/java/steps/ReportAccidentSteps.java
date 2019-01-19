package steps;

import com.example.ecko.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class ReportAccidentSteps extends GreenCoffeeSteps {

    @Given("^i see authenticated dashboard screen$")
    public void i_see_authenticated_dashboard_screen() {
        onViewWithText("OK").click();
        onViewWithId(R.id.mapAuth).isDisplayed();
    }

    @When("^i press the 'Incident' Report button$")
    public void i_press_the_Incident_Report_button() {
        onViewWithId(R.id.btnIncidentReport).click();
    }

    @Then("^I see the incident report screen$")
    public void i_see_the_incident_report_screen() {
        onViewWithId(R.id.btnCancelReport).isDisplayed();
        onViewWithId(R.id.btnReport).isDisplayed();
        onViewWithId(R.id.btnUploadPhoto).isDisplayed();
    }

    @Given("^i see an empty incident report form$")
    public void i_see_an_empty_incident_report_form() {
        onViewWithId(R.id.txtIncidentDescription).isEmpty();
    }

    @When("^i introduce a new incident$")
    public void i_introduce_a_new_incident() {
        onViewWithId(R.id.txtIncidentDescription).type("Incident Report test");
        closeKeyboard();
        onViewWithId(R.id.radioBtnWrongUse).click();
    }

    @When("^i press report button$")
    public void i_press_report_button() {
        onViewWithId(R.id.btnReport).click();
    }

    @Then("^i see a message saying 'Thanks for your help'$")
    public void i_see_a_message_saying_Thanks_for_your_help() {
        onViewWithText(R.string.reportSubmitted).isDisplayed();
        onViewWithText("OK").click();
    }

    @When("^i don't write a description$")
    public void i_don_t_write_a_description() {
        onViewWithId(R.id.radioBtnWrongUse).click();
    }

    @Then("^i see a message saying 'Please insert a description'$")
    public void i_see_a_message_saying_Please_insert_a_description() {
        onViewWithText(R.string.insertDescription).isDisplayed();
        onViewWithText("OK").click();
    }

}
