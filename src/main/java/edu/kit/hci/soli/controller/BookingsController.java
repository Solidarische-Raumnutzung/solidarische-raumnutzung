package edu.kit.hci.soli.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller("/bookings")
public class BookingsController {

    @GetMapping("/bookings")
    public String bookings(Model model, HttpServletResponse response, Principal principal) {

        return "bookings";
    }
}
