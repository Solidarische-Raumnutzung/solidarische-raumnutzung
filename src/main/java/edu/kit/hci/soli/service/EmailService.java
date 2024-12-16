package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import org.jetbrains.annotations.PropertyKey;

import java.util.Map;

/**
 * Service for sending emails
 */
public interface EmailService {
    /**
     * Sends an email to the specified user.
     *
     * @param to       the user to send the email to
     * @param subject  the translation key of the subject
     * @param template the template to use
     * @param model    the model to use for the template
     */
    void sendMail(User to, @PropertyKey(resourceBundle = "messages") String subject, String template, Map<String, Object> model);
}
