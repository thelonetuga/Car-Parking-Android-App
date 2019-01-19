package steps;

import com.example.ecko.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class StatisticsRankingScreenSteps extends GreenCoffeeSteps {
    @Given("^I see authenticated dashboard screen$")
    public void i_see_authenticated_dashboard_screen() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithText("OK").click();
        onViewWithId(R.id.mapAuth).isDisplayed();
    }

    @When("^I see a statistics button$")
    public void i_see_a_statistics_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnStatistics).isDisplayed();
    }

    @When("^I click on statististics button$")
    public void i_click_on_statististics_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.btnStatistics).click();
    }

    @Then("^I see the statistics screen$")
    public void i_see_the_statistics_screen() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.spinnerStatisticsType).isDisplayed();
    }

    @Then("^I choose by ranking of occupated$")
    public void i_choose_by_ranking_of_occupated() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.spinnerStatisticsType).click();
        onViewWithText("Ranking").isDisplayed();
        onViewWithText("Ranking").click();
    }

    @Then("^I click general radio button$")
    public void i_click_general_radio_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.general).click();
    }

    @Then("^I see all general statistics charts about ranking of occupated$")
    public void i_see_all_general_statistics_charts_about_ranking_of_occupated() {
        // Write code here that turns the phrase above into concrete actions
        //verificar se o grafico é displayed
        onViewWithId(R.id.ver).click();
        onViewWithId(R.id.viewChart).isDisplayed();
    }

    @Then("^I click by hour radio button$")
    public void i_click_by_hour_radio_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.betweenHours).click();
    }

    @Then("^I choose an interval time$")
    public void i_choose_an_interval_time() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.startHour).type("12:00");
        onViewWithId(R.id.endHour).type("23:59");
    }

    @Then("^I see statistics charts about ranking of occupated by hour$")
    public void i_see_statistics_charts_about_ranking_of_occupated_by_hour() {
        // Write code here that turns the phrase above into concrete actions
        //verificar se o grafico é displayed
        onViewWithId(R.id.ver).click();
        onViewWithId(R.id.viewChart).isDisplayed();
    }


    @Then("^I click by Park radio button$")
    public void i_click_by_Park_radio_button() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.byPark).click();
    }

    @Then("^I choose the park on combobox$")
    public void i_choose_the_park_on_combobox() {
        // Write code here that turns the phrase above into concrete actions
        onViewWithId(R.id.spinnerParks).click();
        onViewWithText("Parque D").isDisplayed();
        onViewWithText("Parque D").click();
    }

    @Then("^I see statistics charts about ranking of occupated by Park choosen$")
    public void i_see_statistics_charts_about_ranking_of_occupated_by_Park_choosen() {
        // Write code here that turns the phrase above into concrete actions
        //verificar se o grafico é displayed
        onViewWithId(R.id.ver).click();
        onViewWithId(R.id.viewChart).isDisplayed();
    }
}
