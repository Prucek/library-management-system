package cz.muni.fi.pa165.seminar3.reporting.service;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Provides description of JSON returned for ResponseStatusException.
 *
 * @author Marek Miček
 */
@Schema(title = "error message",
        description = "response body for HTML statuses"
)
public record ErrorMessage(
        @Schema(format = "date-time", description = "time in ISO format", example = "2022-12-21T18:52:10.757+00:00")
        String timestamp,
        @Schema(description = "HTTP status code", example = "404")
        int status,
        @Schema(description = "HTTP status text", example = "Not Found")
        String error,
        @Schema(description = "reason for error", example = "entity not found")
        String message,
        @Schema(description = "URL path", example = "/report/users")
        String path) {
}