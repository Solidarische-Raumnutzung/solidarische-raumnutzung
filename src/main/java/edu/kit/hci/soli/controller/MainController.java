package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.dto.KnownError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.*;
import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.PATH;

/**
 * Main controller for miscellaneous status requests.
 */
@Controller("/misc")
@Slf4j
public class MainController extends AbstractErrorController {
    /**
     * Constructs an MainController with the specified {@link DefaultErrorAttributes}.
     *
     * @param errorAttributes the default error attributes
     */
    public MainController(DefaultErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    /**
     * Handles errors and returns the appropriate error view.
     *
     * @param model   the model to be used in the view
     * @param request the HTTP request
     * @return the view name
     */
    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.of(
                STATUS,
                ERROR,
                MESSAGE,
                PATH
        ));
        if (errorAttributes.get("status").equals(404)) {
            model.addAttribute("error", KnownError.NOT_FOUND);
            return "error/known";
        }

        model.addAttribute("timestamp", errorAttributes.get("timestamp"));
        model.addAttribute("status", errorAttributes.get("status"));
        model.addAttribute("error", errorAttributes.get("error"));
        model.addAttribute("message", errorAttributes.get("message"));
        model.addAttribute("path", errorAttributes.get("path"));
        return "error/unknown";
    }

    /**
     * Returns the view for disabled users.
     *
     * @param model the model to be used in the view
     * @return the view name
     */
    @RequestMapping("/disabled")
    public String getDisabled(Model model) {
        return "error/disabled_user";
    }
}
