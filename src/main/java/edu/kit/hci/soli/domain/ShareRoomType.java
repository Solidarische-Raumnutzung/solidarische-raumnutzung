package edu.kit.hci.soli.domain;

/**
 * Enumeration representing the types of room sharing options.
 */
public enum ShareRoomType {
    /**
     * Indicates that other bookings can be made in the same time slots without requiring permission.
     */
    YES,

    /**
     * Indicates that a booking is not compatible with other bookings and, in the case of a conflict, may only be overwritten.
     */
    NO,

    /**
     * Indicates that making a booking in the same time slot requires permission from the original booking.
     */
    ON_REQUEST
}
