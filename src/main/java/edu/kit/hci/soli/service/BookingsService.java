package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingsService {

    private final BookingsRepository bookingsRepository;

    public  BookingsService(BookingsRepository bookingsRepository){
        this.bookingsRepository = bookingsRepository;
    }

    public boolean create(Booking booking) {
        bookingsRepository.findAll();

        return true;
    }

}
