package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.BookingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingsService {

    private final BookingsRepository bookingsRepository;

    public  BookingsService(BookingsRepository bookingsRepository){
        this.bookingsRepository = bookingsRepository;
    }

    public boolean create(Booking booking) {
        bookingsRepository.save(booking);
        return true;
    }

    public Booking getBookingById(Long id) {
        return bookingsRepository.findById(id).orElse(null);
    }

    public boolean delete(Booking booking) {
        bookingsRepository.delete(booking);
        return true;
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingsRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<CalendarEvent> getCalendarEvents(LocalDateTime start, LocalDateTime end) {
        return bookingsRepository.findOverlappingBookings(start, end)
                .map(booking -> new BookingsService.CalendarEvent(
                        "/" + booking.getRoom().getId() + "/bookings/view/" + booking.getId(),
                        booking.getPriority().name(), //TODO we should localize this and/or insert it via CSS
                        booking.getStartDate(),
                        booking.getEndDate(),
                        List.of("event-" + booking.getPriority().name().toLowerCase()) //TODO if we own the event, we should add a class to highlight it
                ))
                .toList();
    }

    public record CalendarEvent(String url, String title, LocalDateTime start, LocalDateTime end, List<String> className) { }
}
