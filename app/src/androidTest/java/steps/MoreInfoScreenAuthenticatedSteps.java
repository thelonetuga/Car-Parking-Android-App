package steps;

import com.example.ecko.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static org.junit.Assert.assertTrue;

public class MoreInfoScreenAuthenticatedSteps extends GreenCoffeeSteps {

    @Given("^I am loged$")
    public void i_am_loged() {
        onViewWithText(R.string.OK).click();
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }

    @Given("^I press the more info button$")
    public void i_press_the_more_info_button() {
        onViewWithId(R.id.btnMoreInfo).click();
    }

    @Then("^I can see the more info screen$")
    public void i_can_see_the_more_info_screen() {
        waitFor(2000);
        onViewWithId(R.id.moreInfo).isDisplayed();
    }

    @Given("^I see the more info screen$")
    public void i_see_the_more_info_screen() {
        onViewWithId(R.id.moreInfo).isDisplayed();
    }

    @Then("^I see the product owner$")
    public void i_see_the_product_owner() {
        onViewWithId(R.id.productOwner).isDisplayed();
    }

    @Then("^the scrum master$")
    public void the_scrum_master() {
        onViewWithId(R.id.scrumMaster).isDisplayed();
    }

    @Then("^the development team$")
    public void the_development_team() {
        onViewWithId(R.id.developmentTeam).isDisplayed();
        onViewWithId(R.id.rute).isDisplayed();
        onViewWithId(R.id.edgar).isDisplayed();
        onViewWithId(R.id.joao).isDisplayed();
        onViewWithId(R.id.ricardo).isDisplayed();
    }

    @Then("^the school$")
    public void the_school() {
        onViewWithId(R.id.school).isDisplayed();
    }

    @Then("^the class$")
    public void the_class() {
        onViewWithId(R.id.taes).isDisplayed();
    }

    @Then("^the scholar year$")
    public void the_scholar_year() {
        onViewWithId(R.id.scholarYear).isDisplayed();
    }

    @When("^I press the cancel button$")
    public void i_press_the_cancel_button() {
        onViewWithId(R.id.cancel).click();
    }

    @Then("^I see Authenticated dashboard$")
    public void i_see_Authenticated_dashboard() {
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }
}