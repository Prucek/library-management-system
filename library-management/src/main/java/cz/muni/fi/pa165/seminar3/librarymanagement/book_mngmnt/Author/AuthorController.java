package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Author.AuthorCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService service;
    private final AuthorMapper mapper;

    @Autowired
    public AuthorController(AuthorService service, AuthorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public AuthorDto find(@PathVariable String id){
        return mapper.toDto(service.find(id));
    }

    @GetMapping
    public Result<AuthorDto> findAll(@RequestParam(defaultValue = "0") int page){
        return mapper.toResult(service.findAll(page));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDto create(@RequestBody AuthorCreateDto dto){
        return mapper.toDto(service.create(mapper.fromCreateDto(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        service.delete(service.find(id));
    }

    @PutMapping("/{id}")
    public AuthorDto update(@PathVariable String id) {
        return mapper.toDto(service.create(service.find(id)));
    }
}
