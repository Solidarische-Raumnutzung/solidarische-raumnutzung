package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * Repository for managing {@link Booking} entities.
 * Extends {@link JpaRepository} to provide CRUD operations.
 */
public interface BookingsRepository extends JpaRepository<Booking, Serializable> {
    /**
     * Finds bookings that overlap with the specified time range.
     *
     * @param start the start of the time range
     * @param end the end of the time range
     * @return a stream of bookings that overlap with the specified time range
     */
    @Query("SELECT b FROM Booking b WHERE (b.startDate > :start AND b.startDate < :end) OR (b.endDate > :start AND b.endDate < :end) OR (b.startDate <= :start AND b.endDate >= :end)")
    Stream<Booking> findOverlappingBookings(LocalDateTime start, LocalDateTime end);

    /**
     * Finds bookings by user and room.
     *
     * @param user the user associated with the bookings
     * @param room the room associated with the bookings
     * @return a list of bookings for the specified user and room
     */
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.room = :room")
    List<Booking> findByUserAndRoom(User user, Room room);

    /**
     * Deletes all bookings for the specified user.
     *
     * @param user the user whose bookings are to be deleted
     */
    @Transactional
    void deleteAllByUser(User user);
}
