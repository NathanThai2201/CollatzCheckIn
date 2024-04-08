package com.example.collatzcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AttendeeProfileTest {
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
    public void profileActivity() throws InterruptedException {
        Thread.sleep(1000);
        //click profile page
        onView(withId(R.id.profile)).perform(click());
        Thread.sleep(5000);
        //click on update
        onView(withId(R.id.up_button)).perform(click());
        Thread.sleep(5000);
        //Update user info
        onView(withId(R.id.editName)).perform(clearText(), typeText("FirstName LastName"));
        onView(withId(R.id.editEmail)).perform(typeText("Email"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirm_button)).perform(click());
        Thread.sleep(2000);
        onView(withText("FirstName LastName")).check(matches(isDisplayed()));
    }

}
