package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller for handling calendar-related requests.
 */
public class CalendarController {
    private final RoomService roomService;

    /**
     * Constructs a CalendarController with the specified {@link RoomService}.
     *
     * @param roomService the service for managing rooms
     */
    public CalendarController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Displays the calendar for the default room.
     *
     * @param model the model to be used in the view
     * @param response the HTTP response
     * @param principal the authenticated user details
     * @return the view name
     */
    @GetMapping("/")
    public String calendar(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal) {
        return calendar(model, response, principal, 1L);
    }

    /**
     * Displays the calendar for a specific room.
     *
     * @param model the model to be used in the view
     * @param response the HTTP response
     * @param principal the authenticated user details
     * @param roomId the ID of the room
     * @return the view name
     */
    @GetMapping("/{roomId}")
    public String calendar(Model model, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal, @PathVariable long roomId) {
        if (principal != null) log.info("Received request from {}", principal.getUsername());
        Room room = roomService.get(roomId);
        if (room == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        model.addAttribute("room", room);
        return "calendar";
    }
}
