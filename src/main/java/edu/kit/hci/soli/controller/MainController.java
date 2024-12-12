package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.security.SoliUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public void timezone(@AuthenticationPrincipal SoliUserDetails principal, @RequestHeader("X-Timezone") String timezoneField) {
        if (principal == null) return;

        ZoneId timezone = timezoneField == null || timezoneField.isBlank() ? ZoneId.systemDefault() : ZoneId.of(timezoneField);

        log.info("Timezone: {}", timezone);
        log.info("ZoneId: {}", timezone.getId());

        principal.getUser().setTimezone(timezone);
    }
}
