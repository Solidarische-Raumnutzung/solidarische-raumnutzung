package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Room;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface RoomService {
    boolean existsById(Long id);

    Room get();

    Room get(long id);

    Optional<Room> getOptional(long id);
}
