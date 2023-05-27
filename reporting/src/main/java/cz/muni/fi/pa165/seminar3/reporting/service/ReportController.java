package cz.muni.fi.pa165.seminar3.reporting.service;

import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.USER_SCOPE;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting.BookReportDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting.FinanceReportDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting.UserReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Spring REST Controller for report service.
 *
 * @author Marek Miček
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService service;

    @Autowired
    public ReportController(ReportService service) {
        this.service = service;
    }

    /**
     * REST method for getting finance report of specific user.
     *
     * @param userId Specifies user for whom the report is generated
     * @return ReportDto with finance report info
     */
    @Operation(summary = "Generates finance report of specific user", description = """
            Receives id of user for whom report is generated in request body.
            Calls report service to obtain finance info and returns the DTO object with requested info.
            """)
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "input data were not correct",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE, USER_SCOPE})
    @GetMapping("/finances/{userId}")
    public FinanceReportDto generateFinanceReport(@PathVariable String userId) {
        return service.generateFinanceReport(userId);
    }

    /**
     * REST method for getting user report.
     *
     * @return ReportDto with number of all users in system
     */
    @Operation(summary = "Generates user report", description = """
            Calls report service to obtain number of users and returns the DTO object with requested info.
            """)
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "input data were not correct",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE, USER_SCOPE})
    @GetMapping("/users")
    public UserReportDto generateUserReport() {
        return service.generateUserReport();
    }

    /**
     * REST method for getting book report of specific user.
     *
     * @param userId Specifies user for whom the report is generated
     * @return ReportDto with book report info
     */
    @Operation(summary = "Generates book report of specific user", description = """
            Receives id of user for whom report is generated in request body.
            Calls report service to obtain book info and returns the DTO object with requested info.
            """)
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "input data were not correct",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE, USER_SCOPE})
    @GetMapping("/books/{userId}")
    public BookReportDto generateBookReport(@PathVariable String userId) {
        return service.generateBookReport(userId);
    }

    /**
     * Handles the web client exceptions.
     *
     * @param exception exception to handle
     */
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorMessage> handleWebClientException(WebClientResponseException exception) {
        return switch (HttpStatus.valueOf(exception.getStatusCode().value())) {
            case UNAUTHORIZED -> buildErrorResponse(HttpStatus.UNAUTHORIZED, "Missing bearer token");
            case FORBIDDEN -> buildErrorResponse(HttpStatus.FORBIDDEN, "Invalid bearer token");
            case NOT_FOUND -> buildErrorResponse(HttpStatus.NOT_FOUND, "ID not found");
            default -> buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        };
    }

    private static ResponseEntity<ErrorMessage> buildErrorResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(
                new ErrorMessage(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), status.value(),
                        status.getReasonPhrase(), message, ""), status);
    }
}
