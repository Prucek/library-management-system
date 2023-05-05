package cz.muni.fi.pa165.seminar3.librarymanagement;

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
    public static final String SECURITY_SCHEME_OAUTH2 = "OAuth2";
    public static final String SECURITY_SCHEME_BEARER = "Bearer";
    public static  final String USER_SCOPE = "test_1";
    public static  final String LIBRARIAN_SCOPE = "test_2";


    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }

    /**
     * Configure access restrictions to the API.
     * Introspection of opaque access token is configured, introspection endpoint is defined in application.yml.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(x -> x
                        .requestMatchers(HttpMethod.GET, "/books/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/books/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/books/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/books/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)

                        .requestMatchers(HttpMethod.GET, "/authors/**").hasAuthority("SCOPE_" + USER_SCOPE)
                        .requestMatchers(HttpMethod.POST, "/authors/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.PUT, "/authors/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .requestMatchers(HttpMethod.DELETE, "/authors/**").hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken)
        ;
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
                                                            .addString("test_1", "User")
                                                            .addString("test_2", "Librarian")
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
