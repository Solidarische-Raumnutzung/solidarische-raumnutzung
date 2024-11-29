package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public interface BookingsRepository extends JpaRepository<Booking, Serializable> {
    @Query("SELECT b FROM Booking b WHERE (b.startDate BETWEEN :start AND :end) OR (b.endDate BETWEEN :start AND :end) OR (b.startDate <= :start AND b.endDate >= :end)")
    Stream<Booking> findOverlappingBookings(LocalDateTime start, LocalDateTime end);

    List<Booking> findByUser(User user);
}
