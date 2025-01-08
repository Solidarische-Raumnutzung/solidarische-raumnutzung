package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LayoutParams;
import edu.kit.hci.soli.service.BookingsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * Controller for handling collaboration requests.
 */
@Slf4j
@Controller("/bookings/collaboration")
public class CollaborationRequestController {
    private final BookingsService bookingsService;

    /**
     * Constructor for CollaborationRequestController.
     *
     * @param bookingsService the service to manage bookings
     */
    public CollaborationRequestController(BookingsService bookingsService) {
        this.bookingsService = bookingsService;
    }

    /**
     * Handles HTTP GET requests to view a collaboration request for a specific booking.
     *
     * @param model     the model to add attributes to
     * @param response  the HTTP response to set status codes
     * @param principal the authenticated user details
     * @param roomId    the ID of the room
     * @param eventId   the ID of the event (booking)
     * @return the view name to be rendered
     */
    @GetMapping("/{roomId:\\d+}/bookings/{eventId:\\d+}/collaboration")
    public String viewCollaborationRequest(
            Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal,
            @ModelAttribute("layout") LayoutParams layout,
            @PathVariable Long roomId, @PathVariable Long eventId
    ) {
        log.info("Received request for managing collaboration on booking {}", eventId);
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

        if (!booking.getOpenRequests().contains(principal.getUser())) {
            log.info("Booking {} in room {} has no outstanding collaboration request for {}", eventId, roomId, principal.getUser());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }

        model.addAttribute("booking", booking);

        return "bookings/collaboration/modal";
    }

    /**
     * Handles HTTP PUT requests to accept a collaboration request for a specific booking.
     *
     * @param model     the model to add attributes to
     * @param response  the HTTP response to set status codes
     * @param principal the authenticated user details
     * @param roomId    the ID of the room
     * @param eventId   the ID of the event (booking)
     * @return the view name to be rendered
     */
    @PutMapping("/{roomId:\\d+}/bookings/{eventId:\\d+}/collaboration")
    public String acceptCollaborationRequest(
            Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal,
            @ModelAttribute("layout") LayoutParams layout,
            @PathVariable Long roomId, @PathVariable Long eventId
    ) {
        log.info("Received request to accept collaboration on booking {}", eventId);
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

        if (!booking.getOpenRequests().contains(principal.getUser())) {
            log.info("Booking {} in room {} has no outstanding collaboration request for {}", eventId, roomId, principal.getUser());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }

        bookingsService.confirmRequest(booking, principal.getUser());

        return "redirect:/" + roomId + "/bookings/" + eventId;
    }

    /**
     * Handles HTTP DELETE requests to reject a collaboration request for a specific booking.
     *
     * @param model     the model to add attributes to
     * @param response  the HTTP response to set status codes
     * @param principal the authenticated user details
     * @param roomId    the ID of the room
     * @param eventId   the ID of the event (booking)
     * @return the view name to be rendered
     */
    @DeleteMapping("/{roomId:\\d+}/bookings/{eventId:\\d+}/collaboration")
    public String rejectCollaborationRequest(
            Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal,
            @ModelAttribute("layout") LayoutParams layout,
            @PathVariable Long roomId, @PathVariable Long eventId
    ) {
        log.info("Received request to reject collaboration on booking {}", eventId);
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

        if (!booking.getOpenRequests().contains(principal.getUser())) {
            log.info("Booking {} in room {} has no outstanding collaboration request for {}", eventId, roomId, principal.getUser());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }

        bookingsService.denyRequest(booking, principal.getUser());

        return "redirect:/" + roomId + "/bookings";
    }
}
