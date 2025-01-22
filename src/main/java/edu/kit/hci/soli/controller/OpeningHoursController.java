package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.form.SaveOpeningHoursForm;
import edu.kit.hci.soli.service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Controller("/admin/opening-hours")
public class OpeningHoursController {
    private final RoomService roomService;

    public OpeningHoursController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PutMapping("/admin/opening-hours/{roomId}/save")
    public String saveOpeningHours(@PathVariable Long roomId, Model model,
                                   HttpServletResponse response, @ModelAttribute SaveOpeningHoursForm form) {
        if (form.getMondayStart().isAfter(form.getMondayEnd())
                || form.getTuesdayStart().isAfter(form.getTuesdayEnd())
                || form.getWednesdayStart().isAfter(form.getWednesdayEnd())
                || form.getThursdayStart().isAfter(form.getThursdayEnd())
                || form.getFridayStart().isAfter(form.getFridayEnd())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("error", KnownError.INVALID_TIME);
            return "error/known";
        }
        roomService.saveOpeningHours(roomId, form.getMondayStart(), form.getMondayEnd(), DayOfWeek.MONDAY);
        roomService.saveOpeningHours(roomId, form.getTuesdayStart(), form.getTuesdayEnd(), DayOfWeek.TUESDAY);
        roomService.saveOpeningHours(roomId, form.getWednesdayStart(), form.getWednesdayEnd(), DayOfWeek.WEDNESDAY);
        roomService.saveOpeningHours(roomId, form.getThursdayStart(), form.getThursdayEnd(), DayOfWeek.THURSDAY);
        roomService.saveOpeningHours(roomId, form.getFridayStart(), form.getFridayEnd(), DayOfWeek.FRIDAY);
        return "redirect:/admin/opening-hours/" + roomId;
    }

    @GetMapping("/admin/opening-hours/{roomId:\\d+}")
    public String showOpeningHoursForm(@PathVariable Long roomId, Model model, HttpServletResponse response) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        model.addAttribute("roomId", room.get().getId());
        return "admin/opening-hours" /*+ roomId.toString()*/;
    }
}
