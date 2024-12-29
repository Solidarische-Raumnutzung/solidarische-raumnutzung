package edu.kit.hci.soli.test.repository;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class BookingsRepositoryTest {
    @Autowired private BookingsRepository bookingsRepository;
    @Autowired private TestService testService;

    private Booking testBooking;
    private Booking testBooking2;
    private Booking testBooking3;

    @BeforeAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @BeforeEach
    public void setUp() {
        testBooking = testService.createBooking(testService.user);
        testBooking2 = testService.createBooking(testService.user2);
        testBooking3 = testService.createBooking(testService.user3);
    }

    @AfterEach
    public void tearDown() {
        testService.reset();
    }

    @Test
    public void testDeleteAllBookingsByGuests() {
        bookingsRepository.deleteAllBookingsByGuests();
        assertEquals(1, bookingsRepository.count());
    }
}
