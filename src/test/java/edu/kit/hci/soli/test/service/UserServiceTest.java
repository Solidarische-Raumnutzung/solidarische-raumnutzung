package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.repository.UserRepository;
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
import java.util.UUID;

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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TimeService timeService;

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
    public void testSetUserActive() {
        assertThrows(IllegalArgumentException.class, () -> userService.setUserActive(testService.user, true));
        assertThrows(IllegalArgumentException.class, () -> userService.setUserActive(testService.user, false));
        userService.setUserActive(testService.user2, false);
        assertTrue(userService.findByUserId(testService.user2.getUserId()).isDisabled());
        assertThrows(IllegalArgumentException.class, () -> userService.setUserActive(testService.user2, false));
        assertTrue(userService.findByUserId(testService.user2.getUserId()).isDisabled());
        userService.setUserActive(testService.user2, true);
        assertFalse(userService.findByUserId(testService.user2.getUserId()).isDisabled());
        assertThrows(IllegalArgumentException.class, () -> userService.setUserActive(testService.user, false));
    }

    @Test
    public void testGetManageableUsers() {
        assertIterableEquals(List.of(testService.user2, testService.user3), userService.getManageableUsers(0, 10));
        assertTrue(userService.deleteUser(testService.user2));
        assertIterableEquals(List.of(testService.user3), userService.getManageableUsers(0, 10));
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
        long count = userRepository.count();
        assertEquals(testService.user2, userService.resolveGuestUser(testService.user2.getUserId()));
        assertEquals(testService.user3, userService.resolveGuestUser(testService.user3.getUserId()));
        assertEquals(count, userRepository.count());
        userService.resolveGuestUser("guest/" + UUID.randomUUID() + "/neue");
        assertEquals(count + 1, userRepository.count());
    }

    @Test
    public void testIsGuest() {
        assertTrue(systemConfigurationService.isGuestLoginEnabled());
        assertFalse(userService.isGuest(testService.user));
        assertTrue(userService.isGuest(testService.user2));
        assertTrue(userService.isGuest(testService.user3));
    }

    @Test
    public void testUpdateLastLogin() {
        testService.user.setLastLogin(timeService.now().minusMonths(3));
        testService.user = userRepository.save(testService.user);
        assertFalse(userService.getById(testService.user.getId()).getLastLogin().isAfter(timeService.now().minusMinutes(1)));
        userService.updateLastLogin(testService.user);
        assertTrue(userService.getById(testService.user.getId()).getLastLogin().isAfter(timeService.now().minusMinutes(1)));
    }

    @Test
    public void testDeleteUser() {
        assertTrue(userService.deleteUser(testService.user2));
        assertNull(userService.findByUserId(testService.user2.getUserId()));
        assertIterableEquals(List.of(testService.user3), userService.getManageableUsers(0, 10));
        assertFalse(userService.deleteUser(testService.user2));

        Booking testBooking = testService.createBooking(testService.user3);
        testBooking = assertInstanceOf(BookingAttemptResult.Success.class, bookingsService.attemptToBook(testBooking)).booking();
        assertFalse(userService.deleteUser(testService.user3));
        bookingsService.delete(testBooking, BookingDeleteReason.SELF);
        assertTrue(userService.deleteUser(testService.user3));

        assertThrows(IllegalArgumentException.class, () -> userService.setUserActive(testService.user3, false));

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(testService.user));
    }
}
