package edu.kit.hci.soli.service.impl;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.RoomOpeningHours;
import edu.kit.hci.soli.repository.RoomOpeningHoursRepository;
import edu.kit.hci.soli.repository.RoomRepository;
import edu.kit.hci.soli.service.RoomService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomOpeningHoursRepository roomOpeningHoursRepository;

    /**
     * Constructs a RoomService with the specified {@link RoomRepository}.
     *
     * @param roomRepository the repository for managing Room entities
     */
    public RoomServiceImpl(RoomRepository roomRepository, RoomOpeningHoursRepository roomOpeningHoursRepository) {
        this.roomRepository = roomRepository;
        this.roomOpeningHoursRepository = roomOpeningHoursRepository;
    }

    @Override
    public boolean existsById(Long id) {
        return roomRepository.existsById(id);
    }

    @Override
    public Optional<Room> getOptional(long id) {
        return roomRepository.findById(id);
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public void delete(Room room) {
        roomRepository.delete(room);
    }

    @Override
    public List<RoomOpeningHours> getOpeningHours(long roomId) {
        return roomOpeningHoursRepository.findByRoomId(roomId);
    }

    @Override
    public void saveOpeningHours(long roomId, LocalTime start, LocalTime end, DayOfWeek dayOfWeek) {
        RoomOpeningHours openingHours = new RoomOpeningHours();
        openingHours.setRoom(roomRepository.findById(roomId).orElseThrow());
        openingHours.setStartTime(start);
        openingHours.setEndTime(end);
        openingHours.setDayOfWeek(dayOfWeek);
        openingHours.setId(getDayOfWeek(dayOfWeek));
        roomOpeningHoursRepository.save(openingHours);
    }

    // I didn't include the ID directly in SQL because I wanted to keep a fix easy if FullCalender changes Weekday IDs
    private long getDayOfWeek(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> 1;
            case TUESDAY -> 2;
            case WEDNESDAY -> 3;
            case THURSDAY -> 4;
            case FRIDAY -> 5;
            case SATURDAY -> 6;
            case SUNDAY -> 0;
            default -> throw new IllegalArgumentException("Invalid day of week: " + day.name());
        };
    }
}
