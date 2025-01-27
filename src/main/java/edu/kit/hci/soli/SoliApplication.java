package edu.kit.hci.soli;

import edu.kit.hci.soli.config.SoliConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Main class for the SOLI application.
 * This class is responsible for bootstrapping the Spring Boot application.
 */
@EnableScheduling
@SpringBootApplication
public class SoliApplication {
    /**
     * The main method sets the default locale to English and runs the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(SoliApplication.class, args);
    }

    @Bean
    public InitializingBean initialize(SoliConfiguration config) {
        return () -> TimeZone.setDefault(config.getTimeZone());
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        source.setDefaultEncoding("UTF-8");
        source.setFallbackToSystemLocale(false);
        return source;
    }
}
