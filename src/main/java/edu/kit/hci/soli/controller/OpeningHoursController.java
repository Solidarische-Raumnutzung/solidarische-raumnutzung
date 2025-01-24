package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LayoutParams;
import edu.kit.hci.soli.dto.form.SaveOpeningHoursForm;
import edu.kit.hci.soli.service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.HashMap;
import java.util.Optional;

/**
 * Controller for managing the opening hours of rooms.
 */
@Controller("/admin/opening-hours")
public class OpeningHoursController {
    private final RoomService roomService;

    /**
     * Constructs a new OpeningHoursController with the specified RoomService.
     *
     * @param roomService the service for managing rooms
     */
    public OpeningHoursController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Saves the opening hours for a specific room.
     *
     * @param roomId   the ID of the room
     * @param model    the model to add attributes to
     * @param layout   the layout parameters
     * @param response the HTTP response
     * @param form     the form containing the opening hours
     * @return the view name
     */
    @PutMapping("/admin/opening-hours/{roomId}")
    public String saveOpeningHours(@PathVariable Long roomId, Model model,
                                   @ModelAttribute("layout") LayoutParams layout,
                                   HttpServletResponse response, @ModelAttribute SaveOpeningHoursForm form) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        layout.setRoom(room.get());
        if (form.getMondayStart().isAfter(form.getMondayEnd())
                || form.getTuesdayStart().isAfter(form.getTuesdayEnd())
                || form.getWednesdayStart().isAfter(form.getWednesdayEnd())
                || form.getThursdayStart().isAfter(form.getThursdayEnd())
                || form.getFridayStart().isAfter(form.getFridayEnd())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("error", KnownError.INVALID_TIME);
            return "error/known";
        }
        room.get().setOpeningHours(new HashMap<>(form.toMap()));
        roomService.save(room.get());
        return "redirect:/" + roomId;
    }

    /**
     * Displays the form for editing the opening hours of a specific room.
     *
     * @param roomId   the ID of the room
     * @param model    the model to add attributes to
     * @param response the HTTP response
     * @param layout   the layout parameters
     * @return the view name
     */
    @GetMapping("/admin/opening-hours/{roomId:\\d+}")
    public String showOpeningHoursForm(@PathVariable Long roomId, Model model, HttpServletResponse response,
                                       @ModelAttribute("layout") LayoutParams layout) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        layout.setRoom(room.get());
        return "admin/opening-hours";
    }
}