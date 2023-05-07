package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import static cz.muni.fi.pa165.seminar3.librarymanagement.Config.DEFAULT_PAGE_SIZE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.SECURITY_SCHEME_OAUTH2;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Spring REST Controller for users.
 *
 * @author Peter Rúček
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserFacade userFacade;

    @Autowired
    public UserController(UserFacade facade) {
        this.userFacade = facade;
    }

    /**
     * REST method for creating a new user.
     */
    @Operation(summary = "Create a new fine", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "User created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserCreateDto userCreateDto) {
        try {
            return userFacade.create(userCreateDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    /**
     * REST method returning all users.
     */
    @Operation(summary = "List all users", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "Pages list of all users", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @GetMapping
    public Result<UserDto> findAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return userFacade.findAll(page, pageSize);
    }

    /**
     * REST method returning user with specified id.
     */
    @Operation(summary = "Find user with id", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "User found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @GetMapping("/{id}")
    public UserDto find(@PathVariable String id) {
        try {
            return userFacade.find(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Fine %s not found", id));
        }
    }

    /**
     * REST method for updating a new user.
     */
    @Operation(summary = "Update user", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "User updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @PutMapping("/{id}")
    public UserDto update(@PathVariable String id, @RequestBody UserCreateDto userCreateDto) {
        try {
            return userFacade.update(id, userCreateDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    /**
     * REST method for deleting a user.
     */
    @Operation(summary = "Delete user", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE}),
            @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    })
    @ApiResponse(responseCode = "200", description = "User deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - access token does not have scope test_2",
            content = @Content())
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        try {
            userFacade.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }
}