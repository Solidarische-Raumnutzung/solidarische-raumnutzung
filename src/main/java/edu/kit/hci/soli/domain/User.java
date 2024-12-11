package edu.kit.hci.soli.domain;

import jakarta.persistence.*;

/**
 * The datamodel for a User as it is stored in the database.
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

    /**
     * Constructs a new User with the specified details.
     *
     * @param id         the unique identifier for the user
     * @param username   the username of the user
     * @param email      the email address of the user
     * @param userId     the unique user ID
     * @param isDisabled whether the user is disabled
     */
    public User(Long id, String username, String email, String userId, boolean isDisabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userId = userId;
        this.isDisabled = isDisabled;
    }

    /**
     * Default constructor for User.
     */
    public User() {
    }

    /**
     * Gets the unique identifier for the user.
     *
     * @return the unique identifier for the user
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets the unique user ID.
     *
     * @return the unique user ID
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Checks if the user is disabled.
     *
     * @return true if the user is disabled, false otherwise
     */
    public boolean isDisabled() {
        return this.isDisabled;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id the unique identifier for the user
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the unique user ID.
     *
     * @param userId the unique user ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Sets whether the user is disabled.
     *
     * @param isDisabled true if the user is disabled, false otherwise
     */
    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User other = (User) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$userId = this.getUserId();
        final Object other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
        if (this.isDisabled() != other.isDisabled()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $userId = this.getUserId();
        result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
        result = result * PRIME + (this.isDisabled() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", username=" + this.getUsername() + ", email=" + this.getEmail() + ", userId=" + this.getUserId() + ", isDisabled=" + this.isDisabled() + ")";
    }
}