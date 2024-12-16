package edu.kit.hci.soli.service.impl;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.repository.RoomRepository;
import edu.kit.hci.soli.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    // We currently only have one room, so we can hardcode the id

    private final RoomRepository roomRepository;

    /**
     * Constructs a RoomService with the specified {@link RoomRepository}.
     *
     * @param roomRepository the repository for managing Room entities
     */
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public boolean existsById(Long id) {
        return id == 1;
    }

    @Override
    public Room get() {
        return get(1L);
    }

    @Override
    public Room get(long id) {
        return getOptional(id).orElseThrow();
    }

    @Override
    public Optional<Room> getOptional(long id) {
        return roomRepository.findById(id);
    }
}
