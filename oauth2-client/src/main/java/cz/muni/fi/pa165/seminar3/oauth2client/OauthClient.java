package cz.muni.fi.pa165.seminar3.oauth2client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


/**
 * OauthClient is a Spring Boot application that uses Spring Security to authenticate users.
 */
@SpringBootApplication
public class OauthClient {

    private static final Logger log = LoggerFactory.getLogger(OauthClient.class);

    public static void main(String[] args) {
        SpringApplication.run(OauthClient.class, args);
    }

    /**
     * Configuration of Spring Security. Sets up OAuth2 authentication
     * for all URLS except a list of public ones.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(x -> x
                        .anyRequest().authenticated()
                )
                .oauth2Login();
        return httpSecurity.build();
    }

    /**
     * Display a hint in the log.
     */
    @EventListener
    public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
        log.info("**************************");
        log.info("visit http://localhost:{}/", event.getWebServer().getPort());
        log.info("**************************");
    }
}