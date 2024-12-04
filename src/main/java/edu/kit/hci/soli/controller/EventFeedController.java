package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.dto.CalendarEvent;
import edu.kit.hci.soli.service.BookingsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController("/api/events")
public class EventFeedController {
    final BookingsService bookingsRepository;

    public EventFeedController(BookingsService bookingsRepository) {
        this.bookingsRepository = bookingsRepository;
    }

    // https://fullcalendar.io/docs/events-json-feed
    @GetMapping("/api/events")
    public List<CalendarEvent> getEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        //TODO we should highlight events by ourselves WITHOUT exposing the ownership by allowing others to send requests as spoofed users
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (end.minusMonths(3).isAfter(start)) {
            throw new IllegalArgumentException("Time range must be less than 3 months");
        }

        return bookingsRepository.getCalendarEvents(start, end);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
