package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LayoutParams;
import edu.kit.hci.soli.service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @GetMapping("/admin/rooms")
    public String adminRoomList(Model model, @ModelAttribute("layout") LayoutParams layout) {
        List<Room> rooms = roomService.getAll();
        model.addAttribute("rooms", rooms);
        model.addAttribute("edit", true);
        return "rooms";
    }

    @GetMapping("/admin/rooms/new")
    public String newRoom(Model model, @ModelAttribute("layout") LayoutParams layout) {
        model.addAttribute("id", null);
        model.addAttribute("name", "");
        model.addAttribute("description", "");
        return "admin/edit_room_page";
    }

    @GetMapping("/admin/rooms/{roomId:\\d+}")
    public String editRoom(Model model, HttpServletResponse response,
                           @PathVariable long roomId) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        model.addAttribute("id", roomId);
        model.addAttribute("name", room.get().getName());
        model.addAttribute("description", room.get().getDescription());
        return "admin/edit_room_page";
    }

    @GetMapping("/admin/rooms/{roomId:\\d+}/delete")
    public String showDeletionDialog(Model model, HttpServletResponse response,
                           @PathVariable long roomId) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        model.addAttribute("room", room.get());
        return "admin/confirmation/delete_room_page";
    }

    @DeleteMapping("/admin/rooms/{roomId:\\d+}")
    public String deleteRoom(Model model, HttpServletResponse response,
                             @PathVariable long roomId) {
        Optional<Room> room = roomService.getOptional(roomId);
        if (room.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }
        roomService.delete(room.get());
        return "redirect:/admin/rooms";
    }

    @PostMapping(value = "/admin/rooms", consumes = "application/x-www-form-urlencoded")
    public String editOrCreateRoom(Model model, HttpServletResponse response, @ModelAttribute FormData formData) {
        Objects.requireNonNull(formData.name);
        Objects.requireNonNull(formData.description);
        if (formData.target != null) {
            // Some target was picked, edit it
            Optional<Room> room = roomService.getOptional(formData.target);
            if (room.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                model.addAttribute("error", KnownError.NOT_FOUND);
                return "error/known";
            }
            room.get().setName(formData.name);
            room.get().setDescription(formData.description);
            roomService.save(room.get());
            return "redirect:/admin/rooms";
        } else {
            // No target was picked, create a new room
            roomService.save(new Room(null, formData.name, formData.description));
            return "redirect:/admin/rooms";
        }
    }

    /**
     * Data class for room creation form data.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormData {
        public Long target;
        public String name;
        public String description;
    }
}
