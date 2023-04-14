package cz.muni.fi.pa165.seminar3.reporting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Config class which specifies url of client required for report service.
 *
 * @author Marek Miček
 */
@Configuration
public class WebClientConfig {

    @Value("${library-management.url}")
    private String libraryManagementUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.create(libraryManagementUrl);
    }
}
