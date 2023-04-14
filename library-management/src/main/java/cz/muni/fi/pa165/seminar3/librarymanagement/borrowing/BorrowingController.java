package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


/**
 * Spring REST Controller for borrowing service.
 *
 * @author Marek Miček
 */
@RestController
@RequestMapping("/borrowings")
public class BorrowingController {

    private final BorrowingFacade borrowingFacade;

    @Autowired
    public BorrowingController(BorrowingFacade borrowingFacade) {
        this.borrowingFacade = borrowingFacade;
    }

    /**
     * REST method returning borrowing with specified id.
     *
     * @param id Specifies borrowing which is requested
     * @return Concrete borrowing specified by its id
     */
    @Operation(summary = "Returns identified borrowing")
    @ApiResponse(responseCode = "200", description = "Borrowing found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Borrowing not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/{id}")
    public BorrowingDto find(@PathVariable String id) {
        try {
            return borrowingFacade.find(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "borrowing with id=" + id + " not found");
        }
    }

    /**
     * REST method for creating a new borrowing.
     *
     * @param borrowingCreateDto Borrowing to be posted and created
     * @return Newly created borrowing as a response for calling REST create method
     */
    @Operation(summary = "Create a new borrowing")
    @ApiResponse(responseCode = "200", description = "Borrowing created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "User or book not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    public BorrowingDto create(@RequestBody BorrowingCreateDto borrowingCreateDto) {
        return borrowingFacade.create(borrowingCreateDto);
    }

    /**
     * REST method for updating borrowing.
     *
     * @param id Specifies borrowing to be updated
     * @return Updated borrowing as a response for calling REST update method
     */
    @Operation(summary = "Update existing borrowing", description = """
            Provides update of existing borrowing".
            Returns updated borrowing" as its response.
            """)
    @ApiResponse(responseCode = "200", description = "Borrowing updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Borrowing not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PutMapping("/{id}")
    public BorrowingDto update(@PathVariable String id,
                               @RequestBody BorrowingCreateDto borrowingCreateDto) {
        return borrowingFacade.updateBorrowing(id, borrowingCreateDto);
    }

    /**
     * REST method for deleting borrowing.
     *
     * @param id Specifies borrowing to be deleted
     */
    @Operation(summary = "Delete existing borrowing", description = """
            Enables deleting of existing borrowing.
            """)
    @ApiResponse(responseCode = "200", description = "Borrowing deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Borrowing not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        try {
            borrowingFacade.deleteBorrowing(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * REST method returning all borrowings.
     *
     * @param pageable Represents Page object of borrowing which will be used for return value
     * @return Result object with all borrowings
     */
    @Operation(summary = "Get all borrowings", description = """
            Returns all borrowings
            """)
    @ApiResponse(responseCode = "200", description = "Pages list of all borrowings", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<BorrowingDto> findAll(Pageable pageable) {
        return borrowingFacade.findAll(pageable);
    }
}
