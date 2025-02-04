package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.config.security.SoliUserDetailsService;
import edu.kit.hci.soli.service.SystemConfigurationService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class SystemConfigurationServiceTest {
    @Autowired private SystemConfigurationService systemConfigurationService;
    @Autowired private TestService testService;
    @Autowired private SoliUserDetailsService soliUserDetailsService;

    @BeforeEach
    public void setUp() {
        testService.reset();
    }

    @AfterAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @Test
    public void testSetGuestLoginEnabled() {
        assertTrue(systemConfigurationService.isGuestLoginEnabled());
        assertDoesNotThrow(() -> soliUserDetailsService.loadUserByUsername("someone@example.com"));
        systemConfigurationService.setGuestLoginEnabled(false);
        assertFalse(systemConfigurationService.isGuestLoginEnabled());
        assertThrows(UsernameNotFoundException.class, () -> soliUserDetailsService.loadUserByUsername("someone@example.com"));
        systemConfigurationService.setGuestLoginEnabled(true);
        assertTrue(systemConfigurationService.isGuestLoginEnabled());
        assertDoesNotThrow(() -> soliUserDetailsService.loadUserByUsername("someone@example.com"));
    }
}
