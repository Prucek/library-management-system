package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Spring REST Controller for reservation service.
 * @author Marek Miček
 */
@RestController
@OpenAPIDefinition(
        info = @Info(title = "RestAPI controller for reservations in library management system",
                version = "1.1",
                description = """
                        The API has operations for:
                        - getting all reservations
                        - creating a new reservation
                        - deleting a specific reservation by its id
                        - getting a specific reservation by its id
                        - updating a specific reservation by its id
                        """,
                contact = @Contact(name = "Marek Miček", email = "540461@mail.muni.cz", url = "https://is.muni.cz/auth/osoba/540461")
        )
)
@RequestMapping(path = "/reservations")
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @Autowired
    public ReservationController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    /**
     * REST method returning reservation with specified id.
     * @param id Specifies reservation which is requested
     * @return Concrete reservation specified by its id
     */
    @Operation(
            summary = "Returns identified reservation",
            description = "Looks up a reservation by its id.",
            responses = {
                    @ApiResponse(responseCode = "200",  description = "Reservation found", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "404", description = "reservation not found",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public ReservationDto find(@PathVariable String id) {
        try {
            return reservationFacade.find(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "reservation with id=" + id + " not found");
        }
    }

    /**
     * REST method for creating a new borrowing.
     * @param reservationCreateDto Reservation to be posted and created
     * @return Newly created reservation as a response for calling REST create method
     */
    @Operation(
            summary = "Create a new reservation",
            description = """
                    Receives data in request body and stores it as a new message.
                    Returns the new reservation as its response.
                    """ )
    @ApiResponse(responseCode = "200", description = "Reservation created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "User or book not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    public ReservationDto create(@RequestBody ReservationCreateDto reservationCreateDto) {
        return reservationFacade.create(reservationCreateDto);
    }

    /**
     * REST method for updating reservation.
     * @param id Specifies reservation to be updated
     * @return Updated reservation as a response for calling REST update method
     */
    @Operation(
            summary = "Update existing reservation",
            description = """
                    Provides update of existing reservation.
                    Returns updated reservation as its response.
                    """ )
    @ApiResponse(responseCode = "200", description = "Reservation updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Reservation not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PutMapping("/{id}")
    public ReservationDto update(@PathVariable String id,
                                 @RequestBody ReservationCreateDto reservationCreateDto) {
        return reservationFacade.updateReservation(id, reservationCreateDto);
    }

    /**
    * REST method for deleting reservation.
    * @param id Specifies reservation to be deleted
     */
    @Operation(
            summary = "Delete existing reservation",
            description = """
                    Enables deleting of existing reservation.
                    """ )
    @ApiResponse(responseCode = "200", description = "Reservation deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Reservation not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        try {
            reservationFacade.deleteReservation(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * REST method returning all reservations.
     * @param pageable Represents Page object of reservation which will be used for return value
     * @return Result object with all reservations
     */
    @Operation(
            summary = "Get all reservations",
            description = """
                    Returns all reservations
                    """)
    @ApiResponse(responseCode = "200", description = "Pages list of all reservations", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<ReservationDto> findAll(Pageable pageable) {
        return reservationFacade.findAll(pageable);
    }
}
