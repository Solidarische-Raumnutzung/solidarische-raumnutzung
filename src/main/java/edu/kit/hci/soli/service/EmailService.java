package edu.kit.hci.soli.service;

import edu.kit.hci.soli.domain.User;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface EmailService {
    void sendMail(User to, @PropertyKey(resourceBundle = "messages") String subject, String template, Map<String, Object> model);
}
