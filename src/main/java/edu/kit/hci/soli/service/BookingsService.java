package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.*;
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
        return new BookingsService.BookingAttemptResult.Success(bookingsRepository.save(booking));
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
        BookingAttemptResult attemptResult = attemptToBook(booking);
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
        if (stagedBooking.getOutstandingRequests().isEmpty()) {
            switch (attemptToBook(stagedBooking)) {
                case BookingAttemptResult.Failure(var conflict) -> log.error("Failed to confirm request for booking {} by user {} due to conflict with: {}", stagedBooking, user, conflict);
                case BookingAttemptResult.Staged staged -> log.error("Failed to confirm request for booking {} by user {} due to unexpected staging", stagedBooking, user);
                case BookingAttemptResult.Success success -> log.info("Confirmed request for booking {} by user {}", stagedBooking, user);
                case BookingAttemptResult.PossibleCooperation possible -> possible.override().forEach(b -> delete(b, BookingDeleteReason.CONFLICT));
            }
        }
        return result;
    }

    public enum BookingDeleteReason {
        CONFLICT, ADMIN, SELF
    }

    public List<Booking> getBookingsByUser(User user, Room room) {
        return bookingsRepository.findByUser(user, room);
    }

    @Transactional(readOnly = true)
    public List<CalendarEvent> getCalendarEvents(LocalDateTime start, LocalDateTime end) {
        return bookingsRepository.findOverlappingBookings(start, end)
                .filter(s -> s.getOutstandingRequests().isEmpty())
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
        record Success(Booking booking) implements BookingAttemptResult { }
        record Staged(Booking booking) implements BookingAttemptResult { }
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
