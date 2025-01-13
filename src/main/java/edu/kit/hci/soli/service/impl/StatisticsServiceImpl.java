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
        try (Stream<BookingByDay> stats = bookingsRepository.countBookingsPerWeekdayAllTime()) {
            return stats.collect(Collectors.toMap(
                    s -> getDayOfWeek(s.getDayOfWeek()),
                    BookingByDay::getCount
            ));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<DayOfWeek, Long> countBookingsPerWeekdayRecent(TemporalAmount frame) {
        try (Stream<BookingByDay> stats = bookingsRepository.countBookingsPerWeekdayRecent(Duration.from(frame))) {
            return stats.collect(Collectors.toMap(
                    s -> getDayOfWeek(s.getDayOfWeek()),
                    BookingByDay::getCount
            ));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> countBookingsPerHourAllTime() {
        try (Stream<BookingByHour> stats = bookingsRepository.countBookingsPerHourAllTime()) {
            return stats.collect(Collectors.toMap(
                    BookingByHour::getHour,
                    BookingByHour::getCount
            ));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> countBookingsPerHourRecent(TemporalAmount frame) {
        try (Stream<BookingByHour> stats = bookingsRepository.countBookingsPerHourRecent(Duration.from(frame))) {
            return stats.collect(Collectors.toMap(
                    BookingByHour::getHour,
                    BookingByHour::getCount
            ));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Month, Long> countBookingsPerMonthAllTime() {
        try (Stream<BookingByMonth> stats = bookingsRepository.countBookingsPerMonthAllTime()) {
            return stats.collect(Collectors.toMap(
                    s -> Month.of(s.getMonth()),
                    BookingByMonth::getCount
            ));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Month, Long> countBookingsPerMonthRecent(TemporalAmount frame) {
        try (Stream<BookingByMonth> stats = bookingsRepository.countBookingsPerMonthRecent(Duration.from(frame))) {
            return stats.collect(Collectors.toMap(
                    s -> Month.of(s.getMonth()),
                    BookingByMonth::getCount
            ));
        }
    }
}
