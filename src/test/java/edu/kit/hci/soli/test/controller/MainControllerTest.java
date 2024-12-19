package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.controller.MainController;
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

    @Test
    public void testGetDisabledPage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String result = mainController.getDisabled(model);
        assertEquals("disabled_user", result);
    }
}
