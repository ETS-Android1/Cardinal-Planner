package com.example.cardinalPlanner.model;

/**
 * This is a class used to get info on the logged in user
 */
public class Users {

    private String name;
    private String email;

    /**
     * default constructor
     */
    public Users() {}

    /**
     * Create a user with name and email
     * @param name - username
     * @param email - users email
     */
    public Users(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * @return Gets the users name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the string of the name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Gets the users name
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets the string of email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
