package edu.kit.hci.soli.test;

import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.repository.RoomRepository;
import edu.kit.hci.soli.repository.UserRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired private UserRepository userRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private BookingsRepository bookingsRepository;

    @Autowired private UserService userService;

    public User user;
    public User user2;
    public User user3;
    @Autowired
    private RoomService roomService;
    @Autowired
    private BookingsService bookingsService;

    public void reset() {
        bookingsRepository.deleteAll(bookingsRepository.findAll());
//        roomRepository.deleteAll(roomRepository.findAll());
        userRepository.deleteAll(userRepository.findAll());

        user = userService.resolveAdminUser();
        user2 = userService.resolveGuestUser("testuser2");
        user3 = userService.resolveGuestUser("testuser3");
    }

    public Booking createBooking(User user) {
        Booking booking = new Booking();
        booking.setRoom(roomService.get());
        booking.setUser(user);
        booking.setStartDate(bookingsService.currentSlot().plusDays(1));
        booking.setEndDate(bookingsService.currentSlot().plusDays(2));
        booking.setPriority(Priority.HIGHEST);
        booking.setShareRoomType(ShareRoomType.ON_REQUEST);
        return booking;
    }
}
