package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Controller
@RequestMapping("/admin/opening-hours")
public class OpeningHoursController {
    private final RoomService roomService;

    public OpeningHoursController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/{roomId}/save")
    public String saveOpeningHours(@PathVariable Long roomId, @RequestParam String startTime,
                                   @RequestParam String endTime, @RequestParam DayOfWeek dayOfWeek, Model model,
                                   HttpServletResponse response) {
        LocalTime start, end;
        try {
            start = LocalTime.parse(startTime);
            end = LocalTime.parse(endTime);
        } catch (DateTimeParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("error", KnownError.INVALID_TIME);
            return "error/known";
        }
        roomService.saveOpeningHours(roomId, start, end, dayOfWeek);
        return "redirect:/admin/opening-hours/{roomId}";
    }

    @GetMapping("/admin/opening-hours/{roomId:\\d+}")
    public String showOpeningHoursForm(@PathVariable Long roomId, Model model, HttpServletResponse response) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
//        model.addAttribute("room", room.get());
        return "admin/opening-hours/{roomId}";
    }
}
