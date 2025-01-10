package edu.kit.hci.soli.test;

import edu.kit.hci.soli.controller.LayoutParamsAdvice;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.LayoutParams;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.repository.UserRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestService {
    @Autowired private UserRepository userRepository;
    @Autowired private BookingsRepository bookingsRepository;
    @Autowired private UserService userService;
    @Autowired private RoomService roomService;
    @Autowired private BookingsService bookingsService;
    @Autowired private LayoutParamsAdvice layoutParamsAdvice;

    public User user;
    public User user2;
    public User user3;
    public Room room;

    private LocalDateTime currentSlot;

    public void reset() {
        bookingsRepository.deleteAll(bookingsRepository.findAll());
//        roomRepository.deleteAll(roomRepository.findAll());
        userRepository.deleteAll(userRepository.findAll());

        user = userService.resolveAdminUser();
        user2 = userService.createGuestUser("testuser2");
        user3 = userService.createGuestUser("testuser3");
        room = roomService.get();

        currentSlot = bookingsService.currentSlot();
    }

    public Booking createBooking(User user) {
        Booking booking = new Booking();
        booking.setRoom(roomService.get());
        booking.setUser(user);
        booking.setStartDate(currentSlot.plusDays(1));
        booking.setEndDate(currentSlot.plusDays(2));
        booking.setPriority(Priority.HIGHEST);
        booking.setShareRoomType(ShareRoomType.ON_REQUEST);
        return booking;
    }

    public LayoutParams paramsFor(User user, HttpServletRequest request) {
        return layoutParamsAdvice.getLayoutParams(layoutParamsAdvice.getLoginStateModel(() -> user, null), request);
    }
}
