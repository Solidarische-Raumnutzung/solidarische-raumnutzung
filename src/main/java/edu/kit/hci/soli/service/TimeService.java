package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Room;

import java.time.LocalDateTime;

public interface TimeService {
    /**
     * Retrieves the current time slot.
     *
     * @return the current time slot
     */
    LocalDateTime currentSlot();

    /**
     * Retrieves the minimum starting time for a booking.
     *
     * @param room the room for which the minimum time is calculated
     * @return the minimum time for a booking
     */
    LocalDateTime minimumTime(Room room);

    /**
     * Retrieves the maximum ending time for a booking, which is 14 days from now.
     *
     * @param room the room for which the maximum time is calculated
     * @return the maximum time for a booking
     */
    LocalDateTime maximumTime(Room room);

    /**
     * Retrieves the current time for the configured time zone.
     *
     * @return the current time
     */
    LocalDateTime now();
}
