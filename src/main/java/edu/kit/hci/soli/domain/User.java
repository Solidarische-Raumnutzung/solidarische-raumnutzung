package edu.kit.hci.soli.domain;

import jakarta.persistence.*;

/**
 * The datamodel for a User as it is stored in the database
 */
@Entity
@Table(name = "soli_users")
public class User {
    /**
     * The internal identifier for the user.
     * This is the primary key in the database and is automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The username of the user.
     * This is not unique and may change.
     * It is only used for display purposes.
     */
    private String username;

    /**
     * The email address of the user.
     * This is not unique and may change.
     * It is used for communication with the user.
     */
    private String email;

    /**
     * The user ID, which is a unique identifier for the user.
     * This contains information to integrate with our various identity providers.
     */
    private String userId;

    /**
     * Indicates whether the user is disabled.
     * A disabled user cannot create bookings but can still view or delete them.
     */
    private boolean isDisabled;

    public User(Long id, String username, String email, String userId, boolean isDisabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userId = userId;
        this.isDisabled = isDisabled;
    }

    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUserId() {
        return this.userId;
    }

    public boolean isDisabled() {
        return this.isDisabled;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", username=" + this.getUsername() + ", email=" + this.getEmail() + ", userId=" + this.getUserId() + ", isDisabled=" + this.isDisabled() + ")";
    }
}
