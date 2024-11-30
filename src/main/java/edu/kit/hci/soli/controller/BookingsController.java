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

    @GetMapping("/{id}/bookings")
    public String roomBookings(Model model, HttpServletResponse response, Principal principal, @PathVariable Long id) {
        User user = userService.resolveLoggedInUser(principal);
        log.info("User: {}", user.getEmail());
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

        return "create_booking";
    }

    @PostMapping(value = "/{id}/bookings/new", consumes = "application/x-www-form-urlencoded")
    public String createBooking(
            Model model, HttpServletResponse response, Principal principal, @PathVariable Long id,
            @ModelAttribute("login") LoginStateModel loginStateModel,
            @ModelAttribute FormData formData
    ) {
        if (!roomService.existsById(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        if (loginStateModel.user() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            model.addAttribute("error", KnownError.NO_USER);
            return "error_known"; //TODO we should modify the LSM so this never happens
        }
        //TODO validate the form data
        Room room = roomService.get();
        bookingsService.create(new Booking(
                null,
                "New booking",
                formData.start,
                formData.end,
                formData.cooperative ? ShareRoomType.YES : ShareRoomType.NO,
                room,
                loginStateModel.user(),
                Priority.valueOf(formData.priority)
        ));

        return "redirect:/" + id + "/bookings"; //TODO redirect to the new booking
    }

    @Data
    public static class FormData {
        public LocalDateTime start;
        public LocalDateTime end;
        public String description;
        public boolean emailVisible;
        public String priority;
        public boolean cooperative;
    }
}
