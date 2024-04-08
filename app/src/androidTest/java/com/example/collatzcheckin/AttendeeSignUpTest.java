package com.example.collatzcheckin;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;



import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.example.collatzcheckin.event.Event;
import com.google.firebase.auth.FirebaseAuth;

public class AttendeeSignUpTest {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        // Initialize Firebase and anonymous authentication
        mAuth.signInWithEmailAndPassword("espressotest@gmail.com", "123456")
                .addOnSuccessListener(authResult -> {
                    Log.d("ESPRESSOLOGIN", "Espresso user logged in successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("ESPRESSOLOGIN", "Login failed for Espresso user: " + e.getMessage());
                });
        Thread.sleep(7000);
        Log.e("ESPRESSOLOGIN", String.valueOf(mAuth.getUid()));
    }
    @Test
    public void signUp() throws InterruptedException {
        Thread.sleep(5000);
        //click profile page
        onView(withId(R.id.search)).perform(click());
        Thread.sleep(1000);
        onData(is(instanceOf(Event.class))).inAdapterView(withId(R.id.event_list_view
        )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.sign_up)).perform(click());
        Thread.sleep(1000);
        //sign up for event
        onView(withText(android.R.string.ok))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        Thread.sleep(1000);
        //check if signed up
        onView(withText("Signed Up")).check(matches(isDisplayed()));
    }

}
