package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public interface BookingsRepository extends JpaRepository<Booking, Serializable> {
    @Query("SELECT b FROM Booking b WHERE b.room = :room AND ((b.startDate > :start AND b.startDate < :end) OR (b.endDate > :start AND b.endDate < :end) OR (b.startDate <= :start AND b.endDate >= :end))")
    Stream<Booking> findOverlappingBookings(Room room, LocalDateTime start, LocalDateTime end);

    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.room = :room")
    List<Booking> findByUserAndRoom(Room room, User user);

    @Transactional
    void deleteAllByUser(User user);
}
