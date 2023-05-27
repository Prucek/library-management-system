package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings.SettingsDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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

    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(libraryManagementUrl)
                .filter(new ServletBearerExchangeFilterFunction())
                .build();
    }

    /**
     * Creates a new borrowing.
     *
     * @param createDto create payload
     * @return created borrowing
     */
    public BorrowingDto createBorrowing(BorrowingCreateDto createDto) {
        return getWebClient().post()
                .uri(uriBuilder -> uriBuilder.path("/borrowings").build())
                .bodyValue(createDto)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BorrowingDto.class)
                .block();
    }

    /**
     * Fetches current settings.
     *
     * @return current settings
     */
    public SettingsDto getSettings() {
        return getWebClient().get()
                .uri(uriBuilder -> uriBuilder.path("/settings").build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SettingsDto.class)
                .block();
    }

    /**
     * Fetches pending borrowing for a book instance.
     *
     * @param bookInstanceId id of the book instance
     * @return pending borrowing dto
     */
    public BorrowingDto getPendingBorrowing(String bookInstanceId) {
        return getWebClient().get()
                .uri(uriBuilder -> uriBuilder.path("/borrowings/").queryParam("bookInstanceId", bookInstanceId).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BorrowingDto.class)
                .block();
    }

    /**
     * Updates a borrowing.
     *
     * @param id  id of the borrowing
     * @param dto borrowing data
     * @return updated borrowing
     */
    public BorrowingDto updateBorrowing(String id, BorrowingCreateDto dto) {
        return getWebClient().put()
                .uri(uriBuilder -> uriBuilder.path("/borrowings/{id}").build(id))
                .bodyValue(dto)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BorrowingDto.class)
                .block();
    }
}
