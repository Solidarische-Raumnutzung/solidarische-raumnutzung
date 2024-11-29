package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller("/bookings")
public class BookingsController {

    private final BookingsService bookingsService;
    private final RoomService roomService;

    public BookingsController(BookingsService bookingsService, RoomService roomService) {
        this.bookingsService = bookingsService;
        this.roomService = roomService;
    }

    @GetMapping("/bookings")
    public String userBookings(Model model, HttpServletResponse response, Principal principal) {
        return "bookings";
    }

    @GetMapping("/{id}/bookings")
    public String roomBookings(Model model, HttpServletResponse response, Principal principal, @PathVariable Long id) {
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
            return "404";
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
        if (roomService.existsById(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "404";
        }
        if (loginStateModel.user() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "401"; //TODO we should modify the LSM so this never happens
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
