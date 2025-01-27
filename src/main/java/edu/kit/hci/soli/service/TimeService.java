package edu.kit.hci.soli.service;

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
     * @return the minimum time for a booking
     */
    LocalDateTime minimumTime();

    /**
     * Retrieves the maximum ending time for a booking, which is 14 days from now.
     *
     * @return the maximum time for a booking
     */
    LocalDateTime maximumTime();

    /**
     * Retrieves the current time for the configured time zone.
     *
     * @return the current time
     */
    LocalDateTime now();
}
