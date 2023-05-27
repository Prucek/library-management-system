package cz.muni.fi.pa165.seminar3.reporting;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Class representing Library Management Microservice API client.
 *
 * @author Juraj Marcin
 */
@Service
public class LibraryManagementApi {

    @Value("${library-management.url}")
    @NotBlank
    private String libraryManagementUrl;

    @Bean
    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(libraryManagementUrl)
                .filter(new ServletBearerExchangeFilterFunction())
                .build();
    }

    /**
     * Fetches users.
     *
     * @param page page number
     * @return paged result of users
     */
    public Result<UserDto> getUsers(int page) {
        return getWebClient().get()
                .uri(uriBuilder -> uriBuilder.path("/users").queryParam("page", page).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Result<UserDto>>() {
                })
                .block();
    }

    /**
     * Returns user with id.
     *
     * @param id id of user
     * @return found user
     */
    public UserDto getUserDto(String id) {
        return getWebClient().get()
                .uri(uriBuilder -> uriBuilder.pathSegment("users", id).build())
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    /**
     * Fetches fines.
     *
     * @param page page number
     * @return paged result of fines
     */
    public Result<FineDto> getFines(int page) {
        return getWebClient().get()
                .uri(uriBuilder -> uriBuilder.path("/fines").queryParam("page", page).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Result<FineDto>>() {
                })
                .block();
    }

    /**
     * Fetches borrowings.
     *
     * @param page page number
     * @return paged result of borrowings
     */
    public Result<BorrowingDto> getBorrowings(int page) {
        return getWebClient().get()
                .uri(uriBuilder -> uriBuilder.path("/borrowings").queryParam("page", page).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Result<BorrowingDto>>() {
                })
                .block();
    }
}
