package edu.kit.hci.soli.service.impl;

import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.dto.CalendarEvent;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class BookingsServiceImpl implements BookingsService {
    private final BookingsRepository bookingsRepository;
    private final EmailService emailService;

    /**
     * Constructs a BookingsService with the specified {@link BookingsRepository}.
     *
     * @param bookingsRepository the repository for managing Booking entities
     * @param emailService       the service for sending emails
     */
    public BookingsServiceImpl(BookingsRepository bookingsRepository, EmailService emailService) {
        this.bookingsRepository = bookingsRepository;
        this.emailService = emailService;
    }

    /**
     * Classifies the conflict type between two bookings.
     *
     * @param booking the new booking
     * @param other   the existing booking
     * @return the type of conflict
     */
    private ConflictType classifyConflict(Booking booking, Booking other) {
        final boolean mayCooperate = !ShareRoomType.NO.equals(booking.getShareRoomType());
        if (booking.getPriority().compareTo(other.getPriority()) < 0) { // counterintuitive, but higher priorities come first in the enum
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
     * Retrieves the CSS classes for a calendar event based on the booking and user.
     *
     * @param booking the booking associated with the event
     * @param user    the user associated with the event (nullable)
     * @return a list of CSS classes for the event
     */
    private List<String> getEventClasses(Booking booking, @Nullable User user) {
        List<String> classes = new ArrayList<>();
        classes.add("calendar-event-" + booking.getPriority().name().toLowerCase());
        classes.add("calendar-event-" + booking.getShareRoomType().name().toLowerCase());
        if (booking.getUser().equals(user)) classes.add("calendar-event-own");
        return classes;
    }

    @Transactional
    @Override
    public BookingAttemptResult attemptToBook(Booking booking) {
        if (!booking.getOpenRequests().isEmpty()) throw new IllegalArgumentException("Booking has outstanding requests");
        if (booking.getId() != null) throw new IllegalArgumentException("Booking already saved");
        List<Booking> override = new ArrayList<>();
        List<Booking> contact = new ArrayList<>();
        List<Booking> cooperate = new ArrayList<>();
        List<Booking> conflict = new ArrayList<>();
        try (Stream<Booking> bs = bookingsRepository.findOverlappingBookings(booking.getRoom(), booking.getStartDate(), booking.getEndDate())) {
            bs.forEach(b -> (switch (classifyConflict(booking, b)) {
                case OVERRIDE -> override;
                case CONFLICT -> b.getOpenRequests().isEmpty() ? conflict : override;
                case COOPERATE -> cooperate;
                case CONTACT -> b.getOpenRequests().isEmpty() ? contact : cooperate;
            }).add(b));
        }
        if (!conflict.isEmpty()) return new BookingAttemptResult.Failure(conflict);
        if (!contact.isEmpty()) return new BookingAttemptResult.PossibleCooperation.Deferred(override, contact, cooperate);
        if (!cooperate.isEmpty() || !override.isEmpty()) return new BookingAttemptResult.PossibleCooperation.Immediate(override, cooperate);
        return new BookingAttemptResult.Success(bookingsRepository.save(booking));
    }

    @Override
    @Transactional
    public void deleteAllBookingsForUser(User user) {
        bookingsRepository.deleteAllByUser(user);
    }

    @Transactional
    @Override
    public BookingAttemptResult affirm(Booking booking, BookingAttemptResult.PossibleCooperation result) {
        BookingAttemptResult attemptResult = attemptToBook(booking); // Note: this also ensures that the booking is not already saved
        if (attemptResult.equals(result)) {
            return switch (result) {
                case BookingAttemptResult.PossibleCooperation.Immediate(var override, var cooperate) -> {
                    override.forEach(b -> delete(b, BookingDeleteReason.CONFLICT));
                    yield new BookingAttemptResult.Success(bookingsRepository.save(booking));
                }
                case BookingAttemptResult.PossibleCooperation.Deferred(var override, var contact, var cooperate) -> {
                    Set<User> openRequests = contact.stream().map(Booking::getUser).collect(Collectors.toSet());
                    booking.setOpenRequests(openRequests);
                    booking = bookingsRepository.save(booking);
                    for (User request : openRequests) {
                        emailService.sendMail(
                                request,
                                "bookings.collaboration",
                                "mail/collaboration_request",
                                Map.of(
                                        "booking", booking
                                )
                        );
                    }
                    yield new BookingAttemptResult.Staged(booking);
                }
            };
        }
        return attemptResult;
    }

    @Override
    public Booking getBookingById(Long id) {
        return id == null ? null : bookingsRepository.findById(id).orElse(null);
    }

    @Override
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

    @Transactional
    @Override
    public boolean confirmRequest(Booking stagedBooking, User user) {
        boolean result = stagedBooking.getOpenRequests().remove(user);
        bookingsRepository.save(stagedBooking);
        if (stagedBooking.getOpenRequests().isEmpty()) {
            try (Stream<Booking> bs = bookingsRepository.findOverlappingBookings(stagedBooking.getRoom(), stagedBooking.getStartDate(), stagedBooking.getEndDate())) {
                bs.filter(s -> !s.getOpenRequests().isEmpty())
                        .forEach(b -> {
                            switch (classifyConflict(stagedBooking, b)) {
                                case OVERRIDE, CONFLICT -> delete(b, BookingDeleteReason.CONFLICT);
                                default -> {}
                            }
                        });
            }
        }
        return result;
    }

    @Transactional
    @Override
    public boolean denyRequest(Booking stagedBooking, User user) {
        if (stagedBooking.getOpenRequests().remove(user)) {
            bookingsRepository.delete(stagedBooking);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Page<Booking> getBookingsByUser(User user, Room room, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingsRepository.findByUserAndRoom(user, room, pageable);
    }

    @Override
    public boolean hasBookings(User user) {
        return bookingsRepository.existsByUser(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CalendarEvent> getCalendarEvents(Room room, LocalDateTime start, LocalDateTime end, @Nullable User user) {
        try (Stream<Booking> bs = bookingsRepository.findOverlappingBookings(room, start, end)) {
            return bs.filter(s -> s.getOpenRequests().isEmpty())
                    .map(booking -> new CalendarEvent(
                            "/" + booking.getRoom().getId() + "/bookings/" + booking.getId(),
                            "",
                            booking.getStartDate(),
                            booking.getEndDate(),
                            getEventClasses(booking, user)
                    ))
                    .toList();
        }
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

    @Override
    public LocalDateTime currentSlot() {
        return normalize(LocalDateTime.now());
    }

    @Override
    public LocalDateTime minimumTime() {
        return normalize(LocalDateTime.now().plusMinutes(15));
    }

    @Override
    public LocalDateTime maximumTime() {
        return normalize(LocalDateTime.now().plusDays(14));
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
}
