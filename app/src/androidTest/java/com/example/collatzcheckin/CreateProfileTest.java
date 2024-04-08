package com.example.collatzcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.Is.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.collatzcheckin.attendee.profile.CreateProfileActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateProfileTest {
//    @Rule
//    public ActivityScenarioRule<CreateProfileActivity> scenario = new
//            ActivityScenarioRule<CreateProfileActivity>(CreateProfileActivity.class);
//
//    @Test
//    public void enterProfile(){
//        // Type Name and Email in the editText
//        onView(withId(R.id.username)).perform(ViewActions.typeText("FirstName LastName"));
//        onView(withId(R.id.email)).perform(ViewActions.typeText("Email"));
//        // Check if text "Edmonton" is matched with any of the text displayed on the screen
//        onView(withText("FirstName LastName")).check(matches(isDisplayed()));
//    }
}
