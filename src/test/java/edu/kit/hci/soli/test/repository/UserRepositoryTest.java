package edu.kit.hci.soli.test.repository;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.repository.UserRepository;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class UserRepositoryTest {
    @Autowired private TestService testService;
    @Autowired private UserRepository userRepository;

    @BeforeAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @AfterEach
    public void tearDown() {
        testService.reset();
    }

    @Test
    public void testFindAllWithoutAdminOrAnon() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = userRepository.findAllWithoutAdminOrAnon(pageable);
        assertIterableEquals(List.of(testService.user2, testService.user3), userPage.getContent());
    }
}
