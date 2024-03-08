package com.example.collatzcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventListTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void eventListView() throws InterruptedException {
        Thread.sleep(5000);
        //click nav bar
        onView(withId(R.id.home)).perform(click());
        Thread.sleep(5000);
        //check if on events page
        onView(withText("Your Events")).check(matches(isDisplayed()));
    }

    @Test
    public void createEventButton() {
        Thread.sleep(5000);
        //click nav bar
        onView(withId(R.id.home)).perform(click());
        Thread.sleep(5000);

        onView(withId(R.id.create_event)).perform(click());
        //add event button is there
        onView(withText("Add Event")).check(matches(isDisplayed()));
    }

    @Test
    public void viewEventInfo() throws InterruptedException {
        Thread.sleep(5000);
        //click nav bar
        onView(withId(R.id.home)).perform(click());
        Thread.sleep(5000);

        onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.event_list_view
        )).atPosition(0).perform(click());
        Thread.sleep(5000);
        onView(withText("Send Notification")).check(matches(isDisplayed()));
    }

    public void back() throws InterruptedException {
        Thread.sleep(5000);
        //click nav bar
        onView(withId(R.id.home)).perform(click());
        Thread.sleep(5000);

        onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.event_list_view
        )).atPosition(0).perform(click());
        Thread.sleep(5000);
        //click back button
        onView(withId(R.id.back_button)).perform(click());
        //check if back on page
        onView(withText("Your Events")).check(matches(isDisplayed()));
    }


}


