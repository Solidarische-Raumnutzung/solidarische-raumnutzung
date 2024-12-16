package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.BookingAttemptResult;
import edu.kit.hci.soli.dto.BookingDeleteReason;
import edu.kit.hci.soli.dto.CalendarEvent;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class BookingsServiceTest {
    @Autowired private BookingsService bookingsService;
    @Autowired private RoomService roomService;
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
        testBooking = new Booking();
        testBooking.setRoom(roomService.get());
        testBooking.setUser(testService.user);
        testBooking.setStartDate(bookingsService.currentSlot().plusDays(1));
        testBooking.setEndDate(bookingsService.currentSlot().plusDays(2));
        testBooking.setPriority(Priority.HIGHEST);
        testBooking.setShareRoomType(ShareRoomType.ON_REQUEST);

        testBooking2 = new Booking();
        testBooking2.setRoom(roomService.get());
        testBooking2.setUser(testService.user2);
        testBooking2.setStartDate(bookingsService.currentSlot().plusDays(1));
        testBooking2.setEndDate(bookingsService.currentSlot().plusDays(2));
        testBooking2.setPriority(Priority.HIGHEST);
        testBooking2.setShareRoomType(ShareRoomType.ON_REQUEST);

        testBooking3 = new Booking();
        testBooking3.setRoom(roomService.get());
        testBooking3.setUser(testService.user3);
        testBooking3.setStartDate(bookingsService.currentSlot().plusDays(1));
        testBooking3.setEndDate(bookingsService.currentSlot().plusDays(2));
        testBooking3.setPriority(Priority.HIGHEST);
        testBooking3.setShareRoomType(ShareRoomType.ON_REQUEST);
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
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking);
        assertInstanceOf(BookingAttemptResult.Success.class, result);
        assertTrue(bookingsRepository.existsById(testBooking.getId()));
    }

    @Test
    public void testAttemptToBookFailure() {
        testBooking.setShareRoomType(ShareRoomType.NO);
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        assertIterableEquals(
                List.of(testBooking),
                assertInstanceOf(BookingAttemptResult.Failure.class, result).conflict()
        );
        assertNull(testBooking2.getId());
    }

    @Test
    public void testAttemptToBookPossibleCooperationImmediate() {
        testBooking.setShareRoomType(ShareRoomType.YES);
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingAttemptResult.PossibleCooperation.Immediate immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Immediate.class,
                result
        );
        assertIterableEquals(List.of(), immediate.override());
        assertIterableEquals(List.of(testBooking), immediate.cooperate());
        assertNull(testBooking2.getId());

        result = bookingsService.affirm(testBooking2, immediate);
        assertInstanceOf(BookingAttemptResult.Success.class, result);
        assertTrue(bookingsRepository.existsById(testBooking.getId()));
        assertTrue(bookingsRepository.existsById(testBooking2.getId()));
    }

    @Test
    public void testAttemptToBookPossibleCooperationDeferred() {
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingAttemptResult.PossibleCooperation.Deferred immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Deferred.class,
                result
        );
        assertIterableEquals(List.of(), immediate.override());
        assertIterableEquals(List.of(), immediate.cooperate());
        assertIterableEquals(List.of(testBooking), immediate.contact());
        assertNull(testBooking2.getId());
    }

    @Test
    public void testConfirmRequestSingle() {
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingAttemptResult.PossibleCooperation.Deferred immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Deferred.class,
                result
        );
        result = bookingsService.affirm(testBooking2, immediate);
        testBooking2 = assertInstanceOf(BookingAttemptResult.Staged.class, result).booking();
        assertIterableEquals(List.of(testService.user), testBooking2.getOutstandingRequests());
        assertFalse(bookingsService.confirmRequest(testBooking2, testService.user2));
        assertIterableEquals(List.of(testService.user), testBooking2.getOutstandingRequests());
        assertTrue(bookingsService.confirmRequest(testBooking2, testService.user));
        assertIterableEquals(List.of(), testBooking2.getOutstandingRequests());
    }

    @Test
    public void testConfirmRequestMultiple() {
        testBooking = bookingsRepository.save(testBooking);
        testBooking2 = bookingsRepository.save(testBooking2);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking3);
        BookingAttemptResult.PossibleCooperation.Deferred immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Deferred.class,
                result
        );
        result = bookingsService.affirm(testBooking3, immediate);
        testBooking3 = assertInstanceOf(BookingAttemptResult.Staged.class, result).booking();
        assertEquals(Set.of(testService.user, testService.user2), testBooking3.getOutstandingRequests());
        assertFalse(bookingsService.confirmRequest(testBooking3, testService.user3));
        assertEquals(Set.of(testService.user, testService.user2), testBooking3.getOutstandingRequests());
        assertTrue(bookingsService.confirmRequest(testBooking3, testService.user));
        assertEquals(Set.of(testService.user2), testBooking3.getOutstandingRequests());
        assertTrue(bookingsService.confirmRequest(testBooking3, testService.user2));
        assertEquals(Set.of(), testBooking3.getOutstandingRequests());
    }

    @Test
    public void testAffirmIllegal() {
        testBooking.setShareRoomType(ShareRoomType.YES);
        testBooking = bookingsRepository.save(testBooking);
        BookingAttemptResult result = bookingsService.attemptToBook(testBooking2);
        BookingAttemptResult.PossibleCooperation.Immediate immediate = assertInstanceOf(
                BookingAttemptResult.PossibleCooperation.Immediate.class,
                result
        );
        result = bookingsService.affirm(testBooking2, immediate);
        testBooking2 = assertInstanceOf(BookingAttemptResult.Success.class, result).booking();
        assertThrows(IllegalArgumentException.class, () -> bookingsService.affirm(testBooking2, immediate));
    }

    @Test
    public void testDelete() {
        testBooking = bookingsRepository.save(testBooking);
        assertTrue(bookingsRepository.existsById(testBooking.getId()));
        bookingsService.delete(testBooking, BookingDeleteReason.ADMIN);
        assertFalse(bookingsRepository.existsById(testBooking.getId()));
    }

    @Test
    public void testGetCalendarEvents() {
        Room room = roomService.get();

        testBooking = bookingsRepository.save(testBooking);
        testBooking2 = bookingsRepository.save(testBooking2);
        testBooking3 = bookingsRepository.save(testBooking3);
        List<CalendarEvent> events = bookingsService.getCalendarEvents(room, bookingsService.currentSlot(), bookingsService.currentSlot().plusDays(3), null);
        assertEquals(3, events.size());
        assertEquals(testBooking.getStartDate(), events.get(0).start());
        assertEquals(testBooking.getEndDate(), events.get(0).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request"), events.get(0).classNames());
        assertEquals(testBooking2.getStartDate(), events.get(1).start());
        assertEquals(testBooking2.getEndDate(), events.get(1).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request"), events.get(1).classNames());
        assertEquals(testBooking3.getStartDate(), events.get(2).start());
        assertEquals(testBooking3.getEndDate(), events.get(2).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request"), events.get(2).classNames());
    }

    @Test
    public void testGetCalendarEventsAs() {
        Room room = roomService.get();

        testBooking = bookingsRepository.save(testBooking);
        testBooking2 = bookingsRepository.save(testBooking2);
        testBooking3 = bookingsRepository.save(testBooking3);
        List<CalendarEvent> events = bookingsService.getCalendarEvents(room, bookingsService.currentSlot(), bookingsService.currentSlot().plusDays(3), testService.user);
        assertEquals(3, events.size());
        assertEquals(testBooking.getStartDate(), events.get(0).start());
        assertEquals(testBooking.getEndDate(), events.get(0).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request", "calendar-event-own"), events.get(0).classNames());
        assertEquals(testBooking2.getStartDate(), events.get(1).start());
        assertEquals(testBooking2.getEndDate(), events.get(1).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request"), events.get(1).classNames());
        assertEquals(testBooking3.getStartDate(), events.get(2).start());
        assertEquals(testBooking3.getEndDate(), events.get(2).end());
        assertEquals(List.of("calendar-event-highest", "calendar-event-on_request"), events.get(2).classNames());
    }
}