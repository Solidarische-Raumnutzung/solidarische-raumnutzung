package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.service.BookingsService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class BookingsServiceTest {
    @Autowired private BookingsService bookingsService;
    @Autowired private BookingsRepository bookingsRepository;
    @Autowired private TestService testService;

    private Booking testBooking;

    @BeforeEach
    public void setUp() {
        testService.reset();

        testBooking = new Booking();
        testBooking.setUser(testService.user);
        testBooking.setStartDate(LocalDateTime.now().plusDays(1));
        testBooking.setEndDate(LocalDateTime.now().plusDays(2));
        testBooking.setPriority(Priority.HIGHEST);
        testBooking.setShareRoomType(ShareRoomType.ON_REQUEST);
    }

    @Test
    public void testAttemptToBookBooking() {
        BookingsService.BookingAttemptResult result = bookingsService.attemptToBook(testBooking);
        assertInstanceOf(BookingsService.BookingAttemptResult.Success.class, result);
        assertTrue(bookingsRepository.existsById(testBooking.getId()));
    }

    @Test
    public void testGetBookingsByUser() {
        bookingsRepository.save(testBooking);
        List<Booking> bookings = bookingsService.getBookingsByUser(testService.user);
        assertNotEquals(0, bookings.size());
        assertEquals(testBooking.getId(), bookings.getFirst().getId());
    }
}