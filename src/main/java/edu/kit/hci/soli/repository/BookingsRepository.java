package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface BookingsRepository extends JpaRepository<Booking, Serializable> {
}
