package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public interface StagedBookingsRepository extends JpaRepository<StagedBooking, Long> {
    @Query("SELECT b FROM StagedBooking b WHERE (b.startDate > :start AND b.startDate < :end) OR (b.endDate > :start AND b.endDate < :end) OR (b.startDate <= :start AND b.endDate >= :end)")
    Stream<StagedBooking> findOverlappingBookings(LocalDateTime start, LocalDateTime end);

    @Query("SELECT b FROM StagedBooking b WHERE b.user = :user AND b.room = :room")
    List<StagedBooking> findByUser(User user, Room room);
}
