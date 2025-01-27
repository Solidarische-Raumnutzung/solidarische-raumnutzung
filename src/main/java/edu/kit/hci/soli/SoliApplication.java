package edu.kit.hci.soli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Locale;

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
}
