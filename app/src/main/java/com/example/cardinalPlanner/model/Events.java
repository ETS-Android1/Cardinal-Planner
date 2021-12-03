package com.example.cardinalPlanner.model;
import java.util.*;
import com.google.firebase.firestore.IgnoreExtraProperties;

/*
Events POJO
 */
@IgnoreExtraProperties
public class Events {
    public static final String FIELD_DATE = "Date";
    public static final String FIELD_NAME = "Name";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_MEET = "meetingLink";
    public static final String FIELD_NOTIFICATION = "notification";
    public static final String FIELD_USERID = "userId";

    private Date date;
    private String Name;
    private String category;
    private String description;
    private String meetingLink;
    private boolean notification;
    private String userId;

    public Events() {}

    public Events(Date date, String Name, String category, String description, String meetingLink,
                 boolean notification, String userId){
        this.date = date;
        this.Name = Name;
        this.category = category;
        this.description = description;
        this.meetingLink = meetingLink;
        this.notification = notification;
        this.userId = userId;
    }

    public void setDate(Date date) { this.date = date; }
    public Date getDate() { return date; }
    public void setName(String Name) { this.Name = Name; }
    public String getName() { return Name; }
    public void setCategory(String category) { this.category = category; }
    public String getCategory() { return category; }
    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return description; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public String getMeetingLink() { return meetingLink; }
    public void setNotification(boolean notification) { this.notification = notification; }
    public boolean getNotification() { return notification; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserId() { return userId; }





}
