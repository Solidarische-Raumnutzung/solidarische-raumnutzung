package edu.kit.hci.soli.dto;

/**
 * Enumeration representing the reasons for deleting a booking.
 */
public enum BookingDeleteReason {
    /**
     * Indicates that the booking was deleted due to a conflict.
     */
    CONFLICT,

    /**
     * Indicates that the booking was deleted by an admin.
     */
    ADMIN,

    /**
     * Indicates that the booking was deleted by the user themselves.
     */
    SELF
}
