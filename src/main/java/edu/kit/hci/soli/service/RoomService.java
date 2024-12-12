package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.NoSuchElementException;

/**
 * Service class for managing {@link Room} entities.
 * Provides methods to change and retrieve room details.
 */
@Service
public class RoomService {
    // We currently only have one room, so we can hardcode the id

    private final RoomRepository roomRepository;

    /**
     * Constructs a RoomService with the specified {@link RoomRepository}.
     *
     * @param roomRepository the repository for managing Room entities
     */
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Checks if a room exists by its ID.
     *
     * @param id the ID of the room
     * @return true if the room exists, false otherwise
     */
    public boolean existsById(Long id) {
        return id == 1;
    }

    /**
     * Retrieves the room with the hardcoded ID of 1.
     *
     * @return the room with ID 1
     * @throws NoSuchElementException if the room is not found
     */
    public Room get() {
        return get(1L);
    }

    /**
     * Retrieves a room by its ID.
     *
     * @param id the ID of the room
     * @return the room with the specified ID
     * @throws NoSuchElementException if the room is not found
     */
    public Room get(long id) {
        return getOptional(id).orElseThrow();
    }

    /**
     * Retrieves a room by its ID.
     *
     * @param id the ID of the room
     * @return the room with the specified ID or {@link Optional#empty}
     */
    public Optional<Room> getOptional(long id) {
        return roomRepository.findById(id);
    }
}
