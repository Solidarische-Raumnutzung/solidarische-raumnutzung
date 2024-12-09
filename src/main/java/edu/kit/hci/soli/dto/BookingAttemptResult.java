package edu.kit.hci.soli.dto;

import edu.kit.hci.soli.domain.Booking;

import java.util.List;

/**
 * Algebraic data type representing the result of a booking attempt.
 */
public sealed interface BookingAttemptResult {
    /**
     * Represents a successful booking attempt.
     */
    record Success(Booking booking) implements BookingAttemptResult { }

    /**
     * Represents a booking attempt that was successful but requires confirmation by one or more users.
     */
    record Staged(Booking booking) implements BookingAttemptResult { }

    /**
     * Represents a booking attempt that has failed because of an unresolveable conflict with a booking of higher or equal priority.
     */
    record Failure(List<Booking> conflict) implements BookingAttemptResult { }

    /**
     * Algebraic data type representing a failed booking attempt that may be resolved by cooperation.
     */
    sealed interface PossibleCooperation extends BookingAttemptResult {
        /**
         * Gets the list of bookings that would have to be overwritten for this cooperation.
         *
         * @return the list of bookings to override
         */
        List<Booking> override();

        /**
         * Gets the list of bookings that would happen concurrently to this booking.
         *
         * @return the list of bookings to cooperate with
         */
        List<Booking> cooperate();

        /**
         * Gets the list of bookings that require the permission for this cooperation.
         * If this is not empty, the successful result of affirming this cooperation would be of type {@link Staged}.
         *
         * @return the list of bookings to contact
         */
        List<Booking> contact();

        /**
         * Represents an immediate possible cooperation in a booking attempt.
         * The successful result of affirming this cooperation is an immediate {@link Success}.
         */
        record Immediate(List<Booking> override, List<Booking> cooperate) implements PossibleCooperation {
            @Override
            public List<Booking> contact() {
                return List.of();
            }
        }

        /**
         * Represents a deferred possible cooperation in a booking attempt.
         * The successful result of affirming this cooperation is of type {@link Staged} (and, as such, will require confirmation by others).
         */
        record Deferred(List<Booking> override, List<Booking> contact, List<Booking> cooperate) implements PossibleCooperation { }
    }
}
