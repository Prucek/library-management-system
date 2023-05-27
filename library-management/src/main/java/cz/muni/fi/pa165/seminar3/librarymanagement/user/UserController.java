package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import static cz.muni.fi.pa165.seminar3.librarymanagement.Config.DEFAULT_PAGE_SIZE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_OAUTH2;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.USER_SCOPE;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
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
 * Spring REST Controller for users.
 *
 * @author Peter Rúček
 */
@RestController
@RequestMapping("/users")
@ApiResponse(responseCode = "401", description = "Authentication is required")
@ApiResponse(responseCode = "403", description = "Access token does not have required scope")
public class UserController {

    private final UserFacade userFacade;

    @Autowired
    public UserController(UserFacade facade) {
        this.userFacade = facade;
    }

    /**
     * REST method for creating a new user.
     *
     * @param userCreateDto user to be created
     * @return created user
     */
    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userFacade.create(userCreateDto);
    }

    /**
     * REST method returning all users.
     *
     * @param page page number
     * @param pageSize page size
     * @return list of all users
     */
    @Operation(summary = "List all users")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "200", description = "Pages list of all users", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<UserDto> findAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return userFacade.findAll(page, pageSize);
    }

    /**
     * REST method returning user with specified id.
     *
     * @param id user id
     * @return user with specified id
     */
    @Operation(summary = "Find user with id")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "200", description = "User found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/{id}")
    public UserDto find(@PathVariable String id) {
        return userFacade.find(id);
    }

    /**
     * REST method for updating a new user.
     *
     * @param id user id
     * @param userCreateDto user to be updated
     * @return updated user
     */
    @Operation(summary = "Update user")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "200", description = "User updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PutMapping("/{id}")
    public UserDto update(@PathVariable String id, @RequestBody @Valid UserCreateDto userCreateDto) {
        return userFacade.update(id, userCreateDto);
    }

    /**
     * REST method for deleting a user.
     *
     * @param id user id
     */
    @Operation(summary = "Delete user")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "204", description = "User deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        userFacade.delete(id);
    }
}