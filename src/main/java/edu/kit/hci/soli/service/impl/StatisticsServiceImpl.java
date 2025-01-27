package edu.kit.hci.soli.service.impl;

import edu.kit.hci.soli.dto.BookingByDay;
import edu.kit.hci.soli.dto.BookingByHour;
import edu.kit.hci.soli.dto.BookingByMonth;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.service.StatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAmount;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final BookingsRepository bookingsRepository;

    public StatisticsServiceImpl(BookingsRepository bookingsRepository) {
        this.bookingsRepository = bookingsRepository;
    }

    public DayOfWeek getDayOfWeek(int dayOfWeek) {
        return DayOfWeek.of(dayOfWeek == 0 ? 7 : dayOfWeek);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<DayOfWeek, Long> countBookingsPerWeekdayAllTime() {
        SortedMap<DayOfWeek, Long> result = new TreeMap<>();
        try (Stream<BookingByDay> stats = bookingsRepository.countBookingsPerWeekdayAllTime()) {
            stats.forEach(e -> result.put(getDayOfWeek(e.getDayOfWeek()), e.getCount()));
        }
        for (DayOfWeek value : DayOfWeek.values()) {
            if (value == DayOfWeek.SATURDAY || value == DayOfWeek.SUNDAY) continue;
            result.putIfAbsent(value, 0L);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<DayOfWeek, Long> countBookingsPerWeekdayRecent(TemporalAmount frame) {
        SortedMap<DayOfWeek, Long> result = new TreeMap<>();
        try (Stream<BookingByDay> stats = bookingsRepository.countBookingsPerWeekdayRecent(Duration.from(frame))) {
            stats.forEach(e -> result.put(getDayOfWeek(e.getDayOfWeek()), e.getCount()));
        }
        for (DayOfWeek value : DayOfWeek.values()) {
            if (value == DayOfWeek.SATURDAY || value == DayOfWeek.SUNDAY) continue;
            result.putIfAbsent(value, 0L);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> countBookingsPerHourAllTime() {
        SortedMap<Integer, Long> result = new TreeMap<>();
        try (Stream<BookingByHour> stats = bookingsRepository.countBookingsPerHourAllTime()) {
            stats.forEach(e -> result.put(e.getHour(), e.getCount()));
        }
        for (int i = 0; i < 24; i++) {
            result.putIfAbsent(i, 0L);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> countBookingsPerHourRecent(TemporalAmount frame) {
        SortedMap<Integer, Long> result = new TreeMap<>();
        try (Stream<BookingByHour> stats = bookingsRepository.countBookingsPerHourRecent(Duration.from(frame))) {
            stats.forEach(e -> result.put(e.getHour(), e.getCount()));
        }
        for (int i = 0; i < 24; i++) {
            result.putIfAbsent(i, 0L);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Month, Long> countBookingsPerMonthAllTime() {
        SortedMap<Month, Long> result = new TreeMap<>();
        try (Stream<BookingByMonth> stats = bookingsRepository.countBookingsPerMonthAllTime()) {
            stats.forEach(e -> result.put(Month.of(e.getMonth()), e.getCount()));
        }
        for (Month value : Month.values()) {
            result.putIfAbsent(value, 0L);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Month, Long> countBookingsPerMonthRecent(TemporalAmount frame) {
        SortedMap<Month, Long> result = new TreeMap<>();
        try (Stream<BookingByMonth> stats = bookingsRepository.countBookingsPerMonthRecent(Duration.from(frame))) {
            stats.forEach(e -> result.put(Month.of(e.getMonth()), e.getCount()));
        }
        for (Month value : Month.values()) {
            result.putIfAbsent(value, 0L);
        }
        return result;
    }
}
