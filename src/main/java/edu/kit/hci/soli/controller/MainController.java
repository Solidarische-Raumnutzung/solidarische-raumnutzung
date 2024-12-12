package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.ZoneId;

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

    @PostMapping("/timezone")
    public ResponseEntity<String> timezone(@AuthenticationPrincipal SoliUserDetails principal,
                                           @RequestHeader("X-Timezone") String timezoneField) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ZoneId timezone;
        try {
            timezone = timezoneField == null || timezoneField.isBlank()
                    ? ZoneId.systemDefault()
                    : ZoneId.of(timezoneField);
        } catch (DateTimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid timezone");
        }

        log.info("Timezone: {}", timezone);

        principal.getUser().setTimezone(timezone);
        return ResponseEntity.ok().body("Successfully set timezone to " + timezone);
    }
}
