package ch.hsr.mge.gadgeothek;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.*;

/**
 * Created by felix on 29/10/2017.
 */

public class LoginActivityInstrumentationTest {

    @Rule
    public ActivityTestRule loginActivityRule = new ActivityTestRule(LoginActivity.class);

    @Test
    public void useAppContext() throws Exception {
        //Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ch.hsr.mge.gadgeothek", appContext.getPackageName());
    }

    @Test
    public void testLogin() {

        String email = "test@example.com";
        String password = "test12";

        onView(withId(R.id.email))
                .perform(clearText(), typeText(email), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(clearText(), typeText(password), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        onView(withText("Loans")).check(matches(isDisplayed()));
    }
}
