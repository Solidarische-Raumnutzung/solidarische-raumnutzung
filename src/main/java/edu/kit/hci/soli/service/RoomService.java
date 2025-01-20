package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.RoomOpeningHours;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class for managing {@link Room} entities.
 * Provides methods to change and retrieve room details.
 */
public interface RoomService {
    /**
     * Checks if a room exists by its ID.
     *
     * @param id the ID of the room
     * @return true if the room exists, false otherwise
     */
    boolean existsById(Long id);

    /**
     * Retrieves the room with the hardcoded ID of 1.
     *
     * @return the room with ID 1
     * @throws NoSuchElementException if the room is not found
     */
    Room get();

    /**
     * Retrieves a room by its ID.
     *
     * @param id the ID of the room
     * @return the room with the specified ID
     * @throws NoSuchElementException if the room is not found
     */
    Room get(long id);

    /**
     * Retrieves a room by its ID.
     *
     * @param id the ID of the room
     * @return the room with the specified ID or {@link Optional#empty}
     */
    Optional<Room> getOptional(long id);

    List<RoomOpeningHours> getOpeningHours(long roomId);

    void saveOpeningHours(long roomId, LocalTime start, LocalTime end, DayOfWeek dayOfWeek);
}
