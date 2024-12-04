package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@Controller("/bookings")
public class BookingsController {

    private final BookingsService bookingsService;
    private final RoomService roomService;
    private final UserService userService;

    public BookingsController(BookingsService bookingsService, RoomService roomService, UserService userService) {
        this.bookingsService = bookingsService;
        this.roomService = roomService;
        this.userService = userService;
    }

    @GetMapping("/bookings")
    public String userBookings(Model model, HttpServletResponse response, Principal principal) {
        User user = userService.resolveLoggedInUser(principal);
        model.addAttribute("bookings", bookingsService.getBookingsByUser(user));

        return "bookings";
    }


    @DeleteMapping("/bookings/delete/{id}")
    public String deleteBookings(@PathVariable("id") Long id, Model model, HttpServletResponse response, Principal principal) {
        log.info("Received delete request for booking {}", id);
        User user = userService.resolveLoggedInUser(principal);
        Booking booking = bookingsService.getBookingById(id);


        if (booking == null) {
            log.info("Booking {} not found", id);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }

        User admin = userService.resolveAdminUser();

        if (admin.equals(user)) {
            bookingsService.delete(booking);
            log.info("Admin deleted booking {}", id);
            return "redirect:/bookings";
        }

        if (booking.getUser().equals(user)) {
            bookingsService.delete(booking);
            log.info("User deleted booking {}", id);
            return "redirect:/bookings";
        }

        log.info("User {} tried to delete booking {} of user {}", user, id, booking.getUser());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("error", KnownError.DELETE_NO_PERMISSION);
        return "error_known";
    }

    @GetMapping("/{id}/bookings")
    public String roomBookings(Model model, HttpServletResponse response, Principal principal, @PathVariable Long id) {
        User user = userService.resolveLoggedInUser(principal); //TODO user seems to be null here
        model.addAttribute("bookings", bookingsService.getBookingsByUser(user));

        return "bookings";
    }

    @GetMapping("/{id}/bookings/new")
    public String newBooking(
            Model model, HttpServletResponse response, Principal principal, @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        if (!roomService.existsById(id)) {
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
        model.addAttribute("room", id);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        model.addAttribute("minimumTime", minimumTime());
        model.addAttribute("maximumTime", maximumTime());

        return "create_booking";
    }

    @GetMapping("/{roomId}/bookings/view/{eventId}")
    public String viewEvent(Model model, HttpServletResponse response,
                            @PathVariable("roomId") Long roomId,
                            @PathVariable("eventId") Long eventId,
                            @ModelAttribute("login") LoginStateModel login) {

        // Validate room exists
        if (!roomService.existsById(roomId)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }

        Booking booking = bookingsService.getBookingById(eventId);
        if (booking == null) {
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        model.addAttribute("booking", booking);
        model.addAttribute("login", login);
        return "view_event";
    }

    public LocalDateTime currentSlot() {
        LocalDateTime now = LocalDateTime.now();
        return now.minusMinutes(now.getMinute() % 15);
    }

    private LocalDateTime minimumTime() {
        return currentSlot().plusMinutes(15);
    }

    private LocalDateTime maximumTime() {
        return currentSlot().plusDays(14);
    }

    @PostMapping(value = "/{id}/bookings/new", consumes = "application/x-www-form-urlencoded")
    public String createBooking(
            Model model, HttpServletResponse response, @PathVariable Long id,
            @ModelAttribute("login") LoginStateModel loginStateModel,
            @ModelAttribute FormData formData
    ) {
        // Validate exists
        if (!roomService.existsById(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        Room room = roomService.get();
        if (loginStateModel.user() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            model.addAttribute("error", KnownError.NO_USER);
            return "error_known"; //TODO we should modify the LSM so this never happens
        }
        if (formData.start == null || formData.end == null || formData.priority == null || formData.cooperative == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("error", KnownError.MISSING_PARAMETER);
            return "error_known";
        }
        formData.description = formData.description == null ? "" : formData.description.trim();

        // Validate start and end times
        if (formData.start.isAfter(formData.end)
                || formData.start.isBefore(minimumTime())
                || formData.end.isAfter(formData.start.plusHours(4)) // Keep these in sync with index.jte!
                || formData.end.isAfter(maximumTime())
                || formData.start.getMinute() % 15 != 0
                || formData.end.getMinute() % 15 != 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("error", KnownError.INVALID_TIME);
            return "error_known";
        }

        //TODO check for overlapping bookings
        bookingsService.create(new Booking(
                null,
                formData.description,
                formData.start,
                formData.end,
                formData.cooperative,
                room,
                loginStateModel.user(),
                formData.priority
        ));

        return "redirect:/" + id + "/bookings"; //TODO redirect to the new booking
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
