package edu.kit.hci.soli.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final SoliOidcUserService userService;

    public WebSecurityConfig(SoliOidcUserService userService) {
        this.userService = userService;
    }

    // https://www.baeldung.com/spring-security-openid-connect
    // https://spring.io/guides/gs/securing-web
    // https://docs.spring.io/spring-security/reference/servlet/oauth2/login/index.html
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(cfg -> cfg
                        .requestMatchers("/", "/{id:\\d+}", "/api/{id:\\d+}/events", "/login/guest").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(cfg -> cfg
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                        .userInfoEndpoint(userService::configure)
                )
                .formLogin(cfg -> cfg
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }
}
