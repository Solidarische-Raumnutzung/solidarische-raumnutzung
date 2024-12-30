package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.controller.MainController;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.test.TestService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void testHandleError_NotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Model model = new ExtendedModelMap();

        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("status", 404);
        errorAttributes.put("error", "Not Found");
        errorAttributes.put("message", "No message available");
        errorAttributes.put("path", "/some-path");

        when(request.getAttribute("jakarta.servlet.error.status_code")).thenReturn(404);
        when(request.getAttribute("jakarta.servlet.error.message")).thenReturn("No message available");
        when(request.getAttribute("jakarta.servlet.error.request_uri")).thenReturn("/some-path");

        String view = mainController.handleError(model, request);

        assertEquals("error/known", view);
        assertEquals(KnownError.NOT_FOUND, model.getAttribute("error"));
    }

    @Test
    public void testHandleError_OtherError() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Model model = new ExtendedModelMap();

        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("status", 500);
        errorAttributes.put("error", "Internal Server Error");
        errorAttributes.put("message", "An unexpected error occurred");
        errorAttributes.put("path", "/some-path");

        when(request.getAttribute("jakarta.servlet.error.status_code")).thenReturn(500);
        when(request.getAttribute("jakarta.servlet.error.message")).thenReturn("An unexpected error occurred");
        when(request.getAttribute("jakarta.servlet.error.request_uri")).thenReturn("/some-path");
        when(request.getAttribute("jakarta.servlet.error.timestamp")).thenReturn("2023-10-10T10:10:10");

        String view = mainController.handleError(model, request);

        assertEquals("error", view);
        assertEquals(500, model.getAttribute("status"));
        assertEquals("Internal Server Error", model.getAttribute("error"));
        assertEquals("An unexpected error occurred", model.getAttribute("message"));
        assertEquals("/some-path", model.getAttribute("path"));
    }

    @Test
    public void testGetDisabledPage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String result = mainController.getDisabled(model);
        assertEquals("disabled_user", result);
    }
}
