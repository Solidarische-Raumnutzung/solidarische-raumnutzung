package edu.kit.hci.soli.domain;

import jakarta.persistence.*;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

/**
 * The datamodel for a User as it is stored in the database.
 */
@Entity
@Table(name = "soli_users")
@ToString
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
     * The locale of the user.
     * This is used to send mails in the user's preferred language.
     */
    private Locale locale;

    /**
     * The date and time of the last login of the user.
     */
    private @NotNull LocalDateTime lastLogin;

    /**
     * Constructs a new User with the specified details.
     *
     * @param id         the unique identifier for the user
     * @param username   the username of the user
     * @param email      the email address of the user
     * @param userId     the unique user ID
     * @param isDisabled whether the user is disabled
     * @param locale     the locale of the user
     * @param lastLogin  time of the last login of the user
     */
    public User(Long id, String username, String email, String userId, boolean isDisabled, Locale locale, LocalDateTime lastLogin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userId = userId;
        this.isDisabled = isDisabled;
        this.locale = locale;
        this.lastLogin = lastLogin;
    }

    /**
     * Default constructor for User.
     */
    public User() {
        // JPA requires us to provide this constructor without arguments
        // Since this means we cannot access our services or configuration,
        // we just guess reasonable defaults for these.
        // They should get updated as soon as the object is actually used,
        // so this should be fine.
        this.locale = Locale.getDefault();
        this.lastLogin = LocalDateTime.now();
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
     * Gets the locale of the user.
     *
     * @return the locale of the user
     */
    public Locale getLocale() {
        return locale;
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

    /**
     * Sets the locale of the user.
     * This is used to send mails in the user's preferred language.
     *
     * @param locale the locale of the user
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean equals(final Object o) {
        return o instanceof User u && Objects.equals(u.getId(), getId());
    }

    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Gets the date and time of the last login of the user.
     *
     * @return the date and time of the last login of the user
     */
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets the date and time of the last login of the user.
     *
     * @param lastLogin the date and time of the last login of the user
     */
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
