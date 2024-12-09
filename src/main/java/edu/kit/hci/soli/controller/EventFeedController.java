package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.dto.CalendarEvent;
import edu.kit.hci.soli.service.BookingsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for generating the FullCalendar event feed.
 */
@RestController("/api/events")
public class EventFeedController {
    final BookingsService bookingsRepository;

    /**
     * Constructs an EventFeedController with the specified {@link BookingsService}.
     *
     * @param bookingsRepository the service for managing bookings
     */
    public EventFeedController(BookingsService bookingsRepository) {
        this.bookingsRepository = bookingsRepository;
    }

    /**
     * Retrieves calendar events within the specified time range.
     *
     * @param start the start date and time
     * @param end the end date and time
     * @param principal the authenticated user details
     * @return the list of calendar events
     */
    @GetMapping("/api/events")
    public List<CalendarEvent> getEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @AuthenticationPrincipal SoliUserDetails principal
    ) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (end.minusMonths(3).isAfter(start)) {
            throw new IllegalArgumentException("Time range must be less than 3 months");
        }

        return bookingsRepository.getCalendarEvents(start, end, principal == null ? null : principal.getUser());
    }

    /**
     * Handles {@link IllegalArgumentException} and returns a bad request response with the exception message.
     *
     * @param e the IllegalArgumentException
     * @return the response entity with the exception message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
