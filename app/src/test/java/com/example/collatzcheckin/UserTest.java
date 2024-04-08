package com.example.collatzcheckin;

import com.example.collatzcheckin.attendee.User;
import com.example.collatzcheckin.event.Event;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {
    private User user;
    private Event event;

    @Before
    public void setUp() {
        user = new User("1234", "John Doe", "john.doe@example.com");
        event = new Event("Title", "Organizer", "Date", "Description", "PosterURl", "Location", "ID");
    }

    @Test
    public void testGetName() {
        assertEquals("John Doe", user.getName());
    }

    @Test
    public void testSetName() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    public void testGetPfp() {
        assertEquals("generated", user.getPfp());
    }

    @Test
    public void testSetPfp() {
        user.setPfp("custom_url");
        assertEquals("custom_url", user.getPfp());
    }

    @Test
    public void testGetEmail() {
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    public void testSetEmail() {
        user.setEmail("jane.doe@example.com");
        assertEquals("jane.doe@example.com", user.getEmail());
    }

    @Test
    public void testGetEvents() {
        List<Event> events = user.getEvents();
        assertTrue(events.isEmpty());
    }

    @Test
    public void testGetAttendingEvents() {
        String[] attendingEvents = user.getAttendingEvents();
        assertEquals(0, attendingEvents.length);
    }

    @Test
    public void testGetOrganizingEvents() {
        List<String> organizingEvents = user.getOrganizingEvents();
        assertTrue(organizingEvents.isEmpty());
    }

    @Test
    public void testAddOrganizingEvent() {
        user.addOrganizingEvent(event);
        List<String> organizingEvents = user.getOrganizingEvents();
        assertEquals(1, organizingEvents.size());
        assertEquals(event.getEventTitle(), organizingEvents.get(0));
    }

    @Test
    public void testAddAttendingEvent() {
        user.AddAttendingEvent(event);
        String[] attendingEvents = user.getAttendingEvents();
        assertEquals(1, attendingEvents.length);
        assertEquals(event.getEventTitle(), attendingEvents[0]);
    }

    @Test
    public void testIsGeolocation() {
        assertFalse(user.isGeolocation());
    }

    @Test
    public void testGetGeolocation() {
        assertFalse(user.getGeolocation());
    }

    @Test
    public void testSetGeolocation() {
        user.setGeolocation(true);
        assertTrue(user.isGeolocation());
    }

    @Test
    public void testIsNotifications() {
        assertFalse(user.isNotifications());
    }

    @Test
    public void testGetNotifications() {
        assertFalse(user.getNotifications());
    }

    @Test
    public void testSetNotifications() {
        user.setNotifications(true);
        assertTrue(user.isNotifications());
    }

    @Test
    public void testGetUid() {
        assertEquals("1234", user.getUid());
    }

    @Test
    public void testSetUid() {
        user.setUid("5678");
        assertEquals("5678", user.getUid());
    }

    @Test
    public void testIsOrganizer() {
        assertFalse(user.isOrganizer());
    }

    @Test
    public void testSetOrganizer() {
        user.setOrganizer(true);
        assertTrue(user.isOrganizer());
    }

    @Test
    public void testGetGenpfp() {
        assertEquals("generated", user.getGenpfp());
    }

    @Test
    public void testSetGenpfp() {
        user.setGenpfp("custom_genpfp");
        assertEquals("custom_genpfp", user.getGenpfp());
    }

    @Test
    public void testIsAdmin() {
        assertFalse(user.isAdmin());
    }

    @Test
    public void testSetAdmin() {
        user.setAdmin(true);
        assertTrue(user.isAdmin());
    }
}

