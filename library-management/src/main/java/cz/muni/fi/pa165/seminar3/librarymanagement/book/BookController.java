package cz.muni.fi.pa165.seminar3.librarymanagement.book;


import static cz.muni.fi.pa165.seminar3.librarymanagement.Config.DEFAULT_PAGE_SIZE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_OAUTH2;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
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
 * Spring REST Controller for books.
 */
@RestController
@RequestMapping("/books")
@ApiResponse(responseCode = "401", description = "Authentication is required")
@ApiResponse(responseCode = "403", description = "Access token does not have required scope")
public class BookController {

    private final BookFacade facade;

    /**
     * Creates a BookController instance.
     *
     * @param facade FineFacade instance
     */
    @Autowired
    public BookController(BookFacade facade) {
        this.facade = facade;
    }

    /**
     * REST method returning book with specified id.
     */
    @Operation(summary = "Returns identified book")
    @ApiResponse(responseCode = "200", description = "Book found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Book not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/{id}")
    public BookDto find(@PathVariable String id) {
        return facade.find(id);
    }

    /**
     * REST method returning all books.
     */
    @Operation(summary = "Get all books")
    @ApiResponse(responseCode = "200", description = "Pages list of all books", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<BookDto> findAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return facade.findAll(page, pageSize);
    }

    /**
     * REST method for creating a new book.
     */
    @Operation(summary = "Create a new book")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "201", description = "Book created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Author not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestBody @Valid BookCreateDto dto) {
        return facade.create(dto);
    }

    /**
     * REST method deleting specific book.
     */
    @Operation(summary = "Delete book by its ID")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "204", description = "Book deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Book not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        facade.delete(id);
    }

    /**
     * REST method updating specific book.
     */
    @Operation(summary = "Update book by its ID")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @PutMapping("/{id}")
    public BookDto update(@PathVariable String id, @RequestBody @Valid BookCreateDto dto) {
        return facade.update(id, dto);
    }

    /**
     * REST method adding book instance.
     */
    @Operation(summary = "Add book instance")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "201", description = "Book instance added", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Book not found", useReturnTypeSchema = true)
    @PostMapping("/{bookId}/instances")
    @ResponseStatus(HttpStatus.CREATED)
    public BookInstanceDto addInstance(@PathVariable String bookId) {
        return facade.addInstance(bookId);
    }

    /**
     * REST method removing book instance.
     */
    @Operation(summary = "Remove book instance")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "204", description = "Book instance added", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Book or book instance not found", useReturnTypeSchema = true)
    @DeleteMapping("/instances/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeInstance(@PathVariable String id) {
        facade.removeInstance(id);
    }

    /**
     * REST method getting book instance.
     */
    @Operation(summary = "Returns identified book instance")
    @ApiResponse(responseCode = "200", description = "Book instance found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Book instance not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/instances/{id}")
    public BookInstanceDto getInstance(@PathVariable String id) {
        return facade.getInstance(id);
    }
}
