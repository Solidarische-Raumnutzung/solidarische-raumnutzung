package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.*;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;
import java.util.Optional;

/**
 * Controller for handling booking-related requests.
 */
@Slf4j
@Controller("/bookings")
public class BookingViewController {
    private final BookingsService bookingsService;
    private final RoomService roomService;
    private final UserService userService;

    /**
     * Constructs a BookingViewController with the specified services.
     *
     * @param bookingsService the service for managing bookings
     * @param roomService     the service for managing rooms
     * @param userService     the service for managing users
     */
    public BookingViewController(BookingsService bookingsService, RoomService roomService, UserService userService) {
        this.bookingsService = bookingsService;
        this.roomService = roomService;
        this.userService = userService;
    }

    /**
     * Deletes a booking for a specific room and event.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @param roomId    the ID of the room
     * @param eventId   the ID of the event
     * @return the view name
     */
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
            return "redirect:/" + booking.getRoom().getId() + "/bookings";
        }

        if (booking.getUser().equals(principal.getUser())) {
            bookingsService.delete(booking, BookingDeleteReason.SELF);
            log.info("User deleted booking {}", eventId);
            return "redirect:/" + booking.getRoom().getId() + "/bookings";
        }

        log.info("User {} tried to delete booking {} of user {}", principal.getUsername(), eventId, booking.getUser());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("error", KnownError.DELETE_NO_PERMISSION);
        return "error_known";
    }

    /**
     * Displays the bookings for a specific room.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @param roomId    the ID of the room
     * @return the view name
     */
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

    /**
     * Displays the details of a specific event.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @param roomId    the ID of the room
     * @param eventId   the ID of the event
     * @return the view name
     */
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
