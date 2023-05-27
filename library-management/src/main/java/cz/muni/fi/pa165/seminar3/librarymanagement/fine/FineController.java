package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import static cz.muni.fi.pa165.seminar3.librarymanagement.Config.DEFAULT_PAGE_SIZE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_OAUTH2;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.USER_SCOPE;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
 * Fines REST controller.
 *
 * @author Juraj Marcin
 */
@RestController
@RequestMapping(path = "/fines")
@ApiResponse(responseCode = "401", description = "Authentication is required")
@ApiResponse(responseCode = "403", description = "Access token does not have required scope")
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
    @Operation(summary = "Create a new fine")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "201", description = "Fine created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Issuer or borrowing not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FineDto create(@RequestBody @Valid FineCreateDto fineCreateDto) {
        return fineFacade.create(fineCreateDto);
    }

    /**
     * Finds all fines.
     *
     * @param page     page number
     * @param pageSize size of the page
     * @return paged fines
     */
    @Operation(summary = "List all fines")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "200", description = "Pages list of all fines", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<FineDto> findAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return fineFacade.findAll(page, pageSize);
    }

    /**
     * Finds a fine with id.
     *
     * @param id id of the fine
     * @return found fine
     */
    @Operation(summary = "Find fine with id")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "200", description = "Fine found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping(path = "{id}")
    public FineDto find(@PathVariable String id) {
        return fineFacade.find(id);
    }

    /**
     * Updates a fine with id.
     *
     * @param id            id of the fine
     * @param fineCreateDto new fine data
     * @return updated fine
     */
    @Operation(summary = "Update fine")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "200", description = "Fine updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PutMapping(path = "{id}")
    public FineDto update(@PathVariable String id, @RequestBody @Valid FineCreateDto fineCreateDto) {
        return fineFacade.update(id, fineCreateDto);
    }

    /**
     * Deletes a fine with id.
     *
     * @param id id of the fine
     */
    @Operation(summary = "Delete fine")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "200", description = "Fine deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        fineFacade.delete(id);
    }
}
