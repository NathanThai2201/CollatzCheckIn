package com.example.collatzcheckin;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.fragment.app.testing.FragmentScenario.launchInContainer;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.collatzcheckin.R;
import com.example.collatzcheckin.admin.controls.events.AdminEventViewFragment;
import com.google.firebase.auth.FirebaseAuth;

@RunWith(AndroidJUnit4.class)
public class AdminEventViewFragmentTest {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Rule
    public ActivityScenarioRule<AdminMainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AdminMainActivity.class);
    @Before
    public void setUp() {
        // Initialize the fragment scenario with a mock activity context
        FragmentScenario<AdminEventViewFragment> fragmentScenario = FragmentScenario.launchInContainer(
                AdminEventViewFragment.class, null, null);
    }

    @Test
    public void testButtonsExist() {
        // Check if the delete button is displayed
        onView(withId(R.id.delete_event)).check(matches(isDisplayed()));

        // Check if the back button is displayed
        onView(withId(R.id.back_button_event_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testTextViewsExist() {
        // Check if the event title text view is displayed
        onView(withId(R.id.event_title)).check(matches(isDisplayed()));

        // Check if the event description text view is displayed
        onView(withId(R.id.event_description)).check(matches(isDisplayed()));

        // Check if the event location text view is displayed
        onView(withId(R.id.event_location)).check(matches(isDisplayed()));

        // Check if the event month text view is displayed
        onView(withId(R.id.event_month)).check(matches(isDisplayed()));

        // Check if the event day text view is displayed
        onView(withId(R.id.event_day)).check(matches(isDisplayed()));

        // Check if the event time text view is displayed
        onView(withId(R.id.event_time)).check(matches(isDisplayed()));
    }
}
