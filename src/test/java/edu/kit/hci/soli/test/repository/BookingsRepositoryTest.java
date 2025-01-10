package edu.kit.hci.soli.test.repository;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.dto.BookingByDay;
import edu.kit.hci.soli.dto.BookingByHour;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class BookingsRepositoryTest {
    @Autowired private BookingsRepository bookingsRepository;
    @Autowired private TestService testService;
    @Autowired private BookingsService bookingsService;

    private Booking testBooking;
    private Booking testBooking2;
    private Booking testBooking3;

    @BeforeEach
    public void setUp() {
        testService.reset();
        testBooking = testService.createBooking(testService.user);
        testBooking2 = testService.createBooking(testService.user2);
        testBooking3 = testService.createBooking(testService.user3);
    }

    @AfterAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @Test
    @Transactional
    public void testDeleteAllBookingsByGuests() {
        bookingsRepository.save(testBooking);
        bookingsRepository.save(testBooking2);
        bookingsRepository.save(testBooking3);
        assertEquals(3, bookingsRepository.count());
        bookingsRepository.deleteAllBookingsByGuests();
        assertEquals(1, bookingsRepository.count());
    }

    @Test
    @Transactional
    public void validateStatistics() {
        bookingsRepository.save(testBooking);
        bookingsRepository.save(testBooking2);
        bookingsRepository.save(testBooking3);
        assertEquals(3, bookingsRepository.countBookingsPerWeekdayRecent(Duration.ofDays(7))
                .collect(Collectors.toMap(BookingByDay::getDayOfWeek, BookingByDay::getCount))
                .get(testBooking.getStartDate().getDayOfWeek().getValue()));
    }
}
