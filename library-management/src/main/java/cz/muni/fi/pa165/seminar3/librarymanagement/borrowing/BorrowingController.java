package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import static cz.muni.fi.pa165.seminar3.librarymanagement.Config.DEFAULT_PAGE_SIZE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.SECURITY_SCHEME_OAUTH2;
import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.USER_SCOPE;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
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
    @Operation(summary = "Returns identified borrowing", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "Borrowing found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Borrowing not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_1",
            content = @Content())
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
    @Operation(summary = "Create a new borrowing", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "Borrowing created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "User or book not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
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
            """, security = {
                @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
                @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
            }
    )
    @ApiResponse(responseCode = "200", description = "Borrowing updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Borrowing not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @PutMapping("/{id}")
    public BorrowingDto update(@PathVariable String id, @RequestBody BorrowingCreateDto borrowingCreateDto) {
        return borrowingFacade.updateBorrowing(id, borrowingCreateDto);
    }

    /**
     * REST method for deleting borrowing.
     *
     * @param id Specifies borrowing to be deleted
     */
    @Operation(summary = "Delete existing borrowing", description = "Enables deleting of existing borrowing. ",
            security = {
                @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
                @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
            }
    )
    @ApiResponse(responseCode = "200", description = "Borrowing deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Borrowing not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
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
     * @param page     page number
     * @param pageSize size of the page
     * @return Result object with all borrowings
     */
    @Operation(summary = "Get all borrowings", description = "Returns all borrowings", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "Pages list of all borrowings", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_1",
            content = @Content())
    @GetMapping
    public Result<BorrowingDto> findAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                        @RequestParam(defaultValue = "") String bookInstanceId) {
        if (bookInstanceId != null && !bookInstanceId.isBlank()) {
            return Result.of(borrowingFacade.findPending(bookInstanceId));
        }
        return borrowingFacade.findAll(page, pageSize);
    }
}
