package com.example.collatzcheckin;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Event {
    private String eventTitle;
    private User eventOrganizer;
    private String eventDate;
    private String eventDescription;
    private String eventPoster;
    private int memberLimit;
    public Event(String eventTitle, User eventOrganizer, String eventDate, String eventDescription, String eventPoster, int memberLimit) {
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

    public String getEventOrganizer() {
        return eventOrganizer.getName();
    }

    public void setEventOrganizer(User eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
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