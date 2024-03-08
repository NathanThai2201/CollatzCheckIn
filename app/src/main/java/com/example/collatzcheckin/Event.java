package com.example.collatzcheckin;


import java.io.Serializable;

/**
 * This is a class that defines an event.
 */
public class Event implements Serializable {
    private String eventTitle;
    private String eventID;
    private String eventOrganizer;
    private String eventDate;
    private String eventDescription;
    private String eventPoster;
    private String eventLocation;
    private int memberLimit;
    /**
     * This constructs a  user class where atrributes are not set
     *
     *  @param eventTitle The unique identifier for this user to reference in firestore to find their
     */

    public Event(String eventTitle, String eventOrganizer, String eventDate, String eventDescription, String eventPoster, String eventLocation) {
        this.eventTitle = eventTitle;
        this.eventOrganizer = eventOrganizer;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventPoster = eventPoster;
        this.eventLocation = eventLocation;
        this.eventID = eventID;
    }

    public Event(String eventTitle, String eventOrganizer, String eventDate, String eventDescription, String eventPoster, String eventLocation, int memberLimit) {
        this.eventTitle = eventTitle;
        this.eventOrganizer = eventOrganizer;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventPoster = eventPoster;
        this.eventLocation = eventLocation;
        this.memberLimit = memberLimit;
        this.eventID = eventID;
    }

    public Event(String eventTitle, String eventOrganizer, String eventDate, String eventDescription, String eventPoster, int memberLimit) {
        this.eventTitle = eventTitle;
        this.eventOrganizer = eventOrganizer;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventPoster = eventPoster;
        this.eventLocation = eventLocation;
        this.memberLimit = memberLimit;
        this.eventID = eventID;
    }

    /**
     * Getter for Event title
     * @return Event title
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * Setter for Event title
     * @param  eventTitle
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * Getter for EventOrganizer
     * @return Event EventOrganizer
     */
    public String getEventOrganizer() {
        return this.eventOrganizer;
    }

    /**
     * Setter for Event Organizer
     * @param  eventOrganizer
     */
    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    /**
     * Getter for EventDate
     * @return EventDate
     */
    public String getEventDate() {
        return this.eventDate;
    }

    /**
     * Setter for Event Organizer
     * @param  eventDate
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Getter for EventDescription
     * @return EventDescription
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Setter for Event eventDescription
     * @param  eventDescription
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Getter for EventPoster
     * @return EventPoster
     */
    public String getEventPoster() {
        return eventPoster;
    }

    /**
     * Setter for Event eventDescription
     * @param  eventPoster
     */
    public void setEventPoster(String eventPoster) {
        this.eventPoster = eventPoster;
    }

    /**
     * Getter for memberLimit
     * @return memberLimit
     */

    public int getMemberLimit() {
        return memberLimit;
    }

    /**
     * Setter for Event memberLimit
     * @param  memberLimit
     */
    public void setMemberLimit(int memberLimit) {
        this.memberLimit = memberLimit;
    }

    /**
     * Getter for eventLocation
     * @return eventLocation
     */
    public String getEventLocation() {
        return eventLocation;
    }

    /**
     * Setter for eventLocation
     * @param eventLocation
     */
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    /**
     * Getter for id
     * @return eventID
     */
    public String getId() {
        return eventID;
    }
    /**
     * Setter for eventLocation
     * @param eventID
     */
    public void setId(String eventID) {
        this.eventID = eventID;
    }
}
