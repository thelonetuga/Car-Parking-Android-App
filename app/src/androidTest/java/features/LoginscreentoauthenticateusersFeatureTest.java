package features;

import android.support.test.rule.ActivityTestRule;
import android.widget.CheckBox;

import com.example.ecko.spots.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.Scenario;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Locale;

import steps.LoginScreenToAuthenticateUsersSteps;

@RunWith(Parameterized.class)
public class LoginscreentoauthenticateusersFeatureTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule<LoginActivity> activity = new ActivityTestRule<>(LoginActivity.class);

    public LoginscreentoauthenticateusersFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/loginScreenToAuthenticateUsers.feature")
                .scenarios();
    }

    @Test
    public void test() {
        start(new LoginScreenToAuthenticateUsersSteps());
    }

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        FirebaseAuth.getInstance().signOut();
    }
}
