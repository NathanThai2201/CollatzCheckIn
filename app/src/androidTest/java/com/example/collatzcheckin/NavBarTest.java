package com.example.collatzcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class NavBarTest {
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
