package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller("/")
@Slf4j
public class MainController {

    @Value("${spring.profiles.active}")
    private String profile;

    private final RoomService roomService;

    public MainController(RoomService roomService) {
        this.roomService = roomService;
    }

    @ResponseBody
    @GetMapping("/profile")
    public String getProfile() {
        return profile;
    }

    @GetMapping("/")
    public String calendar(Model model, HttpServletResponse response, @ModelAttribute("login") LoginStateModel login) {
        return calendar(model, response, login, 1L);
    }

    @GetMapping("/{roomId}")
    public String calendar(Model model, HttpServletResponse response, @ModelAttribute("login") LoginStateModel login, @PathVariable long roomId) {
        if (!login.name().isEmpty()) log.info("Received request from {}", login.name());
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
