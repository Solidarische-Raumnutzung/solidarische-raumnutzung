package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.service.TimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class TimeServiceTest {
    @Autowired private TimeService timeService;

    @Test
    public void testRelative() {
        LocalDateTime currentSlotNormalized = timeService.currentSlot().plusDays(switch (timeService.currentSlot().getDayOfWeek()) {
            case SATURDAY -> 2;
            case SUNDAY -> 1;
            default -> 0;
        });
        assertFalse(currentSlotNormalized.isAfter(timeService.minimumTime()));
        assertTrue(currentSlotNormalized.isBefore(timeService.maximumTime()));
        assertTrue(currentSlotNormalized.plusMinutes(16).isAfter(timeService.minimumTime()));
    }

    @Test
    public void testDefaultTimeZoneSet() {
        // We set our current timezone in init, but depending on it probably isn't good
        // Make sure we set it correctly anyway
        // as there will be a slight delay between the two timestamps, allow for a bit of leeway
        LocalDateTime ldt1 = timeService.now();
        LocalDateTime ldt2 = LocalDateTime.now();
        assertTrue(ldt1.isBefore(ldt2));
        assertTrue(ldt1.plusSeconds(5).isAfter(ldt2));
    }
}