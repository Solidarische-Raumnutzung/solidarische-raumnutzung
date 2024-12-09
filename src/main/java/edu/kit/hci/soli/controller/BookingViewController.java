package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.ShareRoomType;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Controller("/bookings")
public class BookingViewController {
    private final BookingsService bookingsService;
    private final RoomService roomService;
    private final UserService userService;

    public BookingViewController(BookingsService bookingsService, RoomService roomService, UserService userService) {
        this.bookingsService = bookingsService;
        this.roomService = roomService;
        this.userService = userService;
    }

    @DeleteMapping("/{roomId}/bookings/{eventId}/delete")
    public String deleteBookings(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal,
                                 @PathVariable Long roomId, @PathVariable Long eventId) {
        log.info("Received delete request for booking {}", eventId);
        Booking booking = bookingsService.getBookingById(eventId);

        if (booking == null) {
            log.info("Booking {} not found", eventId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }

        if (!Objects.equals(booking.getRoom().getId(), roomId)) {
            log.info("Booking {} not found in room {}", eventId, roomId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }

        model.addAttribute("room", booking.getRoom());

        User admin = userService.resolveAdminUser();

        if (admin.equals(principal.getUser())) {
            bookingsService.delete(booking, BookingDeleteReason.ADMIN);
            log.info("Admin deleted booking {}", eventId);
            return "redirect:/bookings";
        }

        if (booking.getUser().equals(principal.getUser())) {
            bookingsService.delete(booking, BookingDeleteReason.SELF);
            log.info("User deleted booking {}", eventId);
            return "redirect:/bookings";
        }

        log.info("User {} tried to delete booking {} of user {}", principal.getUsername(), eventId, booking.getUser());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("error", KnownError.DELETE_NO_PERMISSION);
        return "error_known";
    }

    @GetMapping("/{roomId}/bookings")
    public String roomBookings(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal,
                               @PathVariable Long roomId) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        model.addAttribute("room", room.get());
        model.addAttribute("bookings", bookingsService.getBookingsByUser(principal.getUser(), room.get()));

        return "bookings";
    }

    @GetMapping("/{roomId}/bookings/{eventId}")
    public String viewEvent(Model model, HttpServletResponse response,
                            @AuthenticationPrincipal SoliUserDetails principal,
                            @PathVariable Long roomId,
                            @PathVariable Long eventId) {

        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }

        Booking booking = bookingsService.getBookingById(eventId);
        if (booking == null) {
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        model.addAttribute("room", room.get());
        model.addAttribute("booking", booking);
        model.addAttribute("showRequestButton",
                ShareRoomType.ON_REQUEST.equals(booking.getShareRoomType())
                        && booking.getUser().equals(principal.getUser())
                        && !bookingsService.minimumTime().isAfter(booking.getStartDate())
        );
        return "view_event";
    }
}
