package com.example.cardinalPlanner.model;

import java.util.Date;
import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * This class is for the ToDo_object refered to as TD so compiler doesnt confure with todo_
 */
@IgnoreExtraProperties
public class ToDo {
    /**
     * TD date
     */
    public static final String FIELD_DATE = "Date";
    /**
     * TD name
     */
    public static final String FIELD_NAME = "name";
    /**
     * flag for if the TD is completed
     */
    public static final String FIELD_COMPLETE = "complete";
    /**
     * Description of the TD
     */
    public static final String FIELD_DESCRIPTION = "description";
    /**
     * Flag for if notifications persist past date
     */
    public static final String FIELD_PUC = "persistantUntilComplete";
    /**
     * Flag for notifications
     */
    public static final String FIELD_NOTIFICATION = "notification";
    /**
     * Creating user id
     */
    public static final String FIELD_USERID = "userId";

    private Date date;
    private boolean complete;
    private String description;
    private String Name;
    private boolean notification;
    private boolean persistantUntilComplete;
    private String userId;

    /**
     * default contructor
     */
    public ToDo() {}

    /**
     * Constructor if user has all the required variabels
     * @param date - the day the TD is due
     * @param complete - flag for if TD is complete
     * @param description - TD desctiption
     * @param Name - TD name
     * @param notification - flag for if TD has notifications
     * @param persistantUntilComplete - flag for if notifications persist until marked as complete
     * @param userId - the id of the user who created
     */
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

    /**
     * Sets the TD date
     * @param date - java data when TD is due
     */
    public void setDate(Date date) { this.date = date; }

    /**
     * @return the TD date
     */
    public Date getDate() { return date; }
    /**
     * Sets the TD name
     * @param Name - string
     */
    public void setName(String Name) { this.Name = Name; }
    /**
     * @return the TD name
     */
    public String getName() { return Name; }
    /**
     * Sets the TD persitance flag
     * @param persistantUntilComplete - boolean
     */
    public void setPersistantUntilComplete( boolean persistantUntilComplete) { this.persistantUntilComplete = persistantUntilComplete; }
    /**
     * @return the TD persistant status
     */
    public boolean getPersistantUntilComplete() { return persistantUntilComplete; }
    /**
     * Sets the TD complet flag
     * @param complete - string
     */
    public void setComplete(boolean complete) { this.complete = complete; }
    /**
     * @return the TD complete flag
     */
    public boolean getComplete() { return complete; }
    /**
     * Sets the TD description
     * @param description - string
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * @return the TD description
     */
    public String getDescription() { return description; }
    /**
     * Sets the TD notification flag
     * @param notification - boolen
     */
    public void setNotification(boolean notification) { this.notification = notification; }
    /**
     * @return the TD notification flag
     */
    public boolean getNotification() { return notification; }
    /**
     * Sets the TD crated user id
     * @param userId - string
     */
    public void setUserId(String userId) { this.userId = userId; }
    /**
     * @return the TD user id
     */
    public String getUserId() { return userId; }


}
