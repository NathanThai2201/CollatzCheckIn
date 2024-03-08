package com.example.collatzcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class CreateEventTest {
    @Rule
    public ActivityScenarioRule<CreateEvent> scenario = new ActivityScenarioRule<CreateEvent>(CreateEvent.class);

    @Test
    public void testPlainText() {
        onView(withId(R.id.edit_event_name)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_event_date)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_event_location)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_event_description)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_event_limit)).check(matches(isDisplayed()));
    }
}
