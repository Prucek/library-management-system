package cz.muni.fi.pa165.seminar3.librarymanagement.book;


import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.AuthorService;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST Controller for books
 */
@RestController
@OpenAPIDefinition( // metadata for inclusion into OpenAPI document
        info = @Info(title = "Books REST API",
                version = "1.0",
                description = """
                        Simple service for books REST API
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
@RequestMapping("/books")
public class BookController {
    private final BookService service;
    private final AuthorService authorService;
    private final BookMapper mapper;

    private final BookInstanceMapper instanceMapper;

    @Autowired
    public BookController(BookService service,
                          BookMapper mapper,
                          AuthorService authorService,
                          BookInstanceMapper instanceMapper) {
        this.service = service;
        this.mapper = mapper;
        this.authorService = authorService;
        this.instanceMapper = instanceMapper;
    }

    /**
     * REST method returning book with specified id.
     */
    @Operation(
            summary = "Returns identified book",
            description = "Looks up a by by its id.",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "#/components/responses/SingleBookResponse"),
                    @ApiResponse(responseCode = "404", description = "book not found",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public BookDto find(@PathVariable String id) {
        Book book;
        try {
            book = service.find(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "book with id=" + id + " not found");
        }
        return mapper.toDto(book);
    }

    /**
     * REST method returning all books.
     */
    @Operation( // metadata for inclusion into OpenAPI document
            summary = "Get all books",
            description = """
                    Returns all books with authors as JSON
                    """)
    @GetMapping
    public Result<BookDto> findAll(@RequestParam(defaultValue = "0") int page) {
        return mapper.toResult(service.findAll(page));
    }

    /**
     * REST method for creating a new book.
     */
    @Operation(
            summary = "Create a new book",
            description = """
                    Authors are defined by its id, if the ID is not found,
                    the book is created without author
                    """,
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
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestBody BookDto dto) {
        List<Author> authors = new ArrayList<>();
        for (AuthorDto authorDto : dto.getAuthors()) {
            Optional<Author> x = authorService.getRepository().findById(authorDto.getId());
            x.ifPresent(authors::add);
//            Todo: find by name
        }
        dto.setAuthors(new ArrayList<>());
        Book newBook = mapper.fromDto(dto);
        newBook.setAuthors(authors);
        return mapper.toDto(service.create(newBook));
    }

    /**
     * REST method deleting specific book.
     */
    @Operation( // metadata for inclusion into OpenAPI document
            summary = "Delete book by its ID"
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(service.find(id));
    }

    /**
     * REST method updating specific book.
     */
    @Operation( // metadata for inclusion into OpenAPI document
            summary = "Update book by its ID"
    )
    @PutMapping("/{id}")
    public BookDto update(@PathVariable String id) {
        return mapper.toDto(service.create(service.find(id)));
    }

    @Operation(summary = "Add book instance")
    @ApiResponse(responseCode = "200", description = "Book instance added", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Book not found", useReturnTypeSchema = true)
    @PostMapping("/{bookId}/instances")
    public BookInstanceDto addInstance(@PathVariable String bookId) {
        try {
            return instanceMapper.toDto(service.addInstance(bookId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    @Operation(summary = "Remove book instance")
    @ApiResponse(responseCode = "200", description = "Book instance added", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Book or book instance not found", useReturnTypeSchema = true)
    @DeleteMapping("/{bookId}/instances/{id}")
    public void addInstance(@PathVariable String bookId,
                            @PathVariable String id) {
        try {
            service.removeInstance(service.getInstance(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }
}
