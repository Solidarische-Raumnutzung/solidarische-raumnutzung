package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class RoomServiceTest {
    @Autowired private TestService testService;
    @Autowired private RoomService roomService;

    @BeforeEach
    public void setUp() {
        testService.reset();
    }

    @AfterAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @Test
    public void testCreateRoom() {
        assertIterableEquals(List.of(testService.room), roomService.getAll());
        Room neue = new Room(null, "Neue", "Beschreibung", "Ort");
        neue = roomService.save(neue);
        assertEquals(Set.of(testService.room, neue), Set.copyOf(roomService.getAll()));
        assertEquals(Optional.of(neue), roomService.getOptional(neue.getId()));
    }

    @Test
    public void testDeleteRoom() {
        assertIterableEquals(List.of(testService.room), roomService.getAll());
        roomService.delete(testService.room);
        assertIterableEquals(List.of(), roomService.getAll());
        assertNotNull(testService.room.getId());
        assertEquals(Optional.empty(), roomService.getOptional(testService.room.getId()));
    }
}
