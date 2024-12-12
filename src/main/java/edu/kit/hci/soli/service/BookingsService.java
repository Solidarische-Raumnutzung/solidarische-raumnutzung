package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.ShareRoomType;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.dto.CalendarEvent;
import edu.kit.hci.soli.repository.BookingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing {@link Booking} entities.
 * Provides methods to register and retreive booking details.
 */
@Slf4j
@Service
public class BookingsService {
    private final BookingsRepository bookingsRepository;
    private final EmailService emailService;
    private final MailProperties mailProperties;

    /**
     * Constructs a BookingsService with the specified {@link BookingsRepository}.
     *
     * @param bookingsRepository the repository for managing Booking entities
     * @param emailService the service for sending emails
     */
    public BookingsService(BookingsRepository bookingsRepository, EmailService emailService, MailProperties mailProperties) {
        this.bookingsRepository = bookingsRepository;
        this.emailService = emailService;
        this.mailProperties = mailProperties;
    }

    /**
     * Attempts to book a room. If successful, the booking is saved and Success is returned.
     * If there are unresolvable conflicts, Failure is returned.
     * If the booking is possible but has effects the user should be informed about, PossibleCooperation is returned.
     *
     * @param booking the booking to be attempted
     * @return the result of the booking attempt
     */
    @Transactional
    public BookingAttemptResult attemptToBook(Booking booking) {
        if (!booking.getOutstandingRequests().isEmpty()) throw new IllegalArgumentException("Booking has outstanding requests");
        if (booking.getId() != null) throw new IllegalArgumentException("Booking already saved");
        List<Booking> override = new ArrayList<>();
        List<Booking> contact = new ArrayList<>();
        List<Booking> cooperate = new ArrayList<>();
        List<Booking> conflict = new ArrayList<>();
        bookingsRepository.findOverlappingBookings(booking.getRoom(), booking.getStartDate(), booking.getEndDate())
                .forEach(b -> (switch (classifyConflict(booking, b)) {
                    case OVERRIDE -> override;
                    case CONFLICT -> b.getOutstandingRequests().isEmpty() ? conflict : override;
                    case COOPERATE -> cooperate;
                    case CONTACT -> b.getOutstandingRequests().isEmpty() ? contact : cooperate;
                }).add(b));
        if (!conflict.isEmpty()) return new BookingAttemptResult.Failure(conflict);
        if (!contact.isEmpty()) return new BookingAttemptResult.PossibleCooperation.Deferred(override, contact, cooperate);
        if (!cooperate.isEmpty() || !override.isEmpty()) return new BookingAttemptResult.PossibleCooperation.Immediate(override, cooperate);
        return new BookingAttemptResult.Success(bookingsRepository.save(booking));
    }

    /**
     * Classifies the conflict type between two bookings.
     *
     * @param booking the new booking
     * @param other the existing booking
     * @return the type of conflict
     */
    private ConflictType classifyConflict(Booking booking, Booking other) {
        final boolean mayCooperate = !ShareRoomType.NO.equals(booking.getShareRoomType());
        if (booking.getPriority().compareTo(other.getPriority()) < 0) {
            return mayCooperate && ShareRoomType.YES.equals(other.getShareRoomType()) ? ConflictType.COOPERATE : ConflictType.OVERRIDE;
        } else {
            return mayCooperate ? switch (other.getShareRoomType()) {
                case YES -> ConflictType.COOPERATE;
                case NO -> ConflictType.CONFLICT;
                case null -> ConflictType.CONFLICT;
                case ON_REQUEST -> ConflictType.CONTACT;
            } : ConflictType.CONFLICT;
        }
    }

    /**
     * Enum representing the types of conflicts that can occur between bookings.
     */
    private enum ConflictType {
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

    /**
     * Deletes all bookings for a specified user.
     *
     * @param user the user whose bookings are to be deleted
     */
    public void deleteAllBookingsForUser(User user) {
        bookingsRepository.deleteAllByUser(user);
    }

