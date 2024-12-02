package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.repository.BookingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BookingsService {

    private final BookingsRepository bookingsRepository;

    public  BookingsService(BookingsRepository bookingsRepository){
        this.bookingsRepository = bookingsRepository;
    }

    @Transactional
    public BookingAttemptResult attemptToBook(Booking booking) {
        List<Booking> override = new ArrayList<>();
        List<Booking> contact = new ArrayList<>();
        List<Booking> cooperate = new ArrayList<>();
        List<Booking> conflict = new ArrayList<>();
        bookingsRepository.findOverlappingBookings(booking.getStartDate(), booking.getEndDate())
                .forEach(b -> (switch (classifyConflict(booking, b)) {
                    case OVERRIDE -> override;
                    case CONTACT -> contact;
                    case COOPERATE -> cooperate;
                    case CONFLICT -> conflict;
                }).add(b));
        if (!conflict.isEmpty()) return new BookingAttemptResult.Failure(conflict);
        if (!contact.isEmpty()) return new BookingAttemptResult.PossibleCooperation.Deferred(override, contact, cooperate);
        if (!cooperate.isEmpty() || !override.isEmpty()) return new BookingAttemptResult.PossibleCooperation.Immediate(override, cooperate);
        bookingsRepository.save(booking);
        return new BookingsService.BookingAttemptResult.Success();
    }

    private ConflictType classifyConflict(Booking booking, Booking other) {
        final boolean mayCooperate = !booking.getShareRoomType().equals(ShareRoomType.NO);
        if (booking.getPriority().compareTo(other.getPriority()) < 0) {
            return mayCooperate && other.getShareRoomType().equals(ShareRoomType.YES) ? ConflictType.COOPERATE : ConflictType.OVERRIDE;
        } else {
            return mayCooperate ? switch (other.getShareRoomType()) {
                case YES -> ConflictType.COOPERATE;
                case NO -> ConflictType.CONFLICT;
                case ON_REQUEST -> ConflictType.CONTACT;
            } : ConflictType.CONFLICT;
        }
    }

    private enum ConflictType {
        OVERRIDE, CONTACT, COOPERATE, CONFLICT
    }

    @Transactional
    public BookingAttemptResult affirm(Booking booking, BookingAttemptResult.PossibleCooperation result) {
        BookingAttemptResult attemptResult = attemptToBook(booking);
        if (attemptResult.equals(result)) {
            switch (result) {
                case BookingAttemptResult.PossibleCooperation.Immediate(var override, var cooperate) -> {
                    override.forEach(b -> deleteBooking(b, BookingDeleteReason.CONFLICT));
                    bookingsRepository.save(booking);
                    return new BookingAttemptResult.Success();
                }
                case BookingAttemptResult.PossibleCooperation.Deferred(var override, var contact, var cooperate) -> {
                    //TODO send notifications, wait for response and save attempted booking
                    throw new IllegalArgumentException("Not yet implemented");
                }
            }
        }
        return attemptResult;
    }

    public void deleteBooking(Booking booking, BookingDeleteReason reason) {
        bookingsRepository.delete(booking);
        //TODO send notification to user
    }

    public enum BookingDeleteReason {
        CONFLICT, ADMIN
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingsRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<CalendarEvent> getCalendarEvents(LocalDateTime start, LocalDateTime end) {
        return bookingsRepository.findOverlappingBookings(start, end)
                .map(booking -> new BookingsService.CalendarEvent(
                        booking.getPriority().name(), //TODO we should localize this and/or insert it via CSS
                        booking.getStartDate(),
                        booking.getEndDate(),
                        List.of("event-" + booking.getPriority().name().toLowerCase()) //TODO if we own the event, we should add a class to highlight it
                ))
                .toList();
    }

    public record CalendarEvent(String title, LocalDateTime start, LocalDateTime end, List<String> className) { }

    public sealed interface BookingAttemptResult {
        record Success() implements BookingAttemptResult { }
        record Failure(List<Booking> conflict) implements BookingAttemptResult { }
        sealed interface PossibleCooperation extends BookingAttemptResult {
            List<Booking> override();
            List<Booking> cooperate();
            List<Booking> contact();
            record Immediate(List<Booking> override, List<Booking> cooperate) implements PossibleCooperation {
                @Override
                public List<Booking> contact() {
                    return List.of();
                }
            }
            record Deferred(List<Booking> override, List<Booking> contact, List<Booking> cooperate) implements PossibleCooperation { }
        }
    }
}
