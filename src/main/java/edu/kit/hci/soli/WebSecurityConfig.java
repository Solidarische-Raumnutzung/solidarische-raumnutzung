package edu.kit.hci.soli;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    // https://www.baeldung.com/spring-security-openid-connect
    // https://spring.io/guides/gs/securing-web
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(requests ->
                requests
                    .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
            )
//                .oidcLogout()
            .formLogin(form -> form
//                    .loginPage("/login") //TODO implement login page
                    .permitAll());
        return http.build();
    }

    @Value("${soli.administrator.name}")
    private String username;

    @Value("${soli.administrator.password}")
    private String password;

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.builder()
                        .username(username)
                        .password(password)
                        .roles("USER", "ADMIN")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}
