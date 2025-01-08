package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.SoliConfiguration;
import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LayoutParams;
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

/**
 * Controller for handling booking-related requests.
 */
@Slf4j
@Controller("/bookings")
public class BookingViewController {
    private final BookingsService bookingsService;
    private final RoomService roomService;
    private final UserService userService;
    private final int maxPaginationSize;

    /**
     * Constructs a BookingViewController with the specified services.
     *
     * @param bookingsService   the service for managing bookings
     * @param roomService       the service for managing rooms
     * @param userService       the service for managing users
     * @param soliConfiguration the configuration of the application
     */
    public BookingViewController(BookingsService bookingsService, RoomService roomService, UserService userService, SoliConfiguration soliConfiguration) {
        this.bookingsService = bookingsService;
        this.roomService = roomService;
        this.userService = userService;
        this.maxPaginationSize = soliConfiguration.getPagination().getMaxSize();
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
    @DeleteMapping("/{roomId:\\d+}/bookings/{eventId:\\d+}")
    public String deleteBookings(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal,
                                 @PathVariable Long roomId, @PathVariable Long eventId,
                                 @ModelAttribute("layout") LayoutParams layout) {
        log.info("Received delete request for booking {}", eventId);
        Booking booking = bookingsService.getBookingById(eventId);

        if (booking == null) {
            log.info("Booking {} not found", eventId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }

        if (!Objects.equals(booking.getRoom().getId(), roomId)) {
            log.info("Booking {} not found in room {}", eventId, roomId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }

        layout.setRoom(booking.getRoom());

        User admin = userService.resolveAdminUser();

        if (Objects.equals(booking.getUser(), principal.getUser())) {
            bookingsService.delete(booking, BookingDeleteReason.SELF);
            log.info("User deleted booking {}", eventId);
            return "redirect:/" + booking.getRoom().getId() + "/bookings";
        }

        if (admin.equals(principal.getUser())) {
            bookingsService.delete(booking, BookingDeleteReason.ADMIN);
            log.info("Admin deleted booking {}", eventId);
            return "redirect:/" + booking.getRoom().getId() + "/bookings";
        }

        log.info("User {} tried to delete booking {} of user {}", principal.getUsername(), eventId, booking.getUser());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("error", KnownError.DELETE_NO_PERMISSION);
        return "error/known";
    }

    /**
     * Displays the bookings for a specific room.
     * @param page      the page number
     * @param size      the number of items per page
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @param roomId    the ID of the room
     * @return the view name
     */
    @GetMapping("/{roomId:\\d+}/bookings")
    public String roomBookings(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model,
                               HttpServletResponse response,
                               @AuthenticationPrincipal SoliUserDetails principal,
                               @PathVariable Long roomId,
                               @ModelAttribute("layout") LayoutParams layout) {

        if (size > maxPaginationSize) {
            size = maxPaginationSize;
        }
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        layout.setRoom(room.get());
        model.addAttribute("bookings", bookingsService.getBookingsByUser(principal.getUser(), room.get(), page, size));

        return "bookings/list";
    }

    /**
     * Displays the details of a specific event.
     *
     * @param model     the model to be used in the view
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @param roomId    the ID of the room
     * @param eventId   the ID of the event
     * @param layout    state of the site layout
     * @return the view name
     */
    @GetMapping("/{roomId:\\d+}/bookings/{eventId:\\d+}")
    public String viewEvent(Model model, HttpServletResponse response,
                            @AuthenticationPrincipal SoliUserDetails principal,
                            @PathVariable Long roomId,
                            @PathVariable Long eventId,
                            @ModelAttribute("layout") LayoutParams layout) {

        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }

        Booking booking = bookingsService.getBookingById(eventId);
        if (booking == null) {
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }

        layout.setRoom(room.get());

        model.addAttribute("booking", booking);
        model.addAttribute("showRequestButton",
                ShareRoomType.ON_REQUEST.equals(booking.getShareRoomType())
                        && !Objects.equals(booking.getUser(), principal.getUser())
                        && booking.getStartDate().isBefore(bookingsService.minimumTime())
        );

        User admin = userService.resolveAdminUser();

        model.addAttribute("showDeleteButton",
                admin.equals(principal.getUser())
                        || Objects.equals(booking.getUser(), principal.getUser())
        );

        return "bookings/single_page";
    }
}
