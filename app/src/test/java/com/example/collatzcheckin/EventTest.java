package com.example.collatzcheckin;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.example.collatzcheckin.event.Event;

public class EventTest {
    private Event event;

    @Before
    public void setUp() {
        event = new Event("TestEvent", "Organizer", "2022-04-01", "Description", "Poster", "Location", "ID");
    }

    @Test
    public void testGetEventTitle() {
        assertEquals("TestEvent", event.getEventTitle());
    }

    @Test
    public void testSetEventTitle() {
        event.setEventTitle("NewEventTitle");
        assertEquals("NewEventTitle", event.getEventTitle());
    }

    @Test
    public void testGetEventOrganizer() {
        assertEquals("Organizer", event.getEventOrganizer());
    }

    @Test
    public void testSetEventOrganizer() {
        event.setEventOrganizer("NewOrganizer");
        assertEquals("NewOrganizer", event.getEventOrganizer());
    }

    @Test
    public void testGetEventDate() {
        assertEquals("2022-04-01", event.getEventDate());
    }

    @Test
    public void testSetEventDate() {
        event.setEventDate("2022-04-02");
        assertEquals("2022-04-02", event.getEventDate());
    }

    @Test
    public void testGetEventDescription() {
        assertEquals("Description", event.getEventDescription());
    }

    @Test
    public void testSetEventDescription() {
        event.setEventDescription("NewDescription");
        assertEquals("NewDescription", event.getEventDescription());
    }

    @Test
    public void testGetEventPoster() {
        assertEquals("Poster", event.getEventPoster());
    }

    @Test
    public void testSetEventPoster() {
        event.setEventPoster("NewPoster");
        assertEquals("NewPoster", event.getEventPoster());
    }

    @Test
    public void testGetMemberLimit() {
        assertEquals(0, event.getMemberLimit()); // Default value is 0
    }

    @Test
    public void testSetMemberLimit() {
        event.setMemberLimit(100);
        assertEquals(100, event.getMemberLimit());
    }

    @Test
    public void testGetEventLocation() {
        assertEquals("Location", event.getEventLocation());
    }

    @Test
    public void testSetEventLocation() {
        event.setEventLocation("NewLocation");
        assertEquals("NewLocation", event.getEventLocation());
    }

    @Test
    public void testGetEventID() {
        assertEquals("ID", event.getEventID());
    }

    @Test
    public void testSetEventID() {
        event.setEventID("NewID");
        assertEquals("NewID", event.getEventID());
    }

    @Test
    public void testGetAttendees() {
        assertNull(event.getAttendees()); // Default value is null
    }

}

