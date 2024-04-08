package com.example.collatzcheckin;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static androidx.fragment.app.testing.FragmentScenario.launchInContainer;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.common.base.CharMatcher.is;
import static com.google.common.base.Predicates.instanceOf;
import static java.util.EnumSet.allOf;

import android.util.Log;

import com.example.collatzcheckin.admin.controls.events.AdminEventViewFragment;
import com.example.collatzcheckin.attendee.AttendeeDB;
import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.event.Event;
import com.example.collatzcheckin.event.EventDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class AdminTest {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Rule
    public ActivityScenarioRule<AdminMainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AdminMainActivity.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();
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
    public void testAdminBrowse() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.search)).perform(click());
        Thread.sleep(5000);
        // Check if the browse events button is displayed
        onView(withId(R.id.events_button)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the browse events as user button is displayed
        onView(withId(R.id.signup_button)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the browse profiles button is displayed
        onView(withId(R.id.profiles_button)).check(matches(isDisplayed()));


    }
    @Test
    public void testAdminEvent() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.search)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.events_button)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.event_list_view)).perform(click());
        Thread.sleep(5000);
        // Check if the delete button is displayed
        onView(withId(R.id.delete_event)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the delete image button is displayed
        onView(withId(R.id.remove_event_poster)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the back button is displayed
        onView(withId(R.id.back_button_event_view)).check(matches(isDisplayed()));

        Thread.sleep(1000);
        // Check if the event title text view is displayed
        onView(withId(R.id.event_title)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the event description text view is displayed
        onView(withId(R.id.event_description)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the event location text view is displayed
        onView(withId(R.id.event_location)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the event month text view is displayed
        onView(withId(R.id.event_month)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the event day text view is displayed
        onView(withId(R.id.event_day)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the event time text view is displayed
        onView(withId(R.id.event_time)).check(matches(isDisplayed()));
    }
    @Test
    public void testAdminProfiles() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.search)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.profiles_button)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.user_list_view)).perform(click());
        Thread.sleep(5000);
        // Check if the delete user button is displayed
        onView(withId(R.id.delete_user)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the delete image button is displayed
        onView(withId(R.id.remove_image)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the back button is displayed
        onView(withId(R.id.back_button_user_view)).check(matches(isDisplayed()));

        Thread.sleep(1000);
        // Check if the edit name text is displayed
        onView(withId(R.id.editName)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the edit email text is displayed
        onView(withId(R.id.editEmail)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the location switch is displayed
        onView(withId(R.id.enablegeo)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the notification switch is displayed
        onView(withId(R.id.enablenotif)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the imageview is displayed
        onView(withId(R.id.editpfp)).check(matches(isDisplayed()));

    }
    @Test
    public void testAdminSignup() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.search)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.signup_button)).perform(click());
        Thread.sleep(5000);
        // Check if the searchbar is displayed
        onView(withId(R.id.search_view)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the listview is displayed
        onView(withId(R.id.event_list_view)).check(matches(isDisplayed()));

    }


    @Test
    public void testAdminEventDelete() throws InterruptedException {
        EventDB eventDB = new EventDB();
        HashMap<String, String> attendees = new HashMap<>();
        Event event = new Event("TestEventForDelete", "TestOrganizer", "April 8 2024 16:00", "DeleteMe", "Test", "Test", 1, "000000TESTEVENTIDFORDELETE", attendees);
        eventDB.addEvent(event);



        Thread.sleep(1000);
        onView(withId(R.id.search)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.events_button)).perform(click());
        Thread.sleep(5000);
        onView(withText(event.getEventTitle())).check(matches(isDisplayed())).perform(scrollTo());
        Thread.sleep(5000);
        onView(withText(event.getEventTitle())).perform(click());
        Thread.sleep(5000);
        // Check if the delete button is displayed
        onView(withId(R.id.delete_event)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the delete image button is displayed
        onView(withId(R.id.remove_event_poster)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        onView(withId(R.id.delete_event)).perform(click());

        eventDB.eventRef.document("000000TESTEVENTIDFORDELETE").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        thrown.expectMessage("Doc still exists in Firestore. Need to try again or add wait time for async");
                    } else {
                        Log.d("testAdminEventDelete", "Document does not exist!");
                    }
                } else {
                    Log.d("testAdminEventDelete", "Could not pull document from firestore. Successful deletion");
                }
            }
        });

        }
    @Test
    public void testAdminProfileDelete() throws InterruptedException {
        AttendeeDB attendeeDB = new AttendeeDB();
        User user = new User("0000000TESTDELETEUSER", "TESTUSER", "test@email.com");
        attendeeDB.addUser(user);



        Thread.sleep(1000);
        onView(withId(R.id.search)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.profiles_button)).perform(click());
        Thread.sleep(5000);
        onView(withText(user.getName())).check(matches(isDisplayed())).perform(scrollTo());
        Thread.sleep(5000);
        onView(withText(user.getName())).perform(click());
        Thread.sleep(5000);
        // Check if the delete button is displayed
        onView(withId(R.id.delete_user)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        onView(withId(R.id.delete_user)).perform(click());

        attendeeDB.userRef.document("0000000TESTDELETEUSER").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        thrown.expectMessage("Doc still exists in Firestore. Need to try again or add wait time for async");
                    } else {
                        Log.d("testAdminUserDelete", "Document does not exist!");
                    }
                } else {
                    Log.d("testAdminUserDelete", "Could not pull document from firestore. Successful deletion");
                }
            }
        });

    }

    @Test
    public void testAdminEventPosterDelete() throws InterruptedException {
        EventDB eventDB = new EventDB();
        HashMap<String, String> attendees = new HashMap<>();
        Event event = new Event("TestEventForDelete", "TestOrganizer", "April 8 2024 16:00", "DeleteMe", "https://firebasestorage.googleapis.com/v0/b/collatzcheckin.appspot.com/o/generatedpfp%2FG.png?alt=media&token=ebe3d9f3-acd9-4b56-ac58-f2a3ef41fccc", "Test", 1, "000000TESTEVENTIDFORDELETE", attendees);
        eventDB.addEvent(event);


        Thread.sleep(1000);
        onView(withId(R.id.search)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.events_button)).perform(click());
        Thread.sleep(5000);
        onView(withText(event.getEventTitle())).check(matches(isDisplayed())).perform(scrollTo());
        Thread.sleep(5000);
        onView(withText(event.getEventTitle())).perform(click());
        Thread.sleep(5000);
        // Check if the delete button is displayed
        onView(withId(R.id.delete_event)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        // Check if the delete image button is displayed
        onView(withId(R.id.remove_event_poster)).check(matches(isDisplayed()));
        Thread.sleep(5000);
        onView(withId(R.id.remove_event_poster)).perform(click());
        
        Thread.sleep(5000);
        onView(withId(R.id.delete_event)).perform(click());

    }
    }
