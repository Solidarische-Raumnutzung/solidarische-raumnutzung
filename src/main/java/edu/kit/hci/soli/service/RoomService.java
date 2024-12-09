package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return getOptional(1L).orElseThrow();
    }

    public Room get(long id) {
        return getOptional(id).orElseThrow();
    }

    public Optional<Room> getOptional(long id) {
        return roomRepository.findById(id);
    }
}
