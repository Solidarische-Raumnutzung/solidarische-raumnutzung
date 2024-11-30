package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.dto.LoginStateModel;
import edu.kit.hci.soli.service.RoomService;
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
    public String index(Model model, @ModelAttribute("login") LoginStateModel login) {
        if (!login.name().isEmpty()) log.info("Received request from {}", login.name());
        model.addAttribute("room", roomService.get());
        return "index";
    }
}
