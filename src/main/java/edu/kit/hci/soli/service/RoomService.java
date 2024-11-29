package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    // We currently only have one room, so we can hardcode the id

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public boolean existsById(Long id) {
        return id == 1;
    }

    public Room get() {
        return roomRepository.findById(1L).orElseThrow();
    }
}
