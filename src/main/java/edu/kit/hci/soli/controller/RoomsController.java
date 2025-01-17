package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.LayoutParams;
import edu.kit.hci.soli.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Controller class for viewing and managing rooms.
 */
@Controller("/")
@Slf4j
public class RoomsController {
    private final RoomService roomService;

    public RoomsController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String roomList(Model model,
                           @ModelAttribute("layout") LayoutParams layout) {
        List<Room> rooms = roomService.getAll();
        if (rooms.size() == 1) {
            return "redirect:/" + rooms.getFirst().getId();
        }
        layout.setRoom(null);
        model.addAttribute("rooms", rooms);
        return "rooms";
    }
}
