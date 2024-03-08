package com.example.collatzcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class NavBarTest 
{
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void event() throws InterruptedException {
        Thread.sleep(5000);
        //click nav bar
        onView(withId(R.id.home)).perform(click());
        Thread.sleep(5000);
        //check if on events page
        onView(withText("Your Events")).check(matches(isDisplayed()));
    }

    @Test
    public void profile() throws InterruptedException {
        Thread.sleep(5000);
        //click nav bar
        onView(withId(R.id.profile)).perform(click());
        Thread.sleep(5000);
        //check if on profile page
        onView(withText("Profile")).check(matches(isDisplayed()));
    }

    @Test
    public void multipleTransition() throws InterruptedException {
        Thread.sleep(5000);
        //click nav bar
        onView(withId(R.id.profile)).perform(click());
        Thread.sleep(5000);
        //click events page
        onView(withId(R.id.home)).perform(click());
        Thread.sleep(5000);
        //check if on events page
        onView(withText("Your Events")).check(matches(isDisplayed()));
    }
}
