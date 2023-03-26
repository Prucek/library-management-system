package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Spring REST Controller for Authors
 */
@RestController
@OpenAPIDefinition( // metadata for inclusion into OpenAPI document
        info = @Info(title = "Authors REST API",
                version = "1.0",
                description = """
                        Simple service for authors REST API
                        """,
                contact = @Contact(name = "Marek Fiala", email = "xfiala6@fi.muni.cz"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        servers = @Server(description = "library management server", url = "{scheme}://{server}:{port}", variables = {
                @ServerVariable(name = "scheme", allowableValues = {"http", "https"}, defaultValue = "http"),
                @ServerVariable(name = "server", defaultValue = "localhost"),
                @ServerVariable(name = "port", defaultValue = "8080"),
        })
)
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService service;
    private final AuthorMapper mapper;

    @Autowired
    public AuthorController(AuthorService service, AuthorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * REST method returning author with specified id.
     */
    @Operation(
            summary = "Returns identified author",
            description = "Looks up a by by its id.",
            responses = {
                    @ApiResponse(responseCode = "302", ref = "#/components/responses/SingleBookResponse"),
                    @ApiResponse(responseCode = "404", description = "book not found",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public AuthorDto find(@PathVariable String id){
        Author author;
        try {
            author = service.find(id);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "author with id=" + id + " not found");
        }
        return mapper.toDto(author);
    }

    /**
     * REST method returning all authors.
     */
    @Operation( // metadata for inclusion into OpenAPI document
            summary = "Get all authors",
            description = """
                    Returns all authors as JSON
                    """)
    @GetMapping
    public Result<AuthorDto> findAll(@RequestParam(defaultValue = "0") int page){
        return mapper.toResult(service.findAll(page));
    }

    /**
     * REST method for creating a new author.
     */
    @Operation(
            summary = "Create a new author",
            responses = {
                    @ApiResponse(responseCode = "201", ref = "#/components/responses/SingleBookResponse")
//                    ,
//                    @ApiResponse(responseCode = "400", description = "input data were not correct",
//                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
//                    )
            }
    )
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto create(@RequestBody AuthorDto dto){
        return mapper.toDto(service.create(mapper.fromDto(dto)));
    }

    /**
     * REST method deleting specific author.
     */
    @Operation( // metadata for inclusion into OpenAPI document
            summary = "Delete author by its ID"
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        service.delete(service.find(id));
    }

    /**
     * REST method updating specific book.
     */
    @Operation( // metadata for inclusion into OpenAPI document
            summary = "Update author by its ID"
    )
    @PutMapping("/{id}")
    public AuthorDto update(@PathVariable String id) {
        return mapper.toDto(service.create(service.find(id)));
    }
}
