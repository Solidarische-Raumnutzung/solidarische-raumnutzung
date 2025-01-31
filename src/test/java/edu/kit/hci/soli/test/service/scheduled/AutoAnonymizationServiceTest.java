package edu.kit.hci.soli.test.service.scheduled;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.repository.UserRepository;
import edu.kit.hci.soli.service.TimeService;
import edu.kit.hci.soli.service.scheduled.AutoAnonymizationService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class AutoAnonymizationServiceTest {
    @Autowired private TestService testService;
    @Autowired private TimeService timeService;
    @Autowired private AutoAnonymizationService autoAnonymizationService;
    @Autowired private BookingsRepository bookingsRepository;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        testService.reset();
    }

    @AfterAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @Test
    public void testDeleteOutdatedRequests() {
        Booking testBooking = testService.createBooking(testService.user, timeService.now().minusMinutes(1));
        Booking testBooking2 = testService.createBooking(testService.user, timeService.now().minusMinutes(1));
        Booking testBooking3 = testService.createBooking(testService.user, timeService.now().plusMinutes(3));
        testBooking.setOpenRequests(Set.of(testService.user2));
        testBooking3.setOpenRequests(Set.of(testService.user2));
        testBooking = bookingsRepository.save(testBooking);
        testBooking2 = bookingsRepository.save(testBooking2);
        testBooking3 = bookingsRepository.save(testBooking3);
        autoAnonymizationService.scheduledAnonymize();
        assertTrue(bookingsRepository.findById(testBooking.getId()).isEmpty());
        assertTrue(bookingsRepository.findById(testBooking2.getId()).isPresent());
        assertTrue(bookingsRepository.findById(testBooking3.getId()).isPresent());
    }

    @Test
    public void testAnonymizeBookings() {
        Booking testBooking = testService.createBooking(testService.user, timeService.now().minusMonths(4));
        Booking testBooking2 = testService.createBooking(testService.user, timeService.now().minusMonths(2));
        testBooking = bookingsRepository.save(testBooking);
        testBooking2 = bookingsRepository.save(testBooking2);
        autoAnonymizationService.scheduledAnonymize();
        assertEquals(testService.anon, bookingsRepository.findById(testBooking.getId()).orElseThrow().getUser());
        assertEquals(testService.user, bookingsRepository.findById(testBooking2.getId()).orElseThrow().getUser());
    }

    @Test
    public void testDeleteUnusedAccounts() {
        testService.user.setLastLogin(timeService.now().minusMonths(4));
        testService.user2.setLastLogin(timeService.now().minusMonths(4));
        testService.user3.setLastLogin(timeService.now().minusMonths(2));
        testService.user = userRepository.save(testService.user);
        testService.user2 = userRepository.save(testService.user2);
        testService.user3 = userRepository.save(testService.user3);
        autoAnonymizationService.scheduledAnonymize();
        assertTrue(userRepository.findById(testService.user.getId()).isPresent());
        assertTrue(userRepository.findById(testService.user2.getId()).isEmpty());
        assertTrue(userRepository.findById(testService.user3.getId()).isPresent());
    }
}
