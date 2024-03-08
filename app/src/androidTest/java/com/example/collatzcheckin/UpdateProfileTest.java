package com.example.collatzcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.collatzcheckin.attendee.profile.UpdateProfileActivity;

import org.junit.Rule;
import org.junit.Test;

public class UpdateProfileTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void navProfile() throws InterruptedException {
        // Click on navbar
        onView(withId(R.id.profile)).perform(click());
        Thread.sleep(8000);

        // Check if text "Profile" is matched with any of the text displayed on the screen
        onView(withText("Profile")).check(matches(isDisplayed()));
    }

    @Test
    public void updateButton() throws InterruptedException {
        // Click on navbar
        onView(withId(R.id.profile)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.up_button)).perform(click());
        Thread.sleep(2000);
        // Check if text "Cancel" is matched with any of the text displayed on the screen
        // as can be used to differenciate between screens
        onView(withText("Cancel")).check(matches(isDisplayed()));
    }

    @Test
    public void updateProfile() throws InterruptedException {
        // Click on navbar
        onView(withId(R.id.profile)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.up_button)).perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.nameText)).perform(ViewActions.typeText("FirstName LastName"));
        onView(withId(R.id.emailText)).perform(ViewActions.typeText("Email"));

        onView(withId(R.id.up_button)).perform(click());
        Thread.sleep(2000);
        onView(withText("FirstName LastName")).check(matches(isDisplayed()));

    }
}
