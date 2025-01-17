package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.RoomOpeningHours;
import edu.kit.hci.soli.dto.CalendarEvent;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for generating the FullCalendar event feed.
 */
@RestController
@RequestMapping("/api")
public class EventFeedController {
    private final BookingsService bookingsRepository;
    private final RoomService roomService;

    /**
     * Constructs an EventFeedController with the specified {@link BookingsService}.
     *
     * @param bookingsRepository the service for managing bookings
     * @param roomService        the service for managing rooms
     */
    public EventFeedController(BookingsService bookingsRepository, RoomService roomService) {
        this.bookingsRepository = bookingsRepository;
        this.roomService = roomService;
    }

    /**
     * Retrieves calendar events within the specified time range.
     *
     * @param start     the start date and time
     * @param end       the end date and time
     * @param roomId    the id of the room to show
     * @param principal the authenticated user details
     * @return the list of calendar events
     */
    @GetMapping("/api/{roomId}/events")
    public List<CalendarEvent> getEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @PathVariable Long roomId,
            @AuthenticationPrincipal SoliUserDetails principal
    ) {
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

    @GetMapping("/api/{roomId}/opening-hours")
    public List<Map<String, Object>> getOpeningHours(@PathVariable Long roomId) {
        List<RoomOpeningHours> openingHours = roomService.getOpeningHours(roomId);
        return openingHours.stream()
                .map(hour -> Map.of(
                        "daysOfWeek", List.of(getDayOfWeek(hour.getDayOfWeek())),
                        "startTime", hour.getStartTime().toString(),
                        "endTime", hour.getEndTime().toString()
                ))
                .collect(Collectors.toList());
    }

    private int getDayOfWeek(String day) {
        return switch (day) {
            case "Monday" -> 1;
            case "Tuesday" -> 2;
            case "Wednesday" -> 3;
            case "Thursday" -> 4;
            case "Friday" -> 5;
            case "Saturday" -> 6;
            case "Sunday" -> 0;
            default -> throw new IllegalArgumentException("Invalid day of week: " + day);
        };
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
