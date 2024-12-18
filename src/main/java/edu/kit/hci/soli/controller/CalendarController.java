package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Controller for handling calendar-related requests.
 */
@Slf4j
@Controller("/")
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
     * @param model     the model to be used in the view
     * @param request   the HTTP request
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @return the view name
     */
    @GetMapping("/")
    public String calendar(Model model, HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal) {
        return calendar(model, request, response, principal, 1L);
    }

    /**
     * Displays the calendar for a specific room.
     *
     * @param model     the model to be used in the view
     * @param request   the HTTP request
     * @param response  the HTTP response
     * @param principal the authenticated user details
     * @param roomId    the ID of the room
     * @return the view name
     */
    @GetMapping("/{roomId:\\d+}")
    public String calendar(Model model, HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal SoliUserDetails principal, @PathVariable long roomId) {
        if (principal != null) {
            principal.getUser().setLocale(request.getLocale());
            log.info("Received request from {}", principal.getUsername());
        }
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error_known";
        }
        model.addAttribute("room", room.get());
        return "calendar";
    }
}
