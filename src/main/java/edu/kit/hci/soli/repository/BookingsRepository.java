package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.dto.BookingByDay;
import edu.kit.hci.soli.dto.BookingByHour;
import edu.kit.hci.soli.dto.BookingByMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.stream.Stream;

/**
 * Repository for managing {@link Booking} entities.
 * Extends {@link JpaRepository} to provide CRUD operations.
 */
public interface BookingsRepository extends JpaRepository<Booking, Serializable> {
    /**
     * Finds bookings that overlap with the specified time range.
     *
     * @param room  the room in which to search
     * @param start the start of the time range
     * @param end   the end of the time range
     * @return a stream of bookings that overlap with the specified time range
     */
    @Query("SELECT b FROM Booking b WHERE b.room = :room AND ((b.startDate > :start AND b.startDate < :end) OR (b.endDate > :start AND b.endDate < :end) OR (b.startDate <= :start AND b.endDate >= :end))")
    Stream<Booking> findOverlappingBookings(Room room, LocalDateTime start, LocalDateTime end);

    /**
     * Finds bookings by user and room.
     *
     * @param room the room associated with the bookings
     * @param user the user associated with the bookings
     * @param pageable the pagination information
     * @return the page of bookings
     */
    Page<Booking> findByUserAndRoom(User user, Room room, Pageable pageable);

    /**
     * Checks if a booking exists for the specified user.
     *
     * @param user the user to check for
     * @return true if a booking exists for the user, false otherwise
     */
    boolean existsByUser(User user);

    /**
     * Deletes all bookings for the specified user.
     *
     * @param user the user whose bookings are to be deleted
     */
    void deleteAllByUser(User user);

    /**
     * Deletes all bookings for guests.
     */
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.id IN (SELECT b1.id FROM Booking b1 JOIN b1.user u WHERE u.userId LIKE 'guest/%')")
    void deleteAllBookingsByGuests();

    /**
     * Returns the number of bookings per weekday (all time).
     *
     * @return the number of bookings per weekday
     */
    @Query("SELECT NEW edu.kit.hci.soli.dto.BookingByDay(DAY_OF_WEEK(b.startDate), COUNT(b)) FROM Booking b GROUP BY DAY_OF_WEEK(b.startDate)")
    Stream<BookingByDay> countBookingsPerWeekdayAllTime();

    /**
     * Returns the number of bookings per weekday (recent).
     *
     * @param frame the amount of time to look back
     * @return the number of bookings per weekday
     */
    @Query("SELECT NEW edu.kit.hci.soli.dto.BookingByDay(DAY_OF_WEEK(b.startDate), COUNT(b)) FROM Booking b WHERE CURRENT_TIMESTAMP - b.endDate <= :frame GROUP BY DAY_OF_WEEK(b.startDate)")
    Stream<BookingByDay> countBookingsPerWeekdayRecent(Duration frame);

    /**
     * Returns the number of bookings per hour of day (all time).
     *
     * @return the number of bookings per hour of day
     */
    @Query("SELECT NEW edu.kit.hci.soli.dto.BookingByHour(EXTRACT(HOUR FROM b.startDate), COUNT(b)) FROM Booking b GROUP BY EXTRACT(HOUR FROM b.startDate)")
    Stream<BookingByHour> countBookingsPerHourAllTime();

    /**
     * Returns the number of bookings per hour of day (recent).
     *
     * @param frame the amount of time to look back
     * @return the number of bookings per hour of day
     */
    @Query("SELECT NEW edu.kit.hci.soli.dto.BookingByHour(EXTRACT(HOUR FROM b.startDate), COUNT(b)) FROM Booking b WHERE CURRENT_TIMESTAMP - b.endDate <= :frame GROUP BY EXTRACT(HOUR FROM b.startDate)")
    Stream<BookingByHour> countBookingsPerHourRecent(Duration frame);

    /**
     * Returns the number of bookings per month (all time).
     *
     * @return the number of bookings per month
     */
    @Query("SELECT NEW edu.kit.hci.soli.dto.BookingByMonth(EXTRACT(MONTH FROM b.startDate), COUNT(b)) FROM Booking b GROUP BY EXTRACT(MONTH FROM b.startDate)")
    Stream<BookingByMonth> countBookingsPerMonthAllTime();

    /**
     * Returns the number of bookings per month (recent).
     *
     * @param frame the amount of time to look back
     * @return the number of bookings per month
     */
    @Query("SELECT NEW edu.kit.hci.soli.dto.BookingByMonth(EXTRACT(MONTH FROM b.startDate), COUNT(b)) FROM Booking b WHERE CURRENT_TIMESTAMP - b.endDate <= :frame GROUP BY EXTRACT(MONTH FROM b.startDate)")
    Stream<BookingByMonth> countBookingsPerMonthRecent(Duration frame);
}