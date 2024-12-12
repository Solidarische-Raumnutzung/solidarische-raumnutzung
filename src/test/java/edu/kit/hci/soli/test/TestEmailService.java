package edu.kit.hci.soli.test;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.service.EmailService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Profile("test")
public class TestEmailService implements EmailService {
    @Override
    public void sendMail(User to, String subject, String template, Map<String, Object> model) {
    }
}
