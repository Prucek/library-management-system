package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.SECURITY_SCHEME_OAUTH2;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
 * Fines REST controller.
 *
 * @author Juraj Marcin
 */
@RestController
@RequestMapping(path = "/fines")
public class FineController {

    private final FineFacade fineFacade;


    /**
     * Creates a FineController instance.
     *
     * @param fineFacade FineFacade instance
     */
    public FineController(FineFacade fineFacade) {
        this.fineFacade = fineFacade;
    }

    /**
     * Creates a new fine.
     *
     * @param fineCreateDto fine data
     * @return created fine
     */
    @Operation(summary = "Create a new fine", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "Fine created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Issuer or borrowing not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FineDto create(@RequestBody FineCreateDto fineCreateDto) {
        try {
            return fineFacade.create(fineCreateDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    /**
     * Finds all fines.
     *
     * @param pageable page request
     * @return paged fines
     */
    @Operation(summary = "List all fines", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "Pages list of all fines", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @GetMapping
    public Result<FineDto> findAll(Pageable pageable) {
        return fineFacade.findAll(pageable);
    }

    /**
     * Finds a fine with id.
     *
     * @param id id of the fine
     * @return found fine
     */
    @Operation(summary = "Find fine with id", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "Fine found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @GetMapping(path = "{id}")
    public FineDto find(@PathVariable String id) {
        try {
            return fineFacade.find(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Fine %s not found", id));
        }
    }

    /**
     * Updates a fine with id.
     *
     * @param id            id of the fine
     * @param fineCreateDto new fine data
     * @return updated fine
     */
    @Operation(summary = "Update fine", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "Fine updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @PutMapping(path = "{id}")
    public FineDto update(@PathVariable String id, @RequestBody FineCreateDto fineCreateDto) {
        try {
            return fineFacade.update(id, fineCreateDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    /**
     * Deletes a fine with id.
     *
     * @param id id of the fine
     */
    @Operation(summary = "Delete fine", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "Fine deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        try {
            fineFacade.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }
}
