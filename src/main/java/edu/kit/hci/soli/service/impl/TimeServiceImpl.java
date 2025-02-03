package edu.kit.hci.soli.service.impl;

import edu.kit.hci.soli.config.SoliConfiguration;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.service.TimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class TimeServiceImpl implements TimeService {
    private final SoliConfiguration soliConfiguration;

    /**
     * Constructs a new {@code TimeServiceImpl} instance.
     *
     * @param soliConfiguration the app configuration
     */
    public TimeServiceImpl(SoliConfiguration soliConfiguration) {
        this.soliConfiguration = soliConfiguration;
    }

    /**
     * Normalizes a given {@link LocalDateTime} to the nearest 15-minute interval.
     * The seconds and nanoseconds are set to zero.
     *
     * @param time the time to be normalized
     * @return the normalized {@link LocalDateTime}
     */
    private LocalDateTime normalize(LocalDateTime time) {
        return time.minusMinutes(time.getMinute() % 15).withSecond(0).withNano(0);
    }

    /**
     * Returns the current time slot, normalized to the nearest 15-minute interval.
     *
     * @return the current normalized time slot
     */
    @Override
    public LocalDateTime currentSlot() {
        return normalize(now());
    }

    /**
     * Calculates the minimum valid time for a time slot. This is the
     * next available time slot, which is at least 15 minutes in the future,
     * adjusted to skip weekends.
     *
     * @return the minimum valid time slot as a {@link LocalDateTime}
     */
    @Override
    public LocalDateTime minimumTime(Room room) {
        LocalDateTime ldt = normalize(now().plusMinutes(15));
        return switch (ldt.getDayOfWeek()) {
            case SATURDAY -> minimize(room ,ldt.plusDays(2));
            case SUNDAY -> minimize(room, ldt.plusDays(1));
            default -> ldt;
        };
    }

    private LocalDateTime minimize(Room room, LocalDateTime ldt) {
        return room.getOpeningHours().get(ldt.getDayOfWeek()).getStart().atDate(ldt.toLocalDate());
    }

    /**
     * Calculates the maximum valid time for a time slot. This is the
     * latest available time slot, which is 14 days in the future,
     * adjusted to exclude weekends.
     *
     * @return the maximum valid time slot
     */
    @Override
    public LocalDateTime maximumTime(Room room) {
        LocalDateTime ldt = normalize(now().plusDays(14));
        return switch (ldt.getDayOfWeek()) {
            case SATURDAY -> maximize(room, ldt.minusDays(1));
            case SUNDAY -> maximize(room, ldt.minusDays(2));
            default -> ldt;
        };
    }

    private LocalDateTime maximize(Room room, LocalDateTime ldt) {
        return room.getOpeningHours().get(ldt.getDayOfWeek()).getEnd().atDate(ldt.toLocalDate());
    }

    /**
     * Retrieves the current time in the app configured timezone.
     *
     * @return the current time
     */
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now(soliConfiguration.getTimeZone().toZoneId());
    }
}
