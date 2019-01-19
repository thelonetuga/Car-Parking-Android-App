package features;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.rule.ActivityTestRule;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;

import com.example.ecko.spots.AuthenticatedDashboardActivity;
import com.example.ecko.spots.LoginActivity;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import steps.LoginScreenToAuthenticateUsersSteps;
import steps.MySpotScreenSteps;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

@RunWith(Parameterized.class)
public class MySpotScreenFeatureTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule<AuthenticatedDashboardActivity> activity = new ActivityTestRule<>(AuthenticatedDashboardActivity.class);

    public MySpotScreenFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/mySpotScreen.feature")
                .scenarios();
    }

    @Test
    public void test() {
        start(new MySpotScreenSteps());
    }

    public static ViewAction setRating(final float rating) {
        if (rating % 0.5 != 0) {
            throw new IllegalArgumentException("Rating must be multiple of 0.5f");
        }

        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RatingBar.class);
            }

            @Override
            public String getDescription() {
                return "Set rating on RatingBar in 0.5f increments";
            }

            @Override
            public void perform(UiController uiController, View view) {
                GeneralClickAction viewAction = new GeneralClickAction(
                        Tap.SINGLE,
                        new CoordinatesProvider() {
                            @Override
                            public float[] calculateCoordinates(View view) {
                                int[] locationOnScreen = new int[2];
                                view.getLocationOnScreen(locationOnScreen);
                                int screenX = locationOnScreen[0];
                                int screenY = locationOnScreen[1];
                                int numStars = ((RatingBar) view).getNumStars();
                                float widthPerStar = 1f * view.getWidth() / numStars;
                                float percent = rating / numStars;
                                float x = screenX + view.getWidth() * percent;
                                float y = screenY + view.getHeight() * 0.5f;
                                return new float[]{x - widthPerStar * 0.5f, y};
                            }
                        },
                        Press.FINGER,
                        InputDevice.SOURCE_UNKNOWN,
                        MotionEvent.BUTTON_PRIMARY
                );
                viewAction.perform(uiController, view);
            }
        };
    }
}
