package edu.kit.hci.soli.test.controller;

import edu.kit.hci.soli.controller.OpeningHoursController;
import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.dto.KnownError;
import edu.kit.hci.soli.dto.LayoutParams;
import edu.kit.hci.soli.dto.form.SaveOpeningHoursForm;
import edu.kit.hci.soli.service.RoomService;
import edu.kit.hci.soli.service.TimeService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpeningHoursControllerTest {

    private RoomService roomService;
    private Model model;
    private HttpServletResponse response;
    private LayoutParams layoutParams;
    private OpeningHoursController openingHoursController;

    @BeforeEach
    void setUp() {
        roomService = mock(RoomService.class);
        TimeService timeService = mock(TimeService.class);
        model = mock(Model.class);
        response = mock(HttpServletResponse.class);
        layoutParams = mock(LayoutParams.class);
        openingHoursController = new OpeningHoursController(roomService);
    }

    private SaveOpeningHoursForm createValidForm() {
        return new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Monday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Tuesday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Wednesday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Thursday
                LocalTime.of(9, 0), LocalTime.of(17, 0)  // Friday
        );
    }

    @Test
    void testSaveOpeningHours_InvalidTime_ReturnsInvalidTimeError() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(10, 0), LocalTime.of(9, 0), // Monday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Tuesday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Wednesday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Thursday
                LocalTime.of(9, 0), LocalTime.of(17, 0)  // Friday
        );
        when(roomService.getOptional(1L)).thenReturn(Optional.of(new Room()));

        String view = openingHoursController.saveOpeningHours(1L, model, layoutParams, response, form);

        assertEquals("error/known", view);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(model).addAttribute("error", KnownError.INVALID_TIME);
    }

    @Test
    void testSaveOpeningHours_RoomNotFound_ReturnsNotFoundError() {
        SaveOpeningHoursForm form = createValidForm();
        when(roomService.getOptional(1L)).thenReturn(Optional.empty());

        String view = openingHoursController.saveOpeningHours(1L, model, layoutParams, response, form);

        assertEquals("error/known", view);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(model).addAttribute("error", KnownError.NOT_FOUND);
    }

    @Test
    void testSaveOpeningHours_ValidTime_SavesOpeningHours() {
        SaveOpeningHoursForm form = createValidForm();
        Room room = new Room();
        when(roomService.getOptional(1L)).thenReturn(Optional.of(room));

        String view = openingHoursController.saveOpeningHours(1L, model, layoutParams, response, form);

        assertEquals("redirect:/1", view);
        verify(roomService).save(room);
    }

    @Test
    void testSaveOpeningHours_TuesdayStartAfterEnd_ReturnsInvalidTimeError() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Monday
                LocalTime.of(10, 0), LocalTime.of(9, 0), // Tuesday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Wednesday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Thursday
                LocalTime.of(9, 0), LocalTime.of(17, 0)  // Friday
        );
        when(roomService.getOptional(1L)).thenReturn(Optional.of(new Room()));

        String view = openingHoursController.saveOpeningHours(1L, model, layoutParams, response, form);

        assertEquals("error/known", view);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(model).addAttribute("error", KnownError.INVALID_TIME);
    }

    @Test
    void testSaveOpeningHours_WednesdayStartAfterEnd_ReturnsInvalidTimeError() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Monday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Tuesday
                LocalTime.of(10, 0), LocalTime.of(9, 0), // Wednesday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Thursday
                LocalTime.of(9, 0), LocalTime.of(17, 0)  // Friday
        );
        when(roomService.getOptional(1L)).thenReturn(Optional.of(new Room()));

        String view = openingHoursController.saveOpeningHours(1L, model, layoutParams, response, form);

        assertEquals("error/known", view);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(model).addAttribute("error", KnownError.INVALID_TIME);
    }

    @Test
    void testSaveOpeningHours_ThursdayStartAfterEnd_ReturnsInvalidTimeError() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Monday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Tuesday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Wednesday
                LocalTime.of(10, 0), LocalTime.of(9, 0), // Thursday
                LocalTime.of(9, 0), LocalTime.of(17, 0)  // Friday
        );
        when(roomService.getOptional(1L)).thenReturn(Optional.of(new Room()));

        String view = openingHoursController.saveOpeningHours(1L, model, layoutParams, response, form);

        assertEquals("error/known", view);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(model).addAttribute("error", KnownError.INVALID_TIME);
    }

    @Test
    void testSaveOpeningHours_FridayStartAfterEnd_ReturnsInvalidTimeError() {
        SaveOpeningHoursForm form = new SaveOpeningHoursForm(
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Monday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Tuesday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Wednesday
                LocalTime.of(9, 0), LocalTime.of(17, 0), // Thursday
                LocalTime.of(10, 0), LocalTime.of(9, 0)  // Friday
        );
        when(roomService.getOptional(1L)).thenReturn(Optional.of(new Room()));

        String view = openingHoursController.saveOpeningHours(1L, model, layoutParams, response, form);

        assertEquals("error/known", view);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(model).addAttribute("error", KnownError.INVALID_TIME);
    }

    @Test
    void testShowOpeningHoursForm_RoomNotFound_ReturnsNotFoundError() {
        when(roomService.getOptional(1L)).thenReturn(Optional.empty());

        String view = openingHoursController.showOpeningHoursForm(1L, model, response, layoutParams);

        assertEquals("error/known", view);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(model).addAttribute("error", KnownError.NOT_FOUND);
    }

    @Test
    void testShowOpeningHoursForm_RoomFound_ReturnsFormView() {
        Room room = new Room();
        when(roomService.getOptional(1L)).thenReturn(Optional.of(room));

        String view = openingHoursController.showOpeningHoursForm(1L, model, response, layoutParams);

        assertEquals("admin/opening-hours", view);
        verify(layoutParams).setRoom(room);
    }
}