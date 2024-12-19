package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.controller.MainController;
import edu.kit.hci.soli.test.TestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = {"dev", "test"})
public class MainControllerTest {
    @Autowired private MainController mainController;
    @Autowired private TestService testService;

    @BeforeAll
    public static void clean(@Autowired TestService testService) {
        testService.reset();
    }

    @AfterEach
    public void tearDown() {
        testService.reset();
    }

    @Test
    public void testGetDisabledPage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String result = mainController.getDisabled(model);
        assertEquals("disabled_user", result);
    }
}
