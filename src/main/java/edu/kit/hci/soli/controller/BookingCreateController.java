package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller("/bookings/new")
public class BookingCreateController {
    private final BookingsService bookingsService;
    private final RoomService roomService;

    public BookingCreateController(BookingsService bookingsService, RoomService roomService) {
        this.bookingsService = bookingsService;
        this.roomService = roomService;
    }

    @GetMapping("/{roomId}/bookings/new")
    public String newBooking(
            Model model, HttpServletResponse response, @PathVariable Long roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Boolean cooperative
    ) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        if (start == null) {
            start = LocalDateTime.now();
        }
        if (end == null) {
            end = start.plusMinutes(30);
        }
        if (cooperative == null) {
            cooperative = false;
        }
        model.addAttribute("room", room.get());
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("cooperative", cooperative ? ShareRoomType.YES : ShareRoomType.NO);

        model.addAttribute("minimumTime", bookingsService.minimumTime());
        model.addAttribute("maximumTime", bookingsService.maximumTime());

        return "create_booking";
    }

    @PostMapping(value = "/{roomId}/bookings/new", consumes = "application/x-www-form-urlencoded")
    public String createBooking(
            Model model, HttpServletResponse response, HttpServletRequest request,
            @PathVariable Long roomId,
            @AuthenticationPrincipal SoliUserDetails principal,
            @ModelAttribute FormData formData
    ) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        if (principal == null || principal.getUser() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            model.addAttribute("error", KnownError.NO_USER);
            return "error_known"; //TODO we should modify the LSM so this never happens
        }
        if (formData.start == null || formData.end == null || formData.priority == null || formData.cooperative == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("error", KnownError.MISSING_PARAMETER);
            return "error_known";
        }
        model.addAttribute("room", room.get());
        formData.description = formData.description == null ? "" : formData.description.trim();

        // Validate start and end times
        if (formData.start.isAfter(formData.end)
                || formData.start.isBefore(bookingsService.minimumTime())
                || formData.end.isAfter(formData.start.plusHours(4)) // Keep these in sync with index.jte!
                || formData.end.isAfter(bookingsService.maximumTime())
                || formData.start.getMinute() % 15 != 0
                || formData.end.getMinute() % 15 != 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("error", KnownError.INVALID_TIME);
            return "error_known";
        }

        Booking attemptedBooking = new Booking(
                null,
                formData.description,
                formData.start,
                formData.end,
                formData.cooperative,
                room.get(),
                principal.getUser(),
                formData.priority,
                Set.of()
        );
        return handleBookingAttempt(attemptedBooking, bookingsService.attemptToBook(attemptedBooking), request, model);
    }

    @PostMapping(value = "/{roomId}/bookings/new/conflict", consumes = "application/x-www-form-urlencoded")
    public String resolveConflict(
            Model model, HttpServletRequest request, @PathVariable Long roomId
    ) {
        Booking attemptedBooking = (Booking) request.getSession().getAttribute("attemptedBooking");
        if (attemptedBooking == null) {
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        if (!Objects.equals(attemptedBooking.getRoom().getId(), roomId)) {
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        model.addAttribute("room", room.get());
        BookingAttemptResult.PossibleCooperation bookingResult = (BookingAttemptResult.PossibleCooperation) request.getSession().getAttribute("bookingResult");
        return handleBookingAttempt(attemptedBooking, bookingsService.affirm(attemptedBooking, bookingResult), request, model);
    }

    private String handleBookingAttempt(Booking attemptedBooking, BookingAttemptResult bookingResult, HttpServletRequest request, Model model) {
        return switch (bookingResult) {
            case BookingAttemptResult.Failure(var conflict) -> {
                model.addAttribute("error", KnownError.EVENT_CONFLICT);
                model.addAttribute("conflicts", conflict);
                yield "error_known";
            }
            case BookingAttemptResult.Success(var booking) -> "redirect:/" + attemptedBooking.getRoom().getId();
            case BookingAttemptResult.PossibleCooperation result -> {
                request.getSession().setAttribute("attemptedBooking", attemptedBooking);
                request.getSession().setAttribute("bookingResult", result);
                model.addAttribute("attemptedBooking", attemptedBooking);
                model.addAttribute("bookingResult", result);
                yield "create_booking_conflict";
            }
            case BookingAttemptResult.Staged(var staged) -> {
                model.addAttribute("stagedBooking", staged);
                yield "create_booking_staged";
            }
        };
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormData {
        public LocalDateTime start;
        public LocalDateTime end;
        public String description;
        public Priority priority;
        public ShareRoomType cooperative;
    }
}
