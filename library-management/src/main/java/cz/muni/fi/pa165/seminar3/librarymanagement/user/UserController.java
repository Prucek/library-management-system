package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Spring REST Controller for users
 */
@RestController
@OpenAPIDefinition( // metadata for inclusion into OpenAPI document
        // see javadoc at https://javadoc.io/doc/io.swagger.core.v3/swagger-annotations/latest/index.html
        // see docs at https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations
        info = @Info(title = "Users REST API",
                version = "1.0",
                description = """
                        Simple service for users REST API
                        """,
                contact = @Contact(name = "Peter Rucek", email = "xrucek@fi.muni.cz"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        servers = @Server(description = "library management server", url = "{scheme}://{server}:{port}", variables = {
                @ServerVariable(name = "scheme", allowableValues = {"http", "https"}, defaultValue = "http"),
                @ServerVariable(name = "server", defaultValue = "localhost"),
                @ServerVariable(name = "port", defaultValue = "8080"),
        })
)
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @Autowired
    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * REST method returning user with specified id.
     */
    @Operation(
            summary = "Returns identified user",
            description = "Looks up a user by its id.",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "#/components/responses/SingleUserResponse"),
                    @ApiResponse(responseCode = "404", description = "message not found",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public UserDto find(@PathVariable String id) {
        User user = service.find(id);
        if (user != null){
            return mapper.toDto(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id=" + id + " not found");
        }
    }

    /**
     * REST method for creating a new user.
     */
    @Operation(
            summary = "Create a new user",
            description = """
                    """,
            responses = {
                    @ApiResponse(responseCode = "201", ref = "#/components/responses/SingleUserResponse"),
                    @ApiResponse(responseCode = "400", description = "input data were not correct",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserCreateDto dto) {
        return mapper.toDto(service.create(mapper.fromCreateDto(dto)));
    }

    /**
     * REST method returning all users.
     */
    @Operation( // metadata for inclusion into OpenAPI document
            summary = "Get all users",
            description = """
                    Returns all users as JSON
                    """)
    @GetMapping
    public Result<UserDto> findAll(Pageable page) {
        return mapper.toResult(service.findAll(page));
    }
}