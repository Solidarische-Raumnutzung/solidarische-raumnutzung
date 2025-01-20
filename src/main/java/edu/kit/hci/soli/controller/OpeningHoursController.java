package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@Controller("/admin/opening-hours")
public class OpeningHoursController {
    private final RoomService roomService;

    public OpeningHoursController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/admin/opening-hours/{roomId}/save")
    public String saveOpeningHours(@PathVariable Long roomId, @RequestParam String startTime, @RequestParam String endTime, @RequestParam DayOfWeek dayOfWeek, Model model, HttpServletResponse response) {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            roomService.saveOpeningHours(roomId, start, end, dayOfWeek);
            return "redirect:/admin/opening-hours/{roomId}";
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Invalid time format");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "error/known";
        }
    }
}
