package edu.kit.hci.soli.test;

import edu.kit.hci.soli.controller.LayoutParamsAdvice;
import edu.kit.hci.soli.domain.*;
import edu.kit.hci.soli.dto.LayoutParams;
import edu.kit.hci.soli.repository.BookingsRepository;
import edu.kit.hci.soli.repository.RoomRepository;
import edu.kit.hci.soli.repository.UserRepository;
import edu.kit.hci.soli.service.BookingsService;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.service.TimeService;
import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestService {
    @Autowired private UserRepository userRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private BookingsRepository bookingsRepository;
    @Autowired private UserService userService;
    @Autowired private RoomService roomService;
    @Autowired private TimeService timeService;
    @Autowired private LayoutParamsAdvice layoutParamsAdvice;

    public User user;
    public User user2;
    public User user3;
    public User anon;
    public Room room;

    private LocalDateTime currentSlot;

    public void reset() {
        bookingsRepository.deleteAll(bookingsRepository.findAll());
        userRepository.deleteAll(userRepository.findAll());
        roomRepository.deleteAll(roomRepository.findAll());

        user = userService.resolveAdminUser();
        anon = userService.resolveAnonUser();
        user2 = userService.createGuestUser("testuser2");
        user3 = userService.createGuestUser("testuser3");
        room = roomService.save(new Room(null, "Testraum", "Lorem ipsum odor amet, consectetuer adipiscing elit. Nisi convallis rutrum aenean, dolor quis ut.", "Testort"));

        currentSlot = timeService.currentSlot();
    }

    public Booking createBooking(User user) {
        return createBooking(user, currentSlot.plusDays(1));
    }

    public Booking createBooking(User user, LocalDateTime start) {
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStartDate(start);
        booking.setEndDate(start.plusDays(1));
        booking.setPriority(Priority.HIGHEST);
        booking.setShareRoomType(ShareRoomType.ON_REQUEST);
        return booking;
    }

    public LayoutParams paramsFor(User user, HttpServletRequest request) {
        return layoutParamsAdvice.getLayoutParams(layoutParamsAdvice.getLoginStateModel(() -> user, null), request);
    }
}
