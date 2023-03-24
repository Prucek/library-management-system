package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

/**
 * Spring REST Controller for borrowing service.
 * @author Marek Miček
 */
@RestController
@OpenAPIDefinition(
        info = @Info(title = "RestAPI controller for borrowings in library management system",
                version = "1.1",
                description = """
                        The API has operations for:
                        - getting all borrowings
                        - creating a new borrowings
                        - deleting a specific borrowing by its id
                        - getting a specific borrowing by its id
                        - updating a specific borrowing by its id
                        """,
                contact = @Contact(name = "Marek Miček", email = "540461@mail.muni.cz", url = "https://is.muni.cz/auth/osoba/540461")
        )
)
@RequestMapping("/borrowings")
public class BorrowingController {

    private final BorrowingService service;

    private final BorrowingMapper mapper;

    @Autowired
    public BorrowingController(BorrowingService service, BorrowingMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * REST method returning borrowing with specified id.
     * @param id Specifies borrowing which is requested
     * @return Concrete borrowing specified by its id
     */
    @Operation(
            summary = "Returns identified borrowing",
            description = "Looks up a borrowing by its id.",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "#/components/responses/SingleBorrowingResponse"),
                    @ApiResponse(responseCode = "404", description = "borrowing not found",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public BorrowingDto find(@PathVariable String id) {
        Borrowing borrowing;
        try {
            borrowing = service.find(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "borrowing with id=" + id + " not found");
        }
        return mapper.toDto(borrowing);
    }

    /**
     * REST method for creating a new borrowing.
     * @param borrowing Borrowing to be posted and created
     * @return Newly created borrowing as a response for calling REST create method
     */
    @Operation(
            summary = "Create a new borrowing",
            description = """
                    Receives data in request body and stores it as a new message.
                    Returns the new borrowing as its response.
                    """,
            responses = {
                    @ApiResponse(responseCode = "201", ref = "#/components/responses/SingleBorrowingResponse"),
                    @ApiResponse(responseCode = "400", description = "input data were not correct",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @PostMapping
    public BorrowingDto create(@RequestBody BorrowingDto borrowing) {
        return mapper.toDto(service.create(mapper.fromDto(borrowing)));
    }

    /**
     * REST method for updating borrowing.
     * @param id Specifies borrowing to be updated
     * @return Updated borrowing as a response for calling REST update method
     */
    @Operation(
            summary = "Update existing borrowing",
            description = """
                    Provides update of existing borrowing".
                    Returns updated borrowing" as its response.
                    """,
            responses = {
                    @ApiResponse(responseCode = "201", ref = "#/components/responses/SingleReservationResponse"),
                    @ApiResponse(responseCode = "400", description = "input data were not correct",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @PutMapping("/{id}")
    public BorrowingDto update(@PathVariable String id) {
        return mapper.toDto(service.create(service.find(id)));
    }

    /**
     * REST method for deleting borrowing.
     * @param id Specifies borrowing to be deleted
     */
    @Operation(
            summary = "Delete existing borrowing",
            description = """
                    Enables deleting of existing borrowing.
                    """,
            responses = {
                    @ApiResponse(responseCode = "201", ref = "#/components/responses/SingleReservationResponse"),
                    @ApiResponse(responseCode = "400", description = "input data were not correct",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(service.find(id));
    }

    /**
     * REST method returning all borrowings.
     * @param pageable Represents Page object of borrowing which will be used for return value
     * @return Result object with all borrowings
     */
    @Operation(
            summary = "Get all borrowings",
            description = """
                    Returns all borrowings
                    """)
    @GetMapping
    public Result<BorrowingDto> findAll(Pageable pageable) {
        return mapper.toResult(service.findAll(pageable));
    }
}
