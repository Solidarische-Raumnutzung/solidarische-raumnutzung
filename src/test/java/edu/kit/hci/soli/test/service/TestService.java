package edu.kit.hci.soli.test.service;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.*;
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

    public void reset() {
        bookingsRepository.deleteAll(bookingsRepository.findAll());
//        roomRepository.deleteAll(roomRepository.findAll());
        userRepository.deleteAll(userRepository.findAll());

        user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        userService.create(user);
    }
}
