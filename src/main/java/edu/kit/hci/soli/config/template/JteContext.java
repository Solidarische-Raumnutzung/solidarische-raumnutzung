package edu.kit.hci.soli.config.template;

import gg.jte.Content;
import gg.jte.support.LocalizationSupport;
import lombok.Getter;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class JteContext implements LocalizationSupport {
    private final MessageSource messageSource;
    @Getter private final String hostname;
    @Getter private final Locale locale;

    public JteContext(MessageSource messageSource, String hostname, Locale locale) {
        this.messageSource = messageSource;
        this.hostname = hostname;
        this.locale = locale;
    }

    @Override
    public String lookup(String key) {
        return messageSource.getMessage(key, null, locale);
    }

    @Override
    public Content localize(String key, Object... params) {
        String result = messageSource.getMessage(key, params, locale);
        return output -> output.writeUserContent(result);
    }

    public Content empty() {
        return output -> {};
    }
}
