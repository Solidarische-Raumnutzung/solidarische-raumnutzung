package edu.kit.hci.soli.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Main controller for miscellaneous status requests.
 */
@Controller("/misc")
@Slf4j
public class MainController {
    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * Retrieves the active profile.
     *
     * @return the active profile
     */
    @ResponseBody
    @GetMapping("/profile")
    public String getProfile() {
        return profile;
    }
}
