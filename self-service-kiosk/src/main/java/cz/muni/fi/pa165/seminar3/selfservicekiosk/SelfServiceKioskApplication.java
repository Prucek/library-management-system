package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Self-service kiosk application.
 */
@SpringBootApplication
public class SelfServiceKioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelfServiceKioskApplication.class, args);
    }

    /**
     * Configure access restrictions to the API.
     * Introspection of opaque access token is configured, introspection endpoint is defined in application.yml.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // So unauthorized POST requests can be made to /users
        http.csrf().disable();
        http.authorizeHttpRequests(x -> x.requestMatchers(HttpMethod.POST, "/kiosk/**")
                .hasAuthority("SCOPE_" + LIBRARIAN_SCOPE)
                .anyRequest()
                .permitAll()).oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken);
        http.headers().frameOptions().disable();
        return http.build();
    }

    /**
     * Add security definitions to generated openapi.yaml.
     */
    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> openApi.getComponents()
                .addSecuritySchemes("Bearer", new SecurityScheme().type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .description("provide a valid access token"));
    }
}
