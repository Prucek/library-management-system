package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import static cz.muni.fi.pa165.seminar3.librarymanagement.Config.DEFAULT_PAGE_SIZE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_OAUTH2;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
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
 * Spring REST Controller for Authors.
 *
 * @author MarekFiala
 */
@RestController
@RequestMapping("/authors")
@ApiResponse(responseCode = "401", description = "Authentication is required")
@ApiResponse(responseCode = "403", description = "Access token does not have required scope")
public class AuthorController {

    private final AuthorFacade facade;

    @Autowired
    public AuthorController(AuthorFacade facade) {
        this.facade = facade;
    }

    /**
     * REST method returning author with specified id.
     */
    @Operation(summary = "Returns identified author")
    @ApiResponse(responseCode = "200", description = "Author found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Author not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/{id}")
    public AuthorDto find(@PathVariable String id) {
        return facade.find(id);
    }

    /**
     * REST method returning all authors.
     */
    @Operation(summary = "Get all authors")
    @ApiResponse(responseCode = "200", description = "Pages list of all books", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<AuthorDto> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return facade.findAll(page, pageSize);
    }

    /**
     * REST method for creating a new author.
     */
    @Operation(summary = "Create a new author")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "201", description = "Author created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto create(@RequestBody @Valid AuthorCreateDto dto) {
        return facade.create(dto);
    }

    /**
     * REST method deleting specific author.
     */
    @Operation(summary = "Delete author by its ID")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "204", description = "Author deleted")
    @ApiResponse(responseCode = "404", description = "Author not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        facade.delete(id);
    }

    /**
     * REST method updating specific book.
     */
    @Operation(summary = "Update author by its ID")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "200", description = "Author updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Author not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PutMapping("/{id}")
    public AuthorDto update(@PathVariable String id, @RequestBody @Valid AuthorCreateDto dto) {
        return facade.update(id, dto);
    }
}
