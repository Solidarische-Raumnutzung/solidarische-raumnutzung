package edu.kit.hci.soli.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;

@Data @NoArgsConstructor
@Configuration
@ConfigurationProperties("soli")
public class SoliConfiguration {
    private boolean developmentMode = false;
    private String hostname;
    private Administrator administrator = new Administrator();
    private Guest guest = new Guest();
    private Pagination pagination = new Pagination();

    @Data @NoArgsConstructor
    public static class Administrator {
        private String password;
    }

    @Data @NoArgsConstructor
    public static class Guest {
        private String marker;
    }

    @Data @NoArgsConstructor
    public static class Pagination {
        private int maxSize;
    }
}
