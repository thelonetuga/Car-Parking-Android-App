package features;

import android.support.test.rule.ActivityTestRule;

import com.example.ecko.spots.AuthenticatedDashboardActivity;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import steps.FindMeASpotSteps;
import steps.FindSpotManuallySteps;

@RunWith(Parameterized.class)
public class FindSpotManuallyFeatureTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule<AuthenticatedDashboardActivity> activity = new ActivityTestRule<>(AuthenticatedDashboardActivity.class);

    public FindSpotManuallyFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }


    @Parameterized.Parameters(name = "{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/findSpotManually.feature").scenarios();
    }

    @Test
    public void test() {
        start(new FindSpotManuallySteps());
    }

}
