package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Priority;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.BookingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class BookingsServiceTest {

    @Autowired
    private BookingsService bookingsService;

    @Autowired
    private BookingsRepository bookingsRepository;

    @Autowired
    private UserService userService;

    private User testUser;
    private Booking testBooking;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        userService.create(testUser);

        testBooking = new Booking();
        testBooking.setUser(testUser);
        testBooking.setStartDate(LocalDateTime.now().plusDays(1));
        testBooking.setEndDate(LocalDateTime.now().plusDays(2));
        testBooking.setPriority(Priority.HIGHEST);
    }

    @Test
    public void testCreateBooking() {

        boolean result = bookingsService.create(testBooking);
        assertThat(result).isTrue();
        assertThat(bookingsRepository.findById(testBooking.getId())).isPresent();
    }

    @Test
    public void testGetBookingsByUser() {
        bookingsRepository.save(testBooking);
        List<Booking> bookings = bookingsService.getBookingsByUser(testUser);
        assertThat(bookings).isNotEmpty();
        assertThat(bookings.getFirst().getId()).isEqualTo(testBooking.getId());
    }
}