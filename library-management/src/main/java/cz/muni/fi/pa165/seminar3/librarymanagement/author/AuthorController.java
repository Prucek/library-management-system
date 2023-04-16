package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Spring REST Controller for Authors.
 *
 * @author MarekFiala
 */
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorFacade facade;

    @Autowired
    public AuthorController(AuthorFacade facade) {
        this.facade = facade;
    }

    /**
     * REST method returning author with specified id.
     */
    @Operation(summary = "Returns identified author", description = "Looks up a by by its id.")
    @ApiResponse(responseCode = "200", description = "Author found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Author not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/{id}")
    public AuthorDto find(@PathVariable String id) {
        try {
            return facade.find(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "author with id=" + id + " not found");
        }
    }

    /**
     * REST method returning all authors.
     */
    @Operation(summary = "Get all authors", description = "Returns all authors with authors as JSON")
    @ApiResponse(responseCode = "200", description = "Pages list of all books", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<AuthorDto> findAll(Pageable pageable) {
        return facade.findAll(pageable);
    }

    /**
     * REST method for creating a new author.
     */
    @Operation(summary = "Create a new author")
    @ApiResponse(responseCode = "201", description = "Author created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto create(@RequestBody AuthorCreateDto dto) {
        try {
            return facade.create(dto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    /**
     * REST method deleting specific author.
     */
    @Operation(summary = "Delete author by its ID")
    @ApiResponse(responseCode = "200", description = "Author deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Author not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        try {
            facade.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    /**
     * REST method updating specific book.
     */
    @Operation(summary = "Update author by its ID")
    @ApiResponse(responseCode = "200", description = "Author updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Author not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PutMapping("/{id}")
    public AuthorDto update(@PathVariable String id, @RequestBody AuthorCreateDto dto) {
        try {
            return facade.update(id, dto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }
}
