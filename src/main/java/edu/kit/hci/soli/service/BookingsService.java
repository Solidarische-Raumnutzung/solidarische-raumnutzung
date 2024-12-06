package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.*;
import edu.kit.hci.soli.repository.BookingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingsService {
    private final BookingsRepository bookingsRepository;

    public  BookingsService(BookingsRepository bookingsRepository){
        this.bookingsRepository = bookingsRepository;
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
        bookingsRepository.findOverlappingBookings(booking.getStartDate(), booking.getEndDate())
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

    private enum ConflictType {
        OVERRIDE, CONTACT, COOPERATE, CONFLICT
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

    public Booking getBookingById(Long id) {
        return bookingsRepository.findById(id).orElse(null);
    }

    public void delete(Booking booking, BookingDeleteReason reason) {
        bookingsRepository.delete(booking);
        //TODO send notification to user
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
            bookingsRepository.findOverlappingBookings(stagedBooking.getStartDate(), stagedBooking.getEndDate())
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

    public List<Booking> getBookingsByUser(User user, Room room) {
        return bookingsRepository.findByUser(user, room);
    }

    @Transactional(readOnly = true)
    public List<CalendarEvent> getCalendarEvents(LocalDateTime start, LocalDateTime end, @Nullable User user) {
        return bookingsRepository.findOverlappingBookings(start, end)
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

    private List<String> getEventClasses(Booking booking, @Nullable User user) {
        List<String> classes = new ArrayList<>();
        classes.add("calendar-event-" + booking.getPriority().name().toLowerCase());
        classes.add("calendar-event-" + booking.getShareRoomType().name().toLowerCase());
        if (booking.getUser().equals(user)) classes.add("calendar-event-own");
        return classes;
    }

    public LocalDateTime currentSlot() {
        return normalize(LocalDateTime.now());
    }

    public LocalDateTime minimumTime() {
        return normalize(LocalDateTime.now().plusMinutes(15));
    }

    public LocalDateTime maximumTime() {
        return normalize(LocalDateTime.now().plusDays(14));
    }

    private LocalDateTime normalize(LocalDateTime time) {
        return time.minusMinutes(time.getMinute() % 15).withSecond(0).withNano(0);
    }
}
