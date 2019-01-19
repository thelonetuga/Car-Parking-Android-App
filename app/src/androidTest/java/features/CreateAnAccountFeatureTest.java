package features;

import android.support.test.rule.ActivityTestRule;

import com.example.ecko.spots.RegisterActivity;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.Scenario;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Locale;

import steps.CreateAnAccountSteps;

@RunWith(Parameterized.class)
public class CreateAnAccountFeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule<RegisterActivity> activity = new ActivityTestRule<>(RegisterActivity.class);

    public CreateAnAccountFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/createAnAccount.feature")
                .scenarios();
    }

    @Test
    public void test() {
        start(new CreateAnAccountSteps());
    }

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {

    }
}
