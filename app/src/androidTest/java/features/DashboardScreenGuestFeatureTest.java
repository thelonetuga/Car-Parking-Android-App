package features;

import android.support.test.rule.ActivityTestRule;

import com.example.ecko.spots.DashboardActivity;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import steps.DashboardScreenGuestSteps;

@RunWith(Parameterized.class)
public class DashboardScreenGuestFeatureTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule<DashboardActivity> activity = new ActivityTestRule<>(DashboardActivity.class);

    public DashboardScreenGuestFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

     @Parameterized.Parameters(name = "{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/dashboardScreenGuest.feature").scenarios();
    }

    @Test
    public void test(){
        start(new DashboardScreenGuestSteps());
    }



}
