package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.ShareRoomType;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.dto.CalendarEvent;
import edu.kit.hci.soli.repository.BookingsRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Service class for managing {@link Booking} entities.
 * Provides methods to register and retreive booking details.
 */
public interface BookingsService {

    /**
     * Attempts to book a room. If successful, the booking is saved and Success is returned.
     * If there are unresolvable conflicts, Failure is returned.
     * If the booking is possible but has effects the user should be informed about, PossibleCooperation is returned.
     *
     * @param booking the booking to be attempted
     * @return the result of the booking attempt
     */
    BookingAttemptResult attemptToBook(Booking booking);

    /**
     * Deletes all bookings for a specified user.
     *
     * @param user the user whose bookings are to be deleted
     */
    void deleteAllBookingsForUser(User user);

    /**
     * Affirms that a possible booking is to be made.
     * This is equivalent to the user confirming the results of an attempt to book.
     * If something has changed in the meantime, a new affirmation may be requested via a returned PossibleCooperation.
     * If some conflicts require contact, a Staged result is returned.
     *
     * @param booking the booking to be affirmed
     * @param result  the result of the booking attempt
     * @return the result of the affirmation
     */
    BookingAttemptResult affirm(Booking booking, BookingAttemptResult.PossibleCooperation result);

    /**
     * Retrieves a booking by its ID.
     *
     * @param id the ID of the booking
     * @return the booking with the specified ID, or null if not found
     */
    Booking getBookingById(Long id);

    /**
     * Deletes a booking for a specified reason.
     *
     * @param booking the booking to be deleted
     * @param reason  the reason for deletion
     */
    void delete(Booking booking, BookingDeleteReason reason);

    /**
     * Confirms a request created as the result of an affirmation of a deferred booking.
     * This is the action taken by a user that has an existing booking marked with {@link ShareRoomType#ON_REQUEST} and confirms that a different user may book the room.
     *
     * @param stagedBooking the booking to be confirmed
     * @param user          the user who confirms the booking
     * @return true if the user was in the list of outstanding requests and was removed
     */
    boolean confirmRequest(Booking stagedBooking, User user);

    /**
     * Denies a request created as the result of an affirmation of a deferred booking.
     * This is the action taken by a user that has an existing booking marked with {@link ShareRoomType#ON_REQUEST} and denies that a different user may book the room.
     *
     * @param stagedBooking the booking to be denied
     * @param user          the user who denies the booking
     * @return true if the user was in the list of outstanding requests, and the request was denied
     */
    boolean denyRequest(Booking stagedBooking, User user);

    /**
     * Retrieves bookings for a specified user and room.
     *
     * @param user the user associated with the bookings
     * @param room the room associated with the bookings
     * @param page the page number
     * @param size the number of items per page
     * @return a list of bookings for the specified user and room
     */
    Page<Booking> getBookingsByUser(User user, Room room, int page, int size);

    /**
     * Checks if a user has any bookings.
     *
     * @param user the user to be checked
     * @return true if the user has any bookings, false otherwise
     */
    boolean hasBookings(User user);

    /**
     * Retrieves calendar events within a specified time range for a user.
     *
     * @param start the start of the time range
     * @param end   the end of the time range
     * @param user  the user associated with the events (nullable)
     * @return a list of calendar events within the specified time range
     */
    List<CalendarEvent> getCalendarEvents(Room room, LocalDateTime start, LocalDateTime end, @Nullable User user);

    /**
     * Exports a booking in the iCalendar format.
     *
     * @param booking the booking
     * @param locale the locale for wich to localize
     * @return the content for the .ics file
     */
    String getICalendar(Booking booking, Locale locale);

    /**
     * Update the description for a booking.
     *
     * @param booking the booking
     * @param description the new description
     */
    void updateDescription(Booking booking, String description);

    /**
     * Gets the booking of the highest priority, which is right now if there is one.
     *
     * @return the current booking
     */
    Optional<Booking> getCurrentHighestBooking(Room room, LocalDateTime time);

    /**
     * Gets booking of logged-in user, which is at the given time if there is one.
     *
     * @return thr current booking of user if there is one
     */
    Optional<Booking> getCurrentBookingOfUser(Room room, LocalDateTime time, User user);
}
