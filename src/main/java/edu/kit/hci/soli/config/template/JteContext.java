package edu.kit.hci.soli.config.template;

import gg.jte.Content;
import gg.jte.support.LocalizationSupport;
import lombok.Getter;
import org.springframework.context.MessageSource;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.Locale;

public class JteContext implements LocalizationSupport {
    private final MessageSource messageSource;
    @Getter private final String hostname;
    @Getter private final Locale locale;
    private final DateFormatSymbols symbols;

    public JteContext(MessageSource messageSource, String hostname, Locale locale) {
        this.messageSource = messageSource;
        this.hostname = hostname;
        this.locale = locale;
        this.symbols = new DateFormatSymbols(locale);
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

    public String format(DayOfWeek day) {
        return symbols.getWeekdays()[day == DayOfWeek.SUNDAY ? 1 : day.getValue() + 1];
    }

    public String format(Month month) {
        return symbols.getMonths()[month.getValue() - 1];
    }
}
