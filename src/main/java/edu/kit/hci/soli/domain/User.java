package edu.kit.hci.soli.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

/**
 * The datamodel for a User as it is stored in the database
 */
@Entity
@Table(name = "soli_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public boolean equals(Object o) {
        return o instanceof User u && u.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
