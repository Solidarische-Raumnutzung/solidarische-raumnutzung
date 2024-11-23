package edu.kit.hci.soli.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties("soli")
public class SoliConfiguration {
    private boolean developmentMode = false;
}
