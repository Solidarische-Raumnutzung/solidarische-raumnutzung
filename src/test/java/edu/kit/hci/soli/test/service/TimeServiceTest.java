package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.service.TimeService;
import jakarta.transaction.Transactional;
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
        assertFalse(timeService.currentSlot().isAfter(timeService.minimumTime()));
        assertTrue(timeService.currentSlot().isBefore(timeService.maximumTime()));
        assertTrue(timeService.currentSlot().plusMinutes(16).isAfter(timeService.minimumTime()));
    }

    @Test
    @Transactional
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