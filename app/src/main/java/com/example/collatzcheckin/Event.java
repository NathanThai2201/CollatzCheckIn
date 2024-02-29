package com.example.collatzcheckin;

import java.util.Date;

public class Event {
    private String eventTitle;
    private Organizer eventOrganizer;
    private Date eventDate;
    private String eventDescription;
    private String eventPoster;
    private int memberLimit;
    public Event(String eventTitle, Organizer eventOrganizer, Date eventDate, String eventDescription, String eventPoster, int memberLimit) {
        this.eventTitle = eventTitle;
        this.eventOrganizer = eventOrganizer;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventPoster = eventPoster;
        this.memberLimit = memberLimit;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Organizer getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(Organizer eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventPoster() {
        return eventPoster;
    }

    public void setEventPoster(String eventPoster) {
        this.eventPoster = eventPoster;
    }

    public int getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(int memberLimit) {
        this.memberLimit = memberLimit;
    }
}
