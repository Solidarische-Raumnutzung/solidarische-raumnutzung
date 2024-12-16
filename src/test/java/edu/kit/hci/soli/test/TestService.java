package edu.kit.hci.soli.test;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.*;
import edu.kit.hci.soli.service.UserService;
import edu.kit.hci.soli.service.impl.UserServiceImpl;
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

    public void reset() {
        bookingsRepository.deleteAll(bookingsRepository.findAll());
//        roomRepository.deleteAll(roomRepository.findAll());
        userRepository.deleteAll(userRepository.findAll());

        user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user = userService.create(user);

        user2 = new User();
        user2.setUsername("testuser2");
        user2.setEmail("testuser2@example.com");
        user2 = userService.create(user2);

        user3 = new User();
        user3.setUsername("testuser3");
        user3.setEmail("testuser3@example.com");
        user3 = userService.create(user3);
    }
}
