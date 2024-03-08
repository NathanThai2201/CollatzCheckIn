package com.example.collatzcheckin;

import android.net.Uri;

import com.example.collatzcheckin.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User holds user information
 */
public class User implements Serializable {
    private String name;
    private String  pfp;
    private String username;
    private String email;
    private List<String> attendingEvents;
    private List<String> organizingEvents;
    private boolean notifications;
    private boolean geolocation;
    private boolean isOrganizer = false;
    private String uid;


    /**
     * This constructs a  user class where atrributes are not set
     * This is used for the Firebase Authentication
     */
    public User() {
    }

    /**
     * This constructs a user class where unique identifier is set
     * This is used for the Firebase Authentication
     * @param uid The unique identifier for this user to reference in firestore to find their
     *              item collection
     */
    public User(String uid) {
        this.uid = uid;
    }

    /**
     * This constructs a user class
     * @param uid The unique identifier for this user to reference in firestore to find their
     *             item collection
     * @param name name of user
     * @param contactInformation user email
     */
    public User(String uid, String name, String contactInformation) {
        this.name = name;
        this.email = contactInformation;
        this.uid = uid;
        this.organizingEvents = new ArrayList<String>();
        this.attendingEvents = new ArrayList<String>();
        this.geolocation = false;
        this.notifications = false;
    }

    /**
     * This constructs a user class
     * @param name name of user
     * @param username username for homepage
     * @param contactInformation user email
     * @param uid The unique identifier for this user to reference in firestore to find their
     *              item collection
     */
    public User( String name, String username, String contactInformation, String uid) {
        this.name = name;
        this.email = contactInformation;
        this.uid = uid;
        this.organizingEvents = new ArrayList<String>();
        this.attendingEvents = new ArrayList<String>();
        this.username = username;
        this.geolocation = false;
        this.notifications = false;

    }

    /**
     * This constructs a user class
     * @param name name of user
     * @param username username for homepage
     * @param contactInformation user email
     * @param uid The unique identifier for this user to reference in firestore to find their
     *              item collection
     * @param geolocation Geolocation perferences ('true' for enabled, 'false' for disabled)
     * @param notifications Notifications perferences ('true' for enabled, 'false' for disabled)
     */
    public User( String name, String username, String contactInformation, String uid, boolean geolocation, boolean notifications) {
        this.name = name;
        this.email = contactInformation;
        this.uid = uid;
        this.organizingEvents = new ArrayList<String>();
        this.attendingEvents = new ArrayList<String>();
        this.username = username;
        this.geolocation = geolocation;
        this.notifications = notifications;

    }

    /**
     * Getter for name
     * @return users preferred name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name users preferred name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for profile picture url
     * @return profile picture url
     */
    public String getPfp() {
        return pfp;
    }

    /**
     * Setter for profile picture url
     * @param pfp profile picture url
     */
    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    /**
     * Getter for username for homepage
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username for homepage
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for user email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for user email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for a list of events that the user has signed up to attend
     * @return email
     */
    public List<String> getAttendingEvents() {
        return this.attendingEvents;
    }
    public List<String> getOrganizingEvents() {
        return this.organizingEvents;
    }

    /**
     * Add an event to the list of events that the user is attending
     * @param event event the user will be attending
     */
    public void addOrganizingEvent(Event event) {

        organizingEvents.add(event.getEventTitle());
    }
    public void AddAttendingEvent(Event event) {
        attendingEvents.add(event.getEventTitle());}


    /**
     * Getter for if user has geolocation enabled
     * @return boolean value represents if geolocation is enabled or disabled
     */
    public boolean isGeolocation() {
        return geolocation;
    }

    /**
     * Convert boolean value to string equivalent
     * @return string equivalent of geolocation perferences
     */
    public Boolean getGeolocation() {
        if(geolocation) {
            return true;
        } else {
            return false;
        }
    }

    public void setGeolocation(boolean geolocation) {
        this.geolocation = geolocation;
    }

    /**
     * Getter for if user has notifications enabled
     * @return boolean value represents if notifications is enabled or disabled
     */
    public boolean isNotifications() {
        return notifications;
    }

    /**
     * Convert boolean value to string equivalent
     * @return string equivalent of notification perferences
     */
    public boolean getNotifications() {
        if(notifications) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Setter for user to change notifcation perferences
     * @param notifications boolean value represents if notifications is enabled or disabled
     */
    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    /**
     * Getter for unique idenifier generated by Firebase Authenicator which is then used to query for data
     * @return unique idenifier
     */
    public String getUid() {
        return uid;
    }

    /**
     * Setter for unique idenifier generated by Firebase Authenicator which is then used to query for data
     * @param uid unique idenifier
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }

}