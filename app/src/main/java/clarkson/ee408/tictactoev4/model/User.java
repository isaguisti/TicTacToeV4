package clarkson.ee408.tictactoev4.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * User class; models a User that will play a TicTacToe game
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String displayName;
    private boolean online;

    // CONSTRUCTORS
    /**
     * Default constructor for a User object; sets all variables to their default values
     */
    public User() {
        this(null, null, null, false);
    }

    /**
     * Parameterized constructor for a User object
     * @param username User's username; of type String
     * @param password User's password; of type String
     * @param displayName User's display name; of type String
     * @param online User's online "status"; of type boolean
     */
    public User(String username, String password, String displayName, boolean online) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.online = online;
    }

    // GET AND SET METHODS
    /**
     * Get method to retrieve a User's username
     * @return Returns the User's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get method to retrieve a User's password
     * @return Returns the User's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get method to retrieve a User's display name
     * @return Returns the User's display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get method to retrieve whether a User is online
     * @return Returns whether the User is online or not
     */
    public boolean getOnline() {
        return online;
    }


    /**
     * Sets a username for a User
     * @param username String representation of User's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets a password for a User
     * @param password String representation of User's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets a display name for a User
     * @param displayName String representation of User's display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Sets whether a User is online or not
     * @param online Boolean which indicates if a User is online or not
     */
    public void setOnline(boolean online) {
        this.online = online;
    }


    /**
     * Overrides the equals method; uses the username variable as the unique attribute and
     * tests if 'o' is equal to the current object and if 'o' is null or not in the same class
     * @param o Has the type Object
     * @return Returns true if both Users are equal; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    /**
     * Writes the hashcode of an object using username as the unique attribute
     * @return Returns the objects hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Converts a User object into a string
     * @return Returns the string with all User variables
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", displayName='" + displayName + '\'' +
                ", online=" + online +
                '}';
    }
}