package com.example.cardinalPlanner.model;
import java.util.*;
import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 The model for Events
 */
@IgnoreExtraProperties
public class Events {
    /**
     * The event date
     */
    public static final String FIELD_DATE = "Date";
    /**
     * the events name
     */
    public static final String FIELD_NAME = "Name";
    /**
     * The events category
     */
    public static final String FIELD_CATEGORY = "category";
    /**
     * The events description
     */
    public static final String FIELD_DESCRIPTION = "description";
    /**
     * The events link
     */
    public static final String FIELD_MEET = "meetingLink";
    /**
     * True/false on if the event has notifications
     */
    public static final String FIELD_NOTIFICATION = "notification";
    /**
     * The events creating user
     */
    public static final String FIELD_USERID = "userId";

    private Date date;
    private String Name;
    private String category;
    private String description;
    private String meetingLink;
    private boolean notification;
    private String userId;
    private List<String> listIDs;

    /**
     * Default constructor
     */
    public Events() {}

    /**
     * Constructor givin all initilixzatrion valuies
     * @param date - day of event
     * @param Name - name of event
     * @param category - category of event
     * @param description - event description
     * @param meetingLink - link to meeting
     * @param notification - if the event has notificatrions
     * @param userId - user who created event
     * @param listIDs - all users who can see the event
     */
    public Events(Date date, String Name, String category, String description, String meetingLink,
                 boolean notification, String userId, List<String> listIDs){
        this.date = date;
        this.Name = Name;
        this.category = category;
        this.description = description;
        this.meetingLink = meetingLink;
        this.notification = notification;
        this.userId = userId;
        this.listIDs = listIDs;
    }

    /**
     * @return the list of users who have the event shared with them
     */
    public List<String> getListIDs() {
        return listIDs;
    }

    /**
     * sets the list of sharable users
     * @param listIDs - lsit of ids s of shared with users
     */
    public void setListIDs(List<String> listIDs) {
        this.listIDs = listIDs;
    }

    /**
     * sets the date
     * @param date - java Date for event date
     */
    public void setDate(Date date) { this.date = date; }

    /**
     *
     * @return the event date
     */
    public Date getDate() { return date; }

    /**
     * Sets the event name
     * @param Name - string
     */
    public void setName(String Name) { this.Name = Name; }

    /**
     * @return the event name
     */
    public String getName() { return Name; }
    /**
     * Sets the event category
     * @param category - string
     */
    public void setCategory(String category) { this.category = category; }
    /**
     * @return the event category
     */
    public String getCategory() { return category; }
    /**
     * Sets the event description
     * @param description - string
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * @return the event description
     */
    public String getDescription() { return description; }
    /**
     * Sets the event link
     * @param meetingLink - string
     */
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    /**
     * @return the event link
     */
    public String getMeetingLink() { return meetingLink; }
    /**
     * Sets the event notification flag
     * @param notification - boolean
     */
    public void setNotification(boolean notification) { this.notification = notification; }
    /**
     * @return the event notification flag
     */
    public boolean getNotification() { return notification; }
    /**
     * Sets the event cxreating suer id
     * @param userId - int
     */
    public void setUserId(String userId) { this.userId = userId; }
    /**
     * @return the event creating user id
     */
    public String getUserId() { return userId; }





}
