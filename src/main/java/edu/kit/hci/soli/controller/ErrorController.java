package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.dto.KnownError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.*;

/**
 * Controller for handling errors in the application.
 */
@Controller
public class ErrorController extends AbstractErrorController {
    /**
     * Constructs an ErrorController with the specified {@link DefaultErrorAttributes}.
     *
     * @param errorAttributes the default error attributes
     */
    public ErrorController(DefaultErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    /**
     * Handles errors and returns the appropriate error view.
     *
     * @param model the model to be used in the view
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
            return "error_known";
        }

        model.addAttribute("timestamp", errorAttributes.get("timestamp"));
        model.addAttribute("status", errorAttributes.get("status"));
        model.addAttribute("error", errorAttributes.get("error"));
        model.addAttribute("message", errorAttributes.get("message"));
        model.addAttribute("path", errorAttributes.get("path"));
        return "error";
    }
}
