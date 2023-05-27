package cz.muni.fi.pa165.seminar3.librarymanagement;

import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_OAUTH2;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.USER_SCOPE;

import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Main class for Spring Boot application.
 */
@SpringBootApplication
public class LibraryManagementApplication {

    private static final Logger log = LoggerFactory.getLogger(LibraryManagementApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }

    /**
     * Configure access restrictions to the API.
     * Introspection of opaque access token is configured, introspection endpoint is defined in application.yml.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // So unauthorized POST requests can be made to /users
        http.csrf().disable();
        http
                .authorizeHttpRequests(x -> x

                        // HttpMethod.GET, "/books/**" unregistered user can see books
                        .requestMatchers(HttpMethod.POST, "/books/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/books/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/books/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)

                        // HttpMethod.GET, "/authors/**" unregistered user can see authors
                        .requestMatchers(HttpMethod.POST, "/authors/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/authors/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/authors/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)

                        .requestMatchers(HttpMethod.GET, "/borrowings/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/borrowings/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/borrowings/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/borrowings/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)

                        .requestMatchers(HttpMethod.GET, "/fines").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/fines/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/fines/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/fines/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/fines/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)

                        .requestMatchers(HttpMethod.GET, "/payments").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/payments/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/payments/**").hasAuthority("SCOPE_" + USER_SCOPE)

                        .requestMatchers(HttpMethod.GET, "/reservations/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/reservations/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/reservations/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/reservations/**").hasAuthority("SCOPE_" + USER_SCOPE)

                        // HttpMethod.GET, "/settings/**" unregistered user can see prices
                        .requestMatchers(HttpMethod.POST, "/settings/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)

                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        // HttpMethod.POST, "/users" unregistered user can register
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken)
        ;
        http.headers().frameOptions().disable();
        return http.build();
    }

    /**
     * Add security definitions to generated openapi.yaml.
     */
    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            log.info("adding security to OpenAPI description");
            openApi.getComponents()
                    .addSecuritySchemes(SECURITY_SCHEME_BEARER,
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .description("provide a valid access token")
                    )
                    .addSecuritySchemes(SECURITY_SCHEME_OAUTH2,
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.OAUTH2)
                                    .description("get access token with Authorization Code Grant")
                                    .flows(new OAuthFlows()
                                            .authorizationCode(new OAuthFlow()
                                                    .authorizationUrl("https://oidc.muni.cz/oidc/authorize")
                                                    .tokenUrl("https://oidc.muni.cz/oidc/token")
                                                    .scopes(new Scopes()
                                                            .addString(USER_SCOPE, "User")
                                                            .addString(LIBRARIAN_SCOPE, "Librarian")
                                                    )
                                            )
                                    )
                    );
        };
    }

    /**
     * Display a hint in the log.
     */
    @EventListener
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        log.info("**************************");
        int port = event.getWebServer().getPort();
        log.info("visit http://localhost:{}/swagger-ui.html for UI", port);
        log.info("visit http://localhost:{}/openapi.yaml for OpenAPI document", port);
        log.info("**************************");
    }

}
