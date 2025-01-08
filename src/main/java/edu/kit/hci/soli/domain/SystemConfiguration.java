package edu.kit.hci.soli.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.ToString;

/**
 * Entity class representing the system configuration.
 * This table is designed to contain only a single row.
 */
@Entity
@Table(name = "soli_system_configuration")
@ToString
public class SystemConfiguration {
    /**
     * The unique identifier for the system configuration.
     * This is always set to 1 to enforce a single row.
     */
    @Id
    private Integer id = 1;

    /**
     * Flag indicating whether guest login is enabled.
     */
    private boolean guestLoginEnabled;

    /**
     * Gets the value of the guestLoginEnabled flag.
     *
     * @return true if guest login is enabled, false otherwise.
     */
    public boolean isGuestLoginEnabled() {
        return guestLoginEnabled;
    }

    /**
     * Sets the value of the guestLoginEnabled flag.
     *
     * @param guestLoginEnabled true to enable guest login, false to disable it.
     */
    public void setGuestLoginEnabled(boolean guestLoginEnabled) {
        this.guestLoginEnabled = guestLoginEnabled;
    }
}
