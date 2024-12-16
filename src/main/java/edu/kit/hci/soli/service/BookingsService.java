package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.ShareRoomType;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.dto.CalendarEvent;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BookingsService {
    @Transactional
    BookingAttemptResult attemptToBook(Booking booking);

    void deleteAllBookingsForUser(User user);

    @Transactional
    BookingAttemptResult affirm(Booking booking, BookingAttemptResult.PossibleCooperation result);

    Booking getBookingById(Long id);

    void delete(Booking booking, BookingDeleteReason reason);

    @Transactional
    boolean confirmRequest(Booking stagedBooking, User user);

    List<Booking> getBookingsByUser(User user, Room room);

    @Transactional(readOnly = true)
    List<CalendarEvent> getCalendarEvents(Room room, LocalDateTime start, LocalDateTime end, @Nullable User user);

    LocalDateTime currentSlot();

    LocalDateTime minimumTime();

    LocalDateTime maximumTime();

    /**
     * Enum representing the types of conflicts that can occur between bookings.
     */
    enum ConflictType {
        /**
         * Indicates that the new booking overrides the existing booking.
         */
        OVERRIDE,
        /**
         * Indicates that the new booking requires contact with the owner of the existing booking.
         */
        CONTACT,
        /**
         * Indicates that the new booking can cooperate with the existing booking.
         */
        COOPERATE,
        /**
         * Indicates that the new booking conflicts with the existing booking.
         */
        CONFLICT
    }
}
