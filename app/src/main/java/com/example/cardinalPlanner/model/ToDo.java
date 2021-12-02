package com.example.cardinalPlanner.model;

import java.util.Date;
import com.google.firebase.firestore.IgnoreExtraProperties;

/*
ToDos POJO
 */
@IgnoreExtraProperties
public class ToDo {
    public static final String FIELD_DATE = "Date";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_COMPLETE = "complete";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_PUC = "persistantUntilComplete";
    public static final String FIELD_NOTIFICATION = "notification";
    public static final String FIELD_USERID = "userId";

    private Date date;
    private boolean complete;
    private String description;
    private String Name;
    private boolean notification;
    private boolean persistantUntilComplete;
    private String userId;

    public ToDo() {}

    public ToDo(Date date, boolean complete, String description, String Name, boolean notification,
                boolean persistantUntilComplete, String userId) {
        this.complete = complete;
        this.description = description;
        this.date = date;
        this.notification = notification;
        this.Name = Name;
        this.persistantUntilComplete = persistantUntilComplete;
        this.userId = userId;
    }

    public void setDate(Date date) { this.date = date; }
    public Date getDate() { return date; }
    public void setName(String Name) { this.Name = Name; }
    public String getName() { return Name; }
    public void setPersistantUntilComplete( boolean persistantUntilComplete) { this.persistantUntilComplete = persistantUntilComplete; }
    public boolean getPersistantUntilComplete() { return persistantUntilComplete; }
    public void setComplete(boolean complete) { this.complete = complete; }
    public boolean getComplete() { return complete; }
    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return description; }
    public void setNotification(boolean notification) { this.notification = notification; }
    public boolean getNotification() { return notification; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserId() { return userId; }
}
