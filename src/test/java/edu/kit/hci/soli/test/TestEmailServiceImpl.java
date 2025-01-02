package edu.kit.hci.soli.test;

import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.service.EmailService;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@Profile("test")
public class TestEmailServiceImpl implements EmailService {
    private final TemplateEngine templateEngine;

    public TestEmailServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMail(User to, String subject, String template, Map<String, Object> model) {
        if (to.getEmail() == null) {
            log.warn("User {} has no email address, not sending email", to.getUserId());
            return;
        }

        StringOutput stringOutput = new StringOutput();
        templateEngine.render(template + ".jte", model, stringOutput);

        log.info("Would have sent email to {} with subject '{}':\n{}", to.getEmail(), subject, stringOutput);
    }
}