    /**
     * Affirms that a possible booking is to be made.
     * This is equivalent to the user confirming the results of an attempt to book.
     * If something has changed in the meantime, a new affirmation may be requested via a returned PossibleCooperation.
     * If some conflicts require contact, a Staged result is returned.
     *
     * @param booking the booking to be affirmed
     * @param result the result of the booking attempt
     * @return the result of the affirmation
     */
    @Transactional
    public BookingAttemptResult affirm(Booking booking, BookingAttemptResult.PossibleCooperation result) {
        BookingAttemptResult attemptResult = attemptToBook(booking); // Note: this also ensures that the booking is not already saved
        if (attemptResult.equals(result)) {
            return switch (result) {
                case BookingAttemptResult.PossibleCooperation.Immediate(var override, var cooperate) -> {
                    override.forEach(b -> delete(b, BookingDeleteReason.CONFLICT));
                    yield new BookingAttemptResult.Success(bookingsRepository.save(booking));
                }
                case BookingAttemptResult.PossibleCooperation.Deferred(var override, var contact, var cooperate) -> {
                    booking.setOutstandingRequests(contact.stream().map(Booking::getUser).collect(Collectors.toSet()));
                    yield new BookingAttemptResult.Staged(bookingsRepository.save(booking));
                }
            };
        }
        return attemptResult;
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param id the ID of the booking
     * @return the booking with the specified ID, or null if not found
     */
    public Booking getBookingById(Long id) {
        return bookingsRepository.findById(id).orElse(null);
    }

    /**
     * Deletes a booking for a specified reason.
     *
     * @param booking the booking to be deleted
     * @param reason the reason for deletion
     */
    public void delete(Booking booking, BookingDeleteReason reason) {
        bookingsRepository.delete(booking);
        emailService.sendMail(
                booking.getUser(),
                "mail.booking_deleted.subject",
                "mail/booking_deleted",
                Map.of(
                        "booking", booking,
                        "reason", reason
                )
        );
    }

    /**
     * Confirms a request created as the result of an affirmation of a deferred booking.
     * This is the action taken by a user that has an existing booking marked with {@link ShareRoomType#ON_REQUEST} and confirms that a different user may book the room.
     *
     * @param stagedBooking the booking to be confirmed
     * @param user the user who confirms the booking
     * @return true if the user was in the list of outstanding requests and was removed
     */
    @Transactional
    public boolean confirmRequest(Booking stagedBooking, User user) {
        //TODO make this available in a controller and send the URL via E-Mail
        boolean result = stagedBooking.getOutstandingRequests().remove(user);
        bookingsRepository.save(stagedBooking);
        if (stagedBooking.getOutstandingRequests().isEmpty()) {
            bookingsRepository.findOverlappingBookings(stagedBooking.getRoom(), stagedBooking.getStartDate(), stagedBooking.getEndDate())
                    .filter(s -> !s.getOutstandingRequests().isEmpty())
                    .forEach(b -> {
                        switch (classifyConflict(stagedBooking, b)) {
                            case OVERRIDE, CONFLICT -> delete(b, BookingDeleteReason.CONFLICT);
                            default -> {}
                        }
                    });
        }
        return result;
    }

    /**
     * Retrieves bookings for a specified user and room.
     *
     * @param user the user associated with the bookings
     * @param room the room associated with the bookings
     * @return a list of bookings for the specified user and room
     */
    public List<Booking> getBookingsByUser(User user, Room room) {
        return bookingsRepository.findByUserAndRoom(room, user);
    }

    /**
     * Retrieves calendar events within a specified time range for a user.
     *
     * @param start the start of the time range
     * @param end the end of the time range
     * @param user the user associated with the events (nullable)
     * @return a list of calendar events within the specified time range
     */
    @Transactional(readOnly = true)
    public List<CalendarEvent> getCalendarEvents(Room room, LocalDateTime start, LocalDateTime end, @Nullable User user) {
        return bookingsRepository.findOverlappingBookings(room, start, end)
                .filter(s -> s.getOutstandingRequests().isEmpty())
                .map(booking -> new CalendarEvent(
                        "/" + booking.getRoom().getId() + "/bookings/" + booking.getId(),
                        "",
                        booking.getStartDate(),
                        booking.getEndDate(),
                        getEventClasses(booking, user)
                ))
                .toList();
    }

    /**
     * Retrieves the CSS classes for a calendar event based on the booking and user.
     *
     * @param booking the booking associated with the event
     * @param user the user associated with the event (nullable)
     * @return a list of CSS classes for the event
     */
    private List<String> getEventClasses(Booking booking, @Nullable User user) {
        List<String> classes = new ArrayList<>();
        classes.add("calendar-event-" + booking.getPriority().name().toLowerCase());
        classes.add("calendar-event-" + booking.getShareRoomType().name().toLowerCase());
        if (booking.getUser().equals(user)) classes.add("calendar-event-own");
        return classes;
    }

    /**
     * Retrieves the current time slot.
     *
     * @return the current time slot
     */
    public LocalDateTime currentSlot() {
        return normalize(LocalDateTime.now());
    }

    /**
     * Retrieves the minimum starting time for a booking.
     *
     * @return the minimum time for a booking
     */
    public LocalDateTime minimumTime() {
        return normalize(LocalDateTime.now().plusMinutes(15));
    }

    /**
     * Retrieves the maximum ending time for a booking, which is 14 days from now.
     *
     * @return the maximum time for a booking
     */
    public LocalDateTime maximumTime() {
        return normalize(LocalDateTime.now().plusDays(14));
    }

    /**
     * Normalizes a given time to the nearest 15 minutes, computing the corresponding time slot.
     *
     * @param time the time to be normalized
     * @return the normalized time
     */
    private LocalDateTime normalize(LocalDateTime time) {
        return time.minusMinutes(time.getMinute() % 15).withSecond(0).withNano(0);
    }
}
