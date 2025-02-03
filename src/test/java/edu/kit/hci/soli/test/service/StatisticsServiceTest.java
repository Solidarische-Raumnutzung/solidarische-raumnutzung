package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.service.StatisticsService;
import edu.kit.hci.soli.service.TimeService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.test.context.ActiveProfiles;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class StatisticsServiceTest {
    @Autowired private BookingsRepository bookingsRepository;
    @Autowired private TestService testService;
    @Autowired private StatisticsService statisticsService;
    @Autowired private TimeService timeService;

    private Booking testBooking;
    private Booking testBooking2;
    private Booking testBooking3;

    private DayOfWeek today;
    private DayOfWeek notToday;
    private int hour;
    private int notHour;
    private Month month;
    private Month notMonth;

    private Duration recentFrame;

    @BeforeEach
    public void setUp() {
        testService.reset();
        LocalDateTime now = LocalDateTime.of(timeService.now().getYear(), 1, 12, 12, 0);
        testBooking = bookingsRepository.save(testService.createBooking(testService.user, now));
        testBooking2 = bookingsRepository.save(testService.createBooking(testService.user2, now));
        LocalDateTime old = now.minusYears(4);
        old = old.plusDays(now.getDayOfWeek().getValue() - old.getDayOfWeek().getValue());
        testBooking3 = bookingsRepository.save(testService.createBooking(testService.user3, old));
        today = testBooking.getStartDate().getDayOfWeek();
        notToday = today == DayOfWeek.MONDAY ? DayOfWeek.TUESDAY : DayOfWeek.MONDAY;
        hour = testBooking.getStartDate().getHour();
        notHour = hour == 12 ? 13 : 12;
        month = testBooking.getStartDate().getMonth();
        notMonth = month == Month.JANUARY ? Month.FEBRUARY : Month.JANUARY;
        recentFrame = Duration.ofDays(365 * 2);
    }

    @AfterAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @Test
    public void testCountBookingsPerWeekdayAllTime() {
        assertEquals(3, statisticsService.countBookingsPerWeekdayAllTime().get(today));
        assertEquals(0, statisticsService.countBookingsPerWeekdayAllTime().get(notToday));
    }

    @Test
    public void testCountBookingsPerWeekdayRecent() {
        assertEquals(2, statisticsService.countBookingsPerWeekdayRecent(recentFrame).get(today));
        assertEquals(0, statisticsService.countBookingsPerWeekdayRecent(recentFrame).get(notToday));
    }

    @Test
    public void testCountBookingsPerHourAllTime() {
        assertEquals(3, statisticsService.countBookingsPerHourAllTime().get(hour));
        assertEquals(0, statisticsService.countBookingsPerHourAllTime().get(notHour));
    }

    @Test
    public void testCountBookingsPerHourRecent() {
        assertEquals(2, statisticsService.countBookingsPerHourRecent(recentFrame).get(hour));
        assertEquals(0, statisticsService.countBookingsPerHourRecent(recentFrame).get(notHour));
    }

    @Test
    public void testCountBookingsPerMonthAllTime() {
        assertEquals(3, statisticsService.countBookingsPerMonthAllTime().get(month));
        assertEquals(0, statisticsService.countBookingsPerMonthAllTime().get(notMonth));
    }

    @Test
    public void testCountBookingsPerMonthRecent() {
        assertEquals(2, statisticsService.countBookingsPerMonthRecent(recentFrame).get(month));
        assertEquals(0, statisticsService.countBookingsPerMonthRecent(recentFrame).get(notMonth));
    }
}
