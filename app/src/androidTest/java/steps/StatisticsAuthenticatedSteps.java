package steps;

import com.example.ecko.spots.R;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

public class StatisticsAuthenticatedSteps extends GreenCoffeeSteps {

    @Given("^I am logged$")
    public void i_am_logged() {
        onViewWithText(R.string.OK).click();
        onViewWithId(R.id.gridviewAuthParque).isDisplayed();
    }

    @When("^I press the statistics button$")
    public void i_press_the_statistics_button() {
        onViewWithId(R.id.btnStatistics).click();
    }

    @Then("^I can see the statistics screen$")
    public void i_can_see_the_statistics_screen() {
        onViewWithId(R.id.textViewStatisticsScreenType).isDisplayed();
    }



    @Given("^I see the statistics screen$")
    public void i_see_the_statistics_screen() {
        onViewWithId(R.id.textViewStatisticsScreenType).isDisplayed();
    }

    @When("^I choose by rate$")
    public void i_choose_by_rate() {
        onViewWithId(R.id.spinnerStatisticsType).click();
        onViewWithText("Rate").click();
    }

    @When("^I click general radio button$")
    public void i_click_general_radio_button() {
        onViewWithId(R.id.general).click();
    }

    @When("^I click ver button$")
    public void i_click_ver_button() {
        onViewWithId(R.id.ver).click();
    }

    @Then("^I see general statistics in charts about the best and the worst spot with rate$")
    public void i_see_general_statistics_in_charts_about_the_best_and_the_worst_spot_with_rate() {
        onViewWithId(R.id.piorSpot).isDisplayed();
        onViewWithId(R.id.melhorSpot).isDisplayed();
    }

    @When("^I click park radio button$")
    public void i_click_park_radio_button() {
        onViewWithId(R.id.byPark).click();
    }

    @When("^I select one park$")
    public void i_select_one_park() {
        onViewWithId(R.id.spinnerParks).click();
        onViewWithText("Parque A").click();
    }

    @Then("^I see by park statistics in charts about the best and the worst spot with rate$")
    public void i_see_by_park_statistics_in_charts_about_the_best_and_the_worst_spot_with_rate() {
        onViewWithId(R.id.piorSpot).isDisplayed();
        onViewWithId(R.id.melhorSpot).isDisplayed();
    }

    @When("^I click hours radio button$")
    public void i_click_hours_radio_button() {
        onViewWithId(R.id.betweenHours).click();
    }

    @When("^I insert the start hour with wrong format$")
    public void i_insert_the_start_hour_with_wrong_format() {
        onViewWithId(R.id.startHour).clearText();
        onViewWithId(R.id.startHour).type("25:18");
        closeKeyboard();
    }

    @When("^I insert the end hour$")
    public void i_insert_the_end_hour() {
        onViewWithId(R.id.endHour).clearText();
        onViewWithId(R.id.endHour).type("23:00");
        closeKeyboard();
    }

    @Then("^I see a message saying 'Please insert the hour correctly'$")
    public void i_see_a_message_saying_Please_insert_the_hour_correctly() {
        onViewWithText(R.string.wrongHourFormart).isDisplayed();
        onViewWithText(R.string.OK).click();
    }

    @When("^I insert the start hour$")
    public void i_insert_the_start_hour() {
        onViewWithId(R.id.startHour).clearText();
        onViewWithId(R.id.startHour).type("10:00");
        closeKeyboard();
    }

    @When("^I insert the end hour with wrong format$")
    public void i_insert_the_end_hour_with_wrong_format() {
        onViewWithId(R.id.endHour).clearText();
        onViewWithId(R.id.endHour).type("25:18");
        closeKeyboard();
    }

    @When("^I don't insert the start hour$")
    public void i_don_t_insert_the_start_hour() {
        onViewWithId(R.id.startHour).clearText();
        onViewWithId(R.id.startHour).isEmpty();
        closeKeyboard();
    }

    @Then("^I see a message saying 'Please insert the start hour'$")
    public void i_see_a_message_saying_Please_insert_the_start_hour() {
        onViewWithText(R.string.introduceStartHour).isDisplayed();
        onViewWithText(R.string.OK).click();
    }

    @When("^I don't insert the end hour$")
    public void i_don_t_insert_the_end_hour() {
        onViewWithId(R.id.endHour).clearText();
        onViewWithId(R.id.endHour).isEmpty();
        closeKeyboard();
    }

    @Then("^I see a message saying 'Please insert the end hour'$")
    public void i_see_a_message_saying_Please_insert_the_end_hour() {
        onViewWithText(R.string.introduceEndHour).isDisplayed();
        onViewWithText(R.string.OK).click();
    }

    @When("^I choose by app$")
    public void i_choose_by_app() {
        onViewWithId(R.id.spinnerStatisticsType).click();
        onViewWithText("App").click();
    }

    @Then("^I see general statatistics in charts about the time that the app take to sugest a spot and the number of registed users $")
    public void i_see_general_statatistics_in_charts_about_the_time_that_the_app_take_to_sugest_a_spot_and_the_number_of_registed_users() {
        onViewWithId(R.id.numberRegisted).isDisplayed();
        onViewWithId(R.id.tempoMedioSugestao).isDisplayed();
    }

    @Then("^I see a message saying 'Este filtro não se aplica!'$")
    public void i_see_a_message_saying_Este_filtro_não_se_aplica() {
        onViewWithText(R.string.naoSeAplicaFiltro).isDisplayed();
        onViewWithText(R.string.OK).click();
    }


}