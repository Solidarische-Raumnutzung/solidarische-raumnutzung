package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Priority;
import edu.kit.hci.soli.domain.ShareRoomType;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.service.*;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class UserServiceTest {
    @Autowired private UserService userService;
    @Autowired private TestService testService;
    @Autowired private BookingsService bookingsService;
    @Autowired private RoomService roomService;
    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @BeforeAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @AfterEach
    public void tearDown() {
        testService.reset();
    }

    @Test
    public void testFindByUserId() {
        assertEquals(testService.user, userService.findByUserId(testService.user.getUserId()));
        assertEquals(testService.user, userService.findByUserId("admin"));
        assertEquals(testService.user2, userService.findByUserId(testService.user2.getUserId()));
        assertEquals(testService.user3, userService.findByUserId(testService.user3.getUserId()));
    }

    @Test
    public void testToggleUserEnabled() {
        userService.toggleUserEnabled(testService.user2);
        assertEquals(true, userService.findByUserId(testService.user2.getUserId()).isDisabled());
        userService.toggleUserEnabled(testService.user2);
        assertEquals(false, userService.findByUserId(testService.user2.getUserId()).isDisabled());
        assertThrows(IllegalArgumentException.class, () -> userService.toggleUserEnabled(testService.user));
    }

    @Test
    public void testGetManageableUsers() {
        assertIterableEquals(List.of(testService.user2, testService.user3), userService.getManageableUsers());
        assertTrue(userService.deleteUser(testService.user2));
        assertIterableEquals(List.of(testService.user3), userService.getManageableUsers());
    }

    @Test
    public void testGetById() {
        assertEquals(testService.user, userService.getById(testService.user.getId()));
    }

    @Test
    public void testIsAdmin() {
        assertTrue(userService.isAdmin(testService.user));
        assertFalse(userService.isAdmin(testService.user2));
        assertFalse(userService.isAdmin(testService.user3));
    }

    @Test
    public void testResolveGuestUser() {
        assertEquals(testService.user2, userService.resolveGuestUser("testuser2"));
        assertEquals(testService.user3, userService.resolveGuestUser("testuser3"));
    }

    @Test
    public void testIsGuest() {
        assertTrue(systemConfigurationService.isGuestLoginEnabled());
        assertFalse(userService.isGuest(testService.user));
        assertTrue(userService.isGuest(testService.user2));
        assertTrue(userService.isGuest(testService.user3));
    }

    @Test
    public void testDeleteUser() {
        assertTrue(userService.deleteUser(testService.user2));
        assertNull(userService.findByUserId(testService.user2.getUserId()));
        assertIterableEquals(List.of(testService.user3), userService.getManageableUsers());
        assertFalse(userService.deleteUser(testService.user2));

        Booking testBooking = testService.createBooking(testService.user3);
        testBooking = assertInstanceOf(BookingAttemptResult.Success.class, bookingsService.attemptToBook(testBooking)).booking();
        assertFalse(userService.deleteUser(testService.user3));
        bookingsService.delete(testBooking, BookingDeleteReason.SELF);
        assertTrue(userService.deleteUser(testService.user3));

        assertThrows(IllegalArgumentException.class, () -> userService.toggleUserEnabled(testService.user3));

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(testService.user));
    }
}
