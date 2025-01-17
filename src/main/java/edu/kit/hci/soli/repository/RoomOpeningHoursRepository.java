package edu.kit.hci.soli.repository;

import edu.kit.hci.soli.domain.RoomOpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomOpeningHoursRepository extends JpaRepository<RoomOpeningHours, Long> {
    List<RoomOpeningHours> findByRoomId(Long roomId);
}