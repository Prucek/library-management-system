package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book;


import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author.AuthorService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book.BookCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService service;
    private final AuthorService authorService;
    private final BookMapper mapper;

    @Autowired
    public BookController(BookService service, BookMapper mapper, AuthorService authorService) {
        this.service = service;
        this.mapper = mapper;
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    public BookDto find(@PathVariable String id){
        return mapper.toDto(service.find(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BookDto create(@RequestBody BookCreateDto dto){
        List<Author> authors = new ArrayList<>();
        for ( AuthorDto authorDto : dto.getAuthors()) {
            Optional<Author> x = authorService.getRepository().findById(authorDto.getId());
            x.ifPresent(authors::add);
//            Todo: find by name
        }
        dto.setAuthors(new ArrayList<>());
        Book newBook = mapper.fromCreateDto(dto);
        newBook.setAuthors(authors);
        return mapper.toDto(service.create(newBook));
    }

    @GetMapping
    public Result<BookDto> findAll(@RequestParam(defaultValue = "0") int page){
        return mapper.toResult(service.findAll(page));
    }
}
