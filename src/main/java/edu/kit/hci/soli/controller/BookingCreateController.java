package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LayoutParams;
import edu.kit.hci.soli.dto.form.CreateEventForm;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.service.TimeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Controller for handling booking creation requests.
 */
@Slf4j
@Controller("/bookings/new")
public class BookingCreateController {
    private final TimeService timeService;
    private final BookingsService bookingsService;
    private final RoomService roomService;

    /**
     * Constructs a BookingCreateController with the specified services.
     *
     * @param bookingsService the service for managing bookings
     * @param roomService     the service for managing rooms
     */
    public BookingCreateController(TimeService timeService, BookingsService bookingsService, RoomService roomService) {
        this.timeService = timeService;
        this.bookingsService = bookingsService;
        this.roomService = roomService;
    }

    /**
     * Displays the form for creating a new booking.
     *
     * @param model       the model to be used in the view
     * @param response    the HTTP response
     * @param roomId      the ID of the room
     * @param start       the start date and time of the booking
     * @param end         the end date and time of the booking
     * @param cooperative whether the booking is cooperative
     * @return the view name
     */
    @GetMapping("/{roomId:\\d+}/bookings/new")
    public String newBooking(
            Model model, HttpServletResponse response, @PathVariable Long roomId,
            @ModelAttribute("layout") LayoutParams layout,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Boolean cooperative
    ) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        if (start == null) {
            start = timeService.minimumTime();
        }
        if (end == null) {
            end = start.plusMinutes(30);
        }
        if (cooperative == null) {
            cooperative = false;
        }
        layout.setRoom(room.get());
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("cooperative", cooperative ? ShareRoomType.YES : ShareRoomType.NO);

        model.addAttribute("minimumTime", timeService.minimumTime());
        model.addAttribute("maximumTime", timeService.maximumTime());

        return "bookings/create/form";
    }

    /**
     * Creates a new booking.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param request   the HTTP request
     * @param roomId    the ID of the room
     * @param principal the authenticated user details
     * @param formData  the form data for the booking
     * @return the view name
     */
    @PostMapping(value = "/{roomId:\\d+}/bookings/new", consumes = "application/x-www-form-urlencoded")
    public String createBooking(
            Model model, HttpServletResponse response, HttpServletRequest request,
            @PathVariable Long roomId,
            @ModelAttribute("layout") LayoutParams layout,
            @AuthenticationPrincipal SoliUserDetails principal,
            @ModelAttribute CreateEventForm formData
    ) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        if (formData.getStart() == null || formData.getEnd() == null || formData.getPriority() == null || formData.getCooperative() == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("error", KnownError.MISSING_PARAMETER);
            return "error/known";
        }
        layout.setRoom(room.get());
        formData.setDescription(formData.getDescription() == null ? "" : formData.getDescription().trim());

        // Validate start and end times
        if (formData.getStart().isAfter(formData.getEnd())
                || formData.getStart().isBefore(timeService.minimumTime())
                || formData.getEnd().isAfter(formData.getStart().plusHours(4)) // Keep these in sync with index.jte!
                || formData.getEnd().isAfter(timeService.maximumTime())
                || formData.getStart().getMinute() % 15 != 0
                || formData.getEnd().getMinute() % 15 != 0
                || formData.getStart().getDayOfWeek() != formData.getEnd().getDayOfWeek()
                || formData.getStart().getDayOfWeek() == DayOfWeek.SATURDAY
                || formData.getStart().getDayOfWeek() == DayOfWeek.SUNDAY) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("error", KnownError.INVALID_TIME);
            return "error/known";
        }

        Booking attemptedBooking = new Booking(
                null,
                formData.getDescription(),
                formData.getStart(),
                formData.getEnd(),
                formData.getCooperative(),
                room.get(),
                principal.getUser(),
                formData.getPriority(),
                Set.of()
        );
        return handleBookingAttempt(attemptedBooking, bookingsService.attemptToBook(attemptedBooking), request, model);
    }

    /**
     * Resolves a booking conflict.
     *
     * @param model   the model to be used in the view
     * @param request the HTTP request
     * @param roomId  the ID of the room
     * @param layout  state of the site layout
     * @return the view name
     */
    @PostMapping(value = "/{roomId:\\d+}/bookings/new/conflict", consumes = "application/x-www-form-urlencoded")
    public String resolveConflict(
            Model model, HttpServletRequest request, @PathVariable Long roomId,
            @ModelAttribute("layout") LayoutParams layout
    ) {
        Booking attemptedBooking = (Booking) request.getSession().getAttribute("attemptedBooking");
        if (attemptedBooking == null) {
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        if (!Objects.equals(attemptedBooking.getRoom().getId(), roomId)) {
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        layout.setRoom(room.get());
        BookingAttemptResult.PossibleCooperation bookingResult = (BookingAttemptResult.PossibleCooperation) request.getSession().getAttribute("bookingResult");
        return handleBookingAttempt(attemptedBooking, bookingsService.affirm(attemptedBooking, bookingResult), request, model);
    }

    /**
     * Handles the result of a booking attempt.
     *
     * @param attemptedBooking the attempted booking
     * @param bookingResult    the result of the booking attempt
     * @param request          the HTTP request
     * @param model            the model to be used in the view
     * @return the view name
     */
    private String handleBookingAttempt(Booking attemptedBooking, BookingAttemptResult bookingResult, HttpServletRequest request, Model model) {
        return switch (bookingResult) {
            case BookingAttemptResult.Failure(var conflict) -> {
                model.addAttribute("error", KnownError.EVENT_CONFLICT);
                model.addAttribute("conflicts", conflict);
                yield "error/known";
            }
            case BookingAttemptResult.Success(var booking) -> "redirect:/" + attemptedBooking.getRoom().getId();
            case BookingAttemptResult.PossibleCooperation result -> {
                request.getSession().setAttribute("attemptedBooking", attemptedBooking);
                request.getSession().setAttribute("bookingResult", result);
                model.addAttribute("attemptedBooking", attemptedBooking);
                model.addAttribute("bookingResult", result);
                yield "bookings/create/conflict";
            }
            case BookingAttemptResult.Staged(var staged) -> {
                model.addAttribute("stagedBooking", staged);
                yield "bookings/create/success_staged";
            }
        };
    }

}
