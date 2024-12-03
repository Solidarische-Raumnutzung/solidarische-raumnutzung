package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.controller.BookingsController;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class BookingsServiceTest {
    @Autowired private BookingsService bookingsService;
    @Autowired private RoomService roomService;
    @Autowired private BookingsRepository bookingsRepository;
    @Autowired private TestService testService;
    @Autowired private BookingsController bookingsController;

    private Booking testBooking;
    private Booking testBooking2;

    @BeforeAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @BeforeEach
    public void setUp() {
        testBooking = new Booking();
        testBooking.setRoom(roomService.get());
        testBooking.setUser(testService.user);
        testBooking.setStartDate(bookingsController.currentSlot().plusDays(1));
        testBooking.setEndDate(bookingsController.currentSlot().plusDays(2));
        testBooking.setPriority(Priority.HIGHEST);
        testBooking.setShareRoomType(ShareRoomType.ON_REQUEST);

        testBooking2 = new Booking();
        testBooking2.setRoom(roomService.get());
        testBooking2.setUser(testService.user);
        testBooking2.setStartDate(bookingsController.currentSlot().plusDays(1));
        testBooking2.setEndDate(bookingsController.currentSlot().plusDays(2));
        testBooking2.setPriority(Priority.HIGHEST);
        testBooking2.setShareRoomType(ShareRoomType.ON_REQUEST);
    }

    @AfterEach
    public void tearDown() {
        testService.reset();
    }

    @Test
    public void testGetBookingsByUser() {
        bookingsRepository.save(testBooking);
        List<Booking> bookings = bookingsService.getBookingsByUser(testService.user, roomService.get());
        assertEquals(1, bookings.size());
        assertEquals(testBooking.getId(), bookings.getFirst().getId());
    }

    @Test
    public void testAttemptToBookBookingSuccess() {
        BookingsService.BookingAttemptResult result = bookingsService.attemptToBook(testBooking);
        assertInstanceOf(BookingsService.BookingAttemptResult.Success.class, result);
        assertTrue(bookingsRepository.existsById(testBooking.getId()));
    }

    @Test
    public void testAttemptToBookFailure() {
        testBooking.setShareRoomType(ShareRoomType.NO);
        testBooking = bookingsRepository.save(testBooking);
        BookingsService.BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        assertIterableEquals(
                List.of(testBooking),
                assertInstanceOf(BookingsService.BookingAttemptResult.Failure.class, result).conflict()
        );
        assertNull(testBooking2.getId());
    }

    @Test
    public void testAttemptToBookPossibleCooperationImmediate() {
        testBooking.setShareRoomType(ShareRoomType.YES);
        testBooking = bookingsRepository.save(testBooking);
        BookingsService.BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingsService.BookingAttemptResult.PossibleCooperation.Immediate immediate = assertInstanceOf(
                BookingsService.BookingAttemptResult.PossibleCooperation.Immediate.class,
                result
        );
        assertIterableEquals(List.of(), immediate.override());
        assertIterableEquals(List.of(testBooking), immediate.cooperate());
        assertNull(testBooking2.getId());
    }

    @Test
    public void testAttemptToBookPossibleCooperationDeferred() {
        testBooking = bookingsRepository.save(testBooking);
        BookingsService.BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingsService.BookingAttemptResult.PossibleCooperation.Deferred immediate = assertInstanceOf(
                BookingsService.BookingAttemptResult.PossibleCooperation.Deferred.class,
                result
        );
        assertIterableEquals(List.of(), immediate.override());
        assertIterableEquals(List.of(), immediate.cooperate());
        assertIterableEquals(List.of(testBooking), immediate.contact());
        assertNull(testBooking2.getId());
    }

    @Test
    public void testConfirmRequestSingle() {

    }

    @Test
    public void testConfirmRequestMultiple() {

    }

    @Test
    public void testAffirmIllegal() {

    }

    @Test
    public void testDelete() {

    }

    @Test
    public void testGetCalendarEvents() {

    }
}