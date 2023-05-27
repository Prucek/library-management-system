package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import static cz.muni.fi.pa165.seminar3.librarymanagement.Config.DEFAULT_PAGE_SIZE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_OAUTH2;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.USER_SCOPE;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring REST Controller for reservation service.
 *
 * @author Marek Miček
 */
@RestController
@RequestMapping(path = "/reservations")
@ApiResponse(responseCode = "401", description = "Authentication is required")
@ApiResponse(responseCode = "403", description = "Access token does not have required scope")
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @Autowired
    public ReservationController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    /**
     * REST method returning reservation with specified id.
     *
     * @param id Specifies reservation which is requested
     * @return Concrete reservation specified by its id
     */
    @Operation(summary = "Returns identified reservation")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "200", description = "Reservation found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "reservation not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/{id}")
    public ReservationDto find(@PathVariable String id) {
        return reservationFacade.find(id);
    }

    /**
     * REST method for creating a new borrowing.
     *
     * @param reservationCreateDto Reservation to be posted and created
     * @return Newly created reservation as a response for calling REST create method
     */
    @Operation(summary = "Create a new reservation")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "201", description = "Reservation created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "User or book not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto create(@RequestBody @Valid ReservationCreateDto reservationCreateDto) {
        return reservationFacade.create(reservationCreateDto);
    }

    /**
     * REST method for updating reservation.
     *
     * @param id Specifies reservation to be updated
     * @return Updated reservation as a response for calling REST update method
     */
    @Operation(summary = "Update existing reservation")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "200", description = "Reservation updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Reservation not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PutMapping("/{id}")
    public ReservationDto update(@PathVariable String id,
                                 @RequestBody @Valid ReservationCreateDto reservationCreateDto) {
        return reservationFacade.updateReservation(id, reservationCreateDto);
    }

    /**
     * REST method for deleting reservation.
     *
     * @param id Specifies reservation to be deleted
     */
    @Operation(summary = "Delete existing reservation")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "204", description = "Reservation deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Reservation not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        reservationFacade.deleteReservation(id);
    }

    /**
     * REST method returning all reservations.
     *
     * @param page     page number
     * @param pageSize size of the page
     * @return Result object with all reservations
     */
    @Operation(summary = "Get all reservations")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "200", description = "Pages list of all reservations", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<ReservationDto> findAll(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return reservationFacade.findAll(page, pageSize);
    }
}
