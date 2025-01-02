package edu.kit.hci.soli.test;

import edu.kit.hci.soli.config.SoliConfiguration;
import edu.kit.hci.soli.config.template.JteContext;
import edu.kit.hci.soli.domain.User;
import edu.kit.hci.soli.service.EmailService;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Profile("test")
public class TestEmailServiceImpl implements EmailService {
    private final TemplateEngine templateEngine;
    private final String hostname;
    private final MessageSource messageSource;

    public TestEmailServiceImpl(TemplateEngine templateEngine, MessageSource messageSource, SoliConfiguration soliConfiguration) {
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
        this.hostname = soliConfiguration.getHostname();
    }

    @Override
    public void sendMail(User to, String subject, String template, Map<String, Object> model) {
        if (to.getEmail() == null) {
            log.warn("User {} has no email address, not sending email", to.getUserId());
            return;
        }

        JteContext context = new JteContext(messageSource, hostname, to.getLocale());
        model = new HashMap<>(model);
        model.put("context", context);

        StringOutput stringOutput = new StringOutput();
        templateEngine.render(template + ".jte", model, stringOutput);

        log.info("Would have sent email to {} with subject '{}':\n{}", to.getEmail(), subject, stringOutput);
    }
}
