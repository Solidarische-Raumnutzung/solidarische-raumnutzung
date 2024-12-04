package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.*;
import edu.kit.hci.soli.repository.BookingsRepository;
import lombok.extern.slf4j.Slf4j;
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
    public List<CalendarEvent> getCalendarEvents(LocalDateTime start, LocalDateTime end) {
        return bookingsRepository.findOverlappingBookings(start, end)
                .filter(s -> s.getOutstandingRequests().isEmpty())
                .map(booking -> new CalendarEvent(
                        booking.getPriority().name(), //TODO we should localize this and/or insert it via CSS
                        booking.getStartDate(),
                        booking.getEndDate(),
                        List.of("event-" + booking.getPriority().name().toLowerCase()) //TODO if we own the event, we should add a class to highlight it
                ))
                .toList();
    }
}
