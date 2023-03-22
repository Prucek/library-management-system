package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book;


import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book.BookCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService service;
    private final BookMapper mapper;

    @Autowired
    public BookController(BookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public BookDto find(@PathVariable String id){
        return mapper.toDto(service.find(id));
    }

    @PostMapping
    public BookDto create(@RequestBody BookCreateDto dto){
        return mapper.toDto(service.create(mapper.fromCreateDto(dto)));
    }

    @GetMapping
    public Result<BookDto> findAll(@RequestParam(defaultValue = "0") int page){
        return mapper.toResult(service.findAll(page));
    }
}
