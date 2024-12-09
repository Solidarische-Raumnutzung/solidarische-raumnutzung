package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.CalendarEvent;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController("/api/events")
public class EventFeedController {
    private final BookingsService bookingsRepository;
    private final RoomService roomService;

    public EventFeedController(BookingsService bookingsRepository, RoomService roomService) {
        this.bookingsRepository = bookingsRepository;
        this.roomService = roomService;
    }

    // https://fullcalendar.io/docs/events-json-feed
    @GetMapping("/api/{roomId}/events")
    public List<CalendarEvent> getEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @PathVariable Long roomId,
            @AuthenticationPrincipal SoliUserDetails principal
    ) {
        //TODO we should highlight events by ourselves WITHOUT exposing the ownership by allowing others to send requests as spoofed users
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (end.minusMonths(3).isAfter(start)) {
            throw new IllegalArgumentException("Time range must be less than 3 months");
        }

        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }

        return bookingsRepository.getCalendarEvents(room.get(), start, end, principal == null ? null : principal.getUser());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
