package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing {@link Room} entities.
 * Extends {@link JpaRepository} to provide CRUD operations.
 */
public interface RoomRepository extends JpaRepository<Room, Long> {
}
