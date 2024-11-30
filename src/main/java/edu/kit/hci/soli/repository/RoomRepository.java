package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
