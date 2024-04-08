package com.example.collatzcheckin.attendee;

import android.graphics.Bitmap;

import com.example.collatzcheckin.event.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User holds user information
 */
public class User implements Serializable {
    private String name;
    private String pfp;
    private String genpfp;
    private String email;
    private List<Event> events;
    private List<String> eventIds;
    private String[] attendingEvents;
    private List<String> organizingEvents;
    private boolean notifications;
    private boolean geolocation;
    private boolean isOrganizer = false;
    private String uid;
    private double latitude;
    private double longitude;
    private boolean is_admin = false;


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
     * @param genpfp user's deterministically generated pfp link
     */
    public User(String uid, String name, String contactInformation, String genpfp) {
        this.name = name;
        this.email = contactInformation;
        this.uid = uid;
        this.events = new ArrayList<Event>();
        this.organizingEvents = new ArrayList<String>();
        this.attendingEvents = new String[0];
        this.geolocation = false;
        this.notifications = false;
        this.pfp = "generated";
        this.genpfp = genpfp;
        this.is_admin = false;
    }
    /**
     * This constructs a user class
     * @param uid The unique identifier for this user to reference in firestore to find their
     *             item collection
     * @param name name of user
     * @param contactInformation user email
     * @param admin does user have administrator permissions
     */
    public User(String uid, String name, String contactInformation, boolean admin) {
        this.name = name;
        this.email = contactInformation;
        this.uid = uid;
        this.events = new ArrayList<Event>();
        this.organizingEvents = new ArrayList<String>();
        this.attendingEvents = new String[0];
        this.geolocation = false;
        this.notifications = false;
        this.is_admin = admin; /// Need to add pfp stuff
    }

    /**
     * This constructs a user class
     * @param name name of user
     * @param contactInformation user email
     * @param uid The unique identifier for this user to reference in firestore to find their
     *              item collection
     */
    public User(String uid, String name, String contactInformation) {
        this.name = name;
        this.email = contactInformation;
        this.uid = uid;
        this.events = new ArrayList<Event>();
        this.organizingEvents = new ArrayList<String>();
        this.attendingEvents = new String[0];
        this.geolocation = false;
        this.notifications = false;
        this.pfp = "generated";
        this.genpfp = "generated";
    }
    /**
     * This constructs a user class
     * @param name name of user
     * @param contactInformation user email
     * @param uid The unique identifier for this user to reference in firestore to find their
     *              item collection
     * @param eventIds List of eventIds the user has signed up for
     */
    public User(String uid, String name, String contactInformation, List<String> eventIds, String pfp) {
        this.name = name;
        this.email = contactInformation;
        this.uid = uid;
        this.events = new ArrayList<Event>();;
        this.organizingEvents = new ArrayList<String>();
        this.attendingEvents = new String[0];
        this.geolocation = false;
        this.notifications = false;
        this.pfp = pfp;
        this.genpfp = "generated";
        this.eventIds = eventIds;
    }

    /**
     * This constructs a user class
     * @param name name of user
     * @param contactInformation user email
     * @param uid The unique identifier for this user to reference in firestore to find their
     *              item collection
     * @param geolocation Geolocation perferences ('true' for enabled, 'false' for disabled)
     * @param notifications Notifications perferences ('true' for enabled, 'false' for disabled)
     */
    public User(String name, String contactInformation, String uid, boolean geolocation, boolean notifications) {
        this.name = name;
        this.email = contactInformation;
        this.uid = uid;
        this.events = new ArrayList<Event>();
        this.organizingEvents = new ArrayList<String>();
        this.attendingEvents = new String[0];
        this.geolocation = geolocation;
        this.notifications = notifications;
        this.pfp = "generated";
        this.genpfp = "generated";
    }

    /**
     * This constructs a user class
     * @param name name of user
     * @param contactInformation user email
     * @param uid The unique identifier for this user to reference in firestore to find their
     *              item collection
     * @param geolocation Geolocation perferences ('true' for enabled, 'false' for disabled)
     * @param notifications Notifications perferences ('true' for enabled, 'false' for disabled)
     * @param pfp user's pfp (can be uploaded or deterministically generated) link
     * @param genpfp user's deterministically generated pfp link
     */
    public User(String name, String contactInformation, String uid, boolean geolocation, boolean notifications, String pfp, String genpfp) {
        this.name = name;
        this.email = contactInformation;
        this.uid = uid;
        this.events = new ArrayList<Event>();
        this.organizingEvents = new ArrayList<String>();
        this.attendingEvents = attendingEvents;
        this.geolocation = geolocation;
        this.notifications = notifications;
        this.pfp = pfp;
        this.genpfp = genpfp;
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
     * @return List<Events> of attending events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Getter for a list of events that the user has signed up to attend
     * @return List<String> of attending events
     */
    public String[] getAttendingEvents() {
        return this.attendingEvents;
    }

    /**
     * Getter for a list of events that the user has organized
     * @return List<String> of organizing events
     */
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

    /**
     * Add an event to the list of events that the user is attending
     * @param event event the user will be attending
     */
    public void AddAttendingEvent(Event event) {
        List<String> tempList = new ArrayList<>(Arrays.asList(this.attendingEvents));
        tempList.add(event.getEventTitle());
        this.attendingEvents = tempList.toArray(new String[0]);
    }

    /**
     * Add an event to the list of events that the user is attending
     * @param event event the user will be attending
     */
    public void addEvent(Event event) {
        events.add(event);
    }

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

    /**
     * Set user's latitude
     * @param latitude captured latitude of user
     */
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    /**
     * Get user's latitude
     * @return captured latitude of user
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Set user's longitude
     * @param longitude captured longitude of user
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Get user's longitude
     * @return captured longitude of user
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setter for user to change geolocation preferences
     * @param geolocation boolean value represents if geolocation is enabled or disabled
     */
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
     * @return string equivalent of notification preferences
     */
    public Boolean getNotifications() {
        if(notifications) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Setter for user to change notification preferences
     * @param notifications boolean value represents if notifications is enabled or disabled
     */
    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    /**
     * Getter for unique identifier generated by Firebase Authenticator which is then used to query for data
     * @return unique identifier
     */
    public String getUid() {
        return uid;
    }

    /**
     * Setter for unique identifier generated by Firebase Authenticator which is then used to query for data
     * @param uid unique identifier
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Getter for if user is an organizer
     * @return boolean value represents if user is an organizer or not
     */
    public boolean isOrganizer() {
        return isOrganizer;
    }

    /**
     * Setter for organizer permissions
     * @param organizer boolean to distinguish organizers
     */
    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }

    /**
     * Getter for deterministically generated profile picture url
     * @return deterministically generated profile picture url
     */
    public String getGenpfp() {
        return genpfp;
    }

    /**
     * Setter for deterministically generated profile picture url
     * @param genpfp deterministically generated profile picture url
     */
    public void setGenpfp(String genpfp) {
        this.genpfp = genpfp;
    }

    /**
     * Getter for if user is an administrator
     * @return boolean value represents if user is an administrator or not
     */
    public boolean isAdmin() {
        return is_admin;
    }
    /**
     * Setter for admin permissions
     * @param is_admin boolean to distinguish users from administrators
     */
    public void setAdmin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    /**
     * Getter for list of event ids
     * @return list of event ids
     */
    public List<String> getEventIds() {
        return eventIds;
    }
    /**
     * Setter for list of event ids
     * @param eventIds list of event ids
     */
    public void setEventIds(List<String> eventIds) {
        this.eventIds = eventIds;
    }


}